class RingBuffer
{
  Pose[] poseArray;
  Pose[] poseNormalizedArray;
  int startOfBuffer = 0;
  
  Float d[][][] = new Float[nbrOfMoves][framesGestureMax][framesInputMax];
  int P[][][] = new int[nbrOfMoves][framesGestureMax][framesInputMax];
  Float D[][] = new Float[framesGestureMax][framesInputMax];
  
  float cost[];
  float costLast[];

  // constructor
  RingBuffer () {
    poseArray = new Pose[framesInputMax];
    poseNormalizedArray = new Pose[framesInputMax];
    
    cost = new float[nbrOfMoves];
    costLast = new float[nbrOfMoves];

    for (int m = 0; m < framesInputMax; m++) 
    {
      poseArray[m] = new Pose();
      poseNormalizedArray[m] = new Pose();            
      for (int n = 0; n < framesGestureMax; n++)
        for (int moveId = 0; moveId < nbrOfMoves; moveId++)
          d[moveId][n][m] = 0.0;
    }
  }

  // a new pose will be saved to the ringbuffer (containing current and previous framesInputMax-1 frames)
  // the ring buffer mechanism uses one pointer: startOfBuffer to determine the current start of a pose
  void fillBuffer(Pose newPose) {
    startOfBuffer = (startOfBuffer + 1) % framesInputMax;
    counter++;
    poseArray[startOfBuffer].copyFrom(newPose);
  }

  // a new rotation normalized pose will be saved to the ringbuffer (containing current and previous framesInputMax-1 frames)
  // the ring buffer mechanism uses one pointer which is set in fillBufer(), not in this routine
  void fillBufferNormalized(Pose newPose) {    
    poseNormalizedArray[startOfBuffer].copyFrom(newPose);
  }

  void copyBuffer(Move move) {
    println("copy buffer!");
    for (int i = 0; i < framesGestureMax; i++) 
      move.get(i).copyFrom(poseArray[(startOfBuffer + i + framesGestureMax) % framesInputMax]);
  }  

  float cost(Move move, int poseId, int thatId) 
  {
    return cost(move, move.get(poseId), poseArray[thatId]);
  }

  float costNormalized(Move move, int poseId, int thatId) 
  {
    return cost(move, move.get(poseId), poseNormalizedArray[thatId]);
  }
    // calculate the cost of one frame
  float cost(Move move, Pose pose, Pose that) 
  {
    if (move.weightLeftOrRight > 1.0) move.weightLeftOrRight = 1.0;
    if (move.weightLeftOrRight < -1.0) move.weightLeftOrRight = -1.0;

    float weight_left = 1.0 + move.weightLeftOrRight;
    float weight_right = 1.0 - move.weightLeftOrRight;

    float mse = 0.0;
    
    mse += weight_left * sqrt( move.weightX * sq(pose.jointLeftShoulderRelative.x - that.jointLeftShoulderRelative.x) 
      + move.weightY * sq(pose.jointLeftShoulderRelative.y - that.jointLeftShoulderRelative.y)
      + move.weightZ * sq(pose.jointLeftShoulderRelative.z - that.jointLeftShoulderRelative.z) );
      
    mse += weight_left * sqrt( move.weightX * sq(pose.jointLeftElbowRelative.x - that.jointLeftElbowRelative.x)
      + move.weightY * sq(pose.jointLeftElbowRelative.y - that.jointLeftElbowRelative.y)
      + move.weightZ * sq(pose.jointLeftElbowRelative.z - that.jointLeftElbowRelative.z) );
      
    mse += weight_left * sqrt( move.weightX * sq(pose.jointLeftHandRelative.x - that.jointLeftHandRelative.x)
      + move.weightY * sq(pose.jointLeftHandRelative.y - that.jointLeftHandRelative.y)
      + move.weightZ * sq(pose.jointLeftHandRelative.z - that.jointLeftHandRelative.z) );
      
    mse += weight_right * sqrt( move.weightX * sq(pose.jointRightShoulderRelative.x - that.jointRightShoulderRelative.x)
      + move.weightY * sq(pose.jointRightShoulderRelative.y - that.jointRightShoulderRelative.y)
      + move.weightZ * sq(pose.jointRightShoulderRelative.z - that.jointRightShoulderRelative.z) );
      
    mse += weight_right * sqrt( move.weightX * sq(pose.jointRightElbowRelative.x - that.jointRightElbowRelative.x)
      + move.weightY * sq(pose.jointRightElbowRelative.y - that.jointRightElbowRelative.y)
      + move.weightZ * sq(pose.jointRightElbowRelative.z - that.jointRightElbowRelative.z) );
      
    mse += weight_right * sqrt( move.weightX * sq(pose.jointRightHandRelative.x - that.jointRightHandRelative.x)
      + move.weightY * sq(pose.jointRightHandRelative.y - that.jointRightHandRelative.y)
      + move.weightZ * sq(pose.jointRightHandRelative.z - that.jointRightHandRelative.z) );

    return mse;
  }

  // calculate the 'cost' of the different moves using DTW
  float pathcost(int moveId)
  {
    Move move = moves[moveId];
    if (move.normRotation)
      // evaluate only for move.framesGesture frames (the last frames)
      for (int n = (framesGestureMax - move.framesGesture); n < framesGestureMax; n++)
        d[moveId][n][(startOfBuffer + framesInputMax - 1) % framesInputMax] = costNormalized( move, (n+framesGestureMax+1)% framesGestureMax, (0 + startOfBuffer) % framesInputMax);
    
    else
      // evaluate only for move.framesGesture frames (the last frames)
      for (int n = (framesGestureMax - move.framesGesture); n < framesGestureMax; n++)
        d[moveId][n][(startOfBuffer + framesInputMax - 1) % framesInputMax] = cost( move, (n+framesGestureMax+1)% framesGestureMax, (0 + startOfBuffer) % framesInputMax);

    float cost = 0;
    if (counter > framesInputMax+1)
    {
      D[framesGestureMax-move.framesGesture][framesInputMax-2*move.framesGesture] = d[moveId][framesGestureMax-move.framesGesture][(startOfBuffer) % framesInputMax];
      P[moveId][framesGestureMax-move.framesGesture][framesInputMax-2*move.framesGesture] = 0;

      // evaluate only for move.framesGesture frames (the last frames)
      for (int n= (framesGestureMax-move.framesGesture+1); n<framesGestureMax; n++)
      {
        D[n][framesInputMax-2*move.framesGesture]=d[moveId][n][(startOfBuffer) % (2*move.framesGesture)] + D[n-1][framesInputMax-2*move.framesGesture];
        P[moveId][n][framesInputMax-2*move.framesGesture] = 1;
      }

      for (int m= (framesInputMax-2*move.framesGesture)+1; m<framesInputMax; m++)
      {
        D[framesGestureMax-move.framesGesture][m] = d[moveId][0][(m + startOfBuffer) % (2*move.framesGesture)];
        P[moveId][framesGestureMax-move.framesGesture][m] = -1;
      }

      // evaluate only for move.framesGesture frames (the last frames)
      for (int n= (framesGestureMax-move.framesGesture+1); n<framesGestureMax; n++)
        for (int m= (framesInputMax-2*move.framesGesture)+1; m<framesInputMax; m++)
          D[n][m] = d[moveId][n][(m + startOfBuffer) % framesInputMax] + min( D[n-1][m-1], D[n][m-1], D[n][m-1] );

      float countAdjust = 3.0;
      // evaluate only for move.framesGesture frames (the last frames)
      for (int n= (framesGestureMax-move.framesGesture+1); n<framesGestureMax; n++) {
        for (int m= (framesInputMax-2*move.framesGesture)+1; m<framesInputMax; m++)
        {
          P[moveId][n][m] = 0;
          if (D[n][m-1] < D[n-1][m-1]) P[moveId][n][m] = -1;
          if (D[n-1][m] < D[n-1][m-1]) 
          {
            P[moveId][n][m] = 1;
            if (D[n][m-1] < D[n-1][m]) P[moveId][n][m] = -1;
          }
          // adjust a little here to detect faster events
          if (P[moveId][n][m] < 0)
          {
            D[framesGestureMax-2][framesInputMax-2] -= 0.01/countAdjust*(1.0-(counterEvent/25.0))*D[n][m]; 
            countAdjust++;
          }
        }
      }

      int n = framesGestureMax-2;
      int m = framesInputMax-2;   
      speed[moveId] = 0.0;
      float adjust = framesGestureMax;
      for (int i = 0; i < 2*framesInputMax; i++) 
      {
        int tempN = n;
        if (P[moveId][n][m] >= 0) tempN--;
        if (P[moveId][n][m] <= 0) m--;
        n = tempN;  

        // average speed values 
        // speed[moveId] -=  m-0.5*framesInputMax-n;

        if (n == framesGestureMax - 4) 
          speed[moveId] = m;

        if (n <= framesGestureMax-move.framesGesture) 
        {
          steps[moveId] = i;
          adjust = (((float) framesInputMax)-m) / ((float) framesGestureMax);
          i = 2*framesInputMax;
        }
        if (m < 0) m = 0;
      }
      steps[moveId]++;
      speed[moveId] -= m;
      speed[moveId] /= framesGestureMax-4.0;
      // speed[moveId] /= (float) steps[moveId];

      // better results by normalizing by framesGestureMax instead of steps
      // cost = D[framesGestureMax-2][framesInputMax-2]/((float) framesGestureMax);
      cost = D[framesGestureMax-2][framesInputMax-2]/steps[moveId];
    }

    return cost;
  }

  void display() 
  {
    if (counter < framesInputMax+1) return;
    float maximum = 0;

    noStroke(); 
    for (int n = 0; n < (framesGestureMax-1); n++) 
      for (int m = 0; m < (framesInputMax-1); m++) 
        if (d[gui.displayCost][n][m] > maximum)
          maximum  = d[gui.displayCost][n][m];

    fill(0, 0, 0);
    rect(context.depthWidth(), 90, 200, 400);

    for (int i = framesGestureMax-moves[gui.displayCost].framesGesture; i < (framesGestureMax-1); i++)
    {
      for (int j = framesInputMax-2*moves[gui.displayCost].framesGesture; j < (framesInputMax-1); j++)
      {
        float value = 255-255*d[gui.displayCost][i][(j + startOfBuffer) % framesInputMax]/maximum;
        fill(value);
        rect(context.depthWidth() + (i-framesGestureMax+moves[gui.displayCost].framesGesture)*400.0/(2.0*moves[gui.displayCost].framesGesture), (j-framesInputMax+2*moves[gui.displayCost].framesGesture)*400.0/(2.0*moves[gui.displayCost].framesGesture)+90.0, 400.0/(2.0*moves[gui.displayCost].framesGesture), 400.0/(2.0*moves[gui.displayCost].framesGesture));
      }
    }

    int n = framesGestureMax-2;
    int m = framesInputMax-2;   
    for (int i = 0; i <= steps[gui.displayCost]; i++) 
    {
      float value = 255-255*d[gui.displayCost][n][(m + startOfBuffer) % framesGestureMax]/maximum;
      fill(value, 0, 0);
      rect(context.depthWidth() + (n-framesGestureMax+moves[gui.displayCost].framesGesture)*400/(2*moves[gui.displayCost].framesGesture), (m-framesInputMax+2*moves[gui.displayCost].framesGesture)*400/(2*moves[gui.displayCost].framesGesture)+90, 400/(2*moves[gui.displayCost].framesGesture), 400/(2*moves[gui.displayCost].framesGesture));                        
      int tempN = n;
      if (P[gui.displayCost][n][m] >= 0) tempN--;
      if (P[gui.displayCost][n][m] <= 0) m--;
      n = tempN;
      if (n < 0) n = 0;
    }

    fill(0, 0, 0);
    rect(context.depthWidth(), 0, 195, 90);

    textAlign(CENTER);
    textFont(gui.fontA32, 32);
    fill(255, 255, 255);
    text("analyse", context.depthWidth() + 100, 35);
    text("figure #" + gui.displayCost, context.depthWidth() + 100, 75);
    
    // find best match
    if (cost.length < 1) return;
    float bestcost = cost[0];
    int whichcost = 0;
    for (int i = 0; i < nbrOfMoves; i++)
    {
      if (!moves[i].empty && cost[i] < bestcost)
      {
        bestcost = cost[i];
        whichcost = i;
      }
    }

    if (cost[whichcost] < 0.3 && costLast[whichcost] >= 0.3)
    {
      fill(0, 0, 0);
      rect(context.depthWidth() + 200, 0, 200, 90);

      stroke(0, 0, 0);
      fill(255, 255, 255);

      text("found", context.depthWidth() + 300, 35);  
      text("event #" + whichcost, context.depthWidth() + 300, 75);
      counterEvent = 25;
    }

    if (counterEvent < 1)
    {
      fill(0, 0, 0);
      rect(context.depthWidth() + 200, 0, 200, 200);

      stroke(0, 0, 0);
      fill(255, 255, 255);

      text("found", context.depthWidth() + 300, 35);
      text("no event", context.depthWidth() + 300, 75);
    } else
    {
      counterEvent--;
    }

    if (costLast[whichcost] < 0.3 && costLast[whichcost] > cost[whichcost])
    {
      fill(0, 0, 0);
      rect(context.depthWidth() + 200, 80, 200, 200);

      fill(255, 255, 255);
      text("motion speed:", context.depthWidth() + 300, 115); 

      if (speed[whichcost] < 1.0/1.5) 
        text("much faster", context.depthWidth() + 300, 155);
      else if (speed[whichcost] < 1.0/1.25) 
        text("faster", context.depthWidth() + 300, 155);
      else if ( (speed[whichcost] >= 1.0/1.25) && (speed[whichcost] <= 1.25) )
        text("similar", context.depthWidth() + 300, 155);
      else if (speed[whichcost] > 1.5) 
        text("much slower", context.depthWidth() + 300, 155);
      else if (speed[whichcost] > 1.25) 
        text("slower", context.depthWidth() + 300, 155);

      counterEvent = 25;
    }
  }
}
