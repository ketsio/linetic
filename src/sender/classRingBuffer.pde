class RingBuffer
{
  Pose[] poseArray;
  Pose[] poseNormalizedArray;
  int startOfBuffer = 0;
  boolean enoughFrames = false;

  Float d[][][] = new Float[nbrOfMoves][framesGestureMax][framesInputMax];
  int P[][][] = new int[nbrOfMoves][framesGestureMax][framesInputMax];
  Float D[][] = new Float[framesGestureMax][framesInputMax];

  float cost[] = new float[nbrOfMoves];
  float costLast[] = new float[nbrOfMoves];
  
  // Settings
  int framesDelayed = 2;
  float quantiles = 5;


  // constructor
  RingBuffer () {
    poseArray = new Pose[framesInputMax];
    poseNormalizedArray = new Pose[framesInputMax];

    for (int m = 0; m < framesInputMax; m++) 
    {
      poseArray[m] = new Pose();
      poseNormalizedArray[m] = new Pose();            
      for (int n = 0; n < framesGestureMax; n++)
        for (int moveId = 0; moveId < nbrOfMoves; moveId++)
          d[moveId][n][m] = 0.0;
    }
  }

  void next() {
    startOfBuffer = (startOfBuffer + 1) % framesInputMax;
    if (!enoughFrames)
      if (++counter >= framesInputMax)
        enoughFrames = true;
  }

  // a new pose will be saved to the ringbuffer (containing current and previous framesInputMax-1 frames)
  // the ring buffer mechanism uses one pointer: startOfBuffer to determine the current start of a pose
  void fillBuffer(Pose newPose) {
    next();
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


  float cost(Move move, int poseId, int thatId) {
    return cost(move, move.get(poseId), poseArray[thatId]);
  }


  float costNormalized(Move move, int poseId, int thatId) {
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

    int framesBufferAnalyzed = getFramesBufferAnalyzed(move);
    int framesMoveAnalyzed = getFramesMoveAnalyzed(move);
    //int endOfAnalysis = (startOfBuffer - framesBufferAnalyzed + framesInputMax) % framesInputMax;

    // evaluate only for the last move.framesGesture frames (compare with the last pose i.e. the pose at startOfBuffer)
    if (move.normRotation)
      for (int m = 0; m < framesMoveAnalyzed; m++)
        d[moveId][m][startOfBuffer] = costNormalized(move, m, startOfBuffer);
    else
      for (int m = 0; m < framesMoveAnalyzed; m++)
      d[moveId][m][startOfBuffer] = cost(move, m, startOfBuffer);

    float cost = 0;
    if (enoughFrames)
    {
      D[0][0] = d[moveId][framesMoveAnalyzed-1][startOfBuffer];
      P[moveId][0][0] = 0;

      for (int m = 0; m < framesMoveAnalyzed; m++) {
        D[m][0] = d[moveId][(framesGestureMax + framesMoveAnalyzed-1 - m) % framesGestureMax][startOfBuffer] + D[m-1][0];
        P[moveId][m][0] = 1;
      }

      for (int n = 0; n < framesBufferAnalyzed; n++) {
        D[0][n] = d[moveId][framesMoveAnalyzed-1][(framesInputMax + startOfBuffer - n) % framesInputMax] + D[0][n-1];
        P[moveId][0][n] = -1;
      }

      for (int m = 1; m < framesMoveAnalyzed; m++)
        for (int n = 1; n < framesBufferAnalyzed; n++)
          D[m][n] = d[moveId][(framesGestureMax + framesMoveAnalyzed-1 - m) % framesGestureMax][(framesInputMax + startOfBuffer - n) % framesInputMax] + min( D[m-1][n-1], D[m][n-1], D[m-1][n] );

      // float countAdjust = 3.0;

      for (int m = 1; m < framesMoveAnalyzed; m++) {
        for (int n = 1; n < framesBufferAnalyzed; n++)
        {
          P[moveId][m][n] = 0;
          if (D[m][n-1] < D[m-1][n-1]) P[moveId][m][n] = -1;
          if (D[m-1][n] < D[m-1][n-1]) {
            P[moveId][m][n] = 1;
            if (D[m][n-1] < D[m-1][n]) P[moveId][m][n] = -1;
          }
          // adjust a little here to detect faster events
          // if (P[moveId][m][n] < 0)
          // {
          //  D[framesGestureMax-2][framesInputMax-2] -= 0.01/countAdjust*(1.0-(counterEvent/25.0))*D[m][n]; 
          //  countAdjust++;
          // }
        }
      }

      int M = framesMoveAnalyzed - 1;
      int N = framesBufferAnalyzed - framesDelayed - 1;
      int m = M;
      int n = N;
      
      steps[moveId] = 1;
      // float adjust = framesGestureMax;
      
      float firstQuantile = 1.0 / quantiles;
      float lastQuantile = 1.0 - firstQuantile;
      
      int beginMove = -1;
      int endMove = -1;

      // Backtracking From [M,N] to [0,0]
      while (m > 0 || n > 0) 
      {
        int currentCell = P[moveId][m][n];
        if (currentCell >= 0) max(m--, 0);
        if (currentCell <= 0) max(n--, 0);

        // average speed values 
        // speed[moveId] -=  n-0.5*framesInputMax-m;

        // if (m == framesGestureMax - 4) 
        //   speed[moveId] = n;

        // if (m <= framesGestureMax-framesMoveAnalyzed) 
        // {
        //   steps[moveId] = i;
        //   adjust = (((float) framesInputMax)-n) / ((float) framesGestureMax);
        //   i = 2*framesInputMax;
        // }
        
        if(m <= firstQuantile && endMove == -1)
          endMove = n;
          
        if(m <= lastQuantile && beginMove == -1)
          beginMove = n;
        
        steps[moveId]++;
      }
      speed[moveId] = abs(endMove - beginMove);
      speed[moveId] /= framesMoveAnalyzed;

      // better results by normalizing by framesGestureMax instead of steps
      // cost = D[M][N]/((float) move.framesGesture);
      cost = D[M][N]/steps[moveId];
    }

    return cost;
  }


  void display() 
  {
    PVector bufferOrigin = new PVector(context.depthWidth() + 2 * gui.padding, gui.padding);
    int bufferWidth = int(width - 3 * gui.padding - bufferOrigin.x);
    int bufferHeight = int(bufferWidth / 2.0);
    PVector textOrigin = new PVector(context.depthWidth() + 2 * gui.padding, 2 * gui.padding + bufferHeight);
    display(bufferOrigin, bufferWidth, bufferHeight, textOrigin);
  }


  void display(PVector bufferOrigin, int bufferWidth, int bufferHeight, PVector textOrigin) 
  {
    if (!enoughFrames) return;
    float maximum = 0;

    Move move = moves[gui.displayCost];
    int framesBufferAnalyzed = getFramesBufferAnalyzed(move);
    int framesMoveAnalyzed = getFramesMoveAnalyzed(move);

    int squareWidth = bufferWidth / (framesBufferAnalyzed - framesDelayed);
    int squareHeight = bufferHeight / framesMoveAnalyzed;

    for (int m = 0; m < framesMoveAnalyzed; m++) 
      for (int n = 0; n < framesBufferAnalyzed; n++) 
        if (d[gui.displayCost][(framesGestureMax + framesMoveAnalyzed-1 - m) % framesGestureMax][(framesInputMax + startOfBuffer - n) % framesInputMax] > maximum)
          maximum  = d[gui.displayCost][(framesGestureMax + framesMoveAnalyzed-1 - m) % framesGestureMax][(framesInputMax + startOfBuffer - n) % framesInputMax];
          
    //println("maximum : " + maximum);

    noStroke(); 
    fill(0, 0, 0);

    for (int m = 0; m < framesMoveAnalyzed; m++) {
      for (int n = 0; n < framesBufferAnalyzed; n++)
      {
        int a = (framesGestureMax + framesMoveAnalyzed-1 - m) % framesGestureMax;
        int b = (framesInputMax + startOfBuffer - n) % framesInputMax;
        float value = 255 - 255 * d[gui.displayCost][a][b] / maximum;
        fill(value);
        rect(bufferOrigin.x + b * squareWidth, bufferOrigin.y + a * squareHeight, squareWidth, squareHeight);
      }
    }

    int m = move.framesGesture - 1;
    int n = framesBufferAnalyzed - framesDelayed - 1;

    for (int i = 0; i <= steps[gui.displayCost]; i++) 
    {
      int a = (framesGestureMax + framesMoveAnalyzed-1 - m) % framesGestureMax;
      int b = (framesInputMax + startOfBuffer - n) % framesInputMax;
      float value = 255 - 255 * d[gui.displayCost][a][b] / maximum;
      fill(value, 0, 0);
      rect(bufferOrigin.x + b * squareWidth, bufferOrigin.y + a * squareHeight, squareWidth, squareHeight);  

      int currentCell = P[gui.displayCost][m][n];
      if (currentCell >= 0) max(--m, 0);
      if (currentCell <= 0) max(--n, 0);
    }

    textAlign(LEFT);
    textFont(gui.fontA12);
    fill(0);
    text("analysing gesture #" + gui.displayCost, textOrigin.x, textOrigin.y);

    // find best match
    if (cost.length < 1) return;
    float bestcost = cost[0];
    int whichcost = 0;
    for (int i = 0; i < nbrOfMoves; i++)
      if (!moves[i].empty && cost[i] < bestcost) {
        bestcost = cost[i];
        whichcost = i;
      }

    if (cost[whichcost] < 0.3 && costLast[whichcost] >= 0.3)
    {
      text("found gesture #" + whichcost, textOrigin.x, textOrigin.y + 40);
      counterEvent = 25;
    }

    if (counterEvent < 1)
    {
      text("found no event", textOrigin.x, textOrigin.y + 40);
    } else {
      counterEvent--;
    }

    if (costLast[whichcost] < 0.3 && costLast[whichcost] > cost[whichcost])
    {
      text("speed: " + speed[whichcost], textOrigin.x, textOrigin.y + 80); 
      counterEvent = 25;
    }
  }
  
  private int getFramesBufferAnalyzed(Move move) {
    return move.framesGesture * 2;
  }
  
  private int getFramesMoveAnalyzed(Move move) {
    return move.framesGesture;
  }
}

