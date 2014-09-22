class RingBuffer
{
  Pose[] poseArray;
  Pose[] poseNormalizedArray;
  int startOfBuffer = 0;

  Float d[][][] = new Float[nbrOfMoves][framesGestureMax][framesInputMax];
  int P[][][] = new int[nbrOfMoves][framesGestureMax][framesInputMax];
  Float D[][] = new Float[framesGestureMax][framesInputMax];

  float cost[] = new float[nbrOfMoves];
  float costLast[] = new float[nbrOfMoves];
  
  // Settings
  int framesDelayed = 2;
  float quantiles = 5;
  
  // States
  boolean enoughFrames = false;
  boolean recording = false;
  Move recMove = null;
  
  // Counters
  int initCounter = 0;
  int recCounter = 0;


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
    if (!enoughFrames && initCounter++ > framesInputMax)
      enoughFrames = true;
    if (recording && recCounter++ > framesGestureMax)
      stopRecording();
  }


  void fillBuffer(Pose newPose) {
    next();
    poseArray[startOfBuffer].copyFrom(newPose);
  }

  void fillBufferNormalized(Pose newPose) {    
    poseNormalizedArray[startOfBuffer].copyFrom(newPose);
  }


  void record(Move move) {
    if (recording)
      return;
      
    recCounter = 0;
    recording = true;
    recMove = move;
  }
  
  void stopRecording() {
    if (!recording)
      return;
      
    recording = false;
    copyBuffer(recMove, recCounter);
  }
  
  void copyBuffer(Move move, int frames) {
    println("copy buffer!");
    for (int i = 0; i < frames; i++) 
      move.get(i).copyFrom(poseArray[(startOfBuffer - frames + i + framesInputMax) % framesInputMax]);
  }  

  void updateCost(int moveId) {
    costLast[moveId] = cost[moveId];
    cost[moveId] = pathcost(moveId);
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

    mse += weight_left * dist(pose.jointLeftShoulderRelative, that.jointLeftShoulderRelative, move);
    mse += weight_left * dist(pose.jointLeftElbowRelative, that.jointLeftElbowRelative, move);
    mse += weight_left * dist(pose.jointLeftHandRelative, that.jointLeftHandRelative, move);

    mse += weight_right * dist(pose.jointRightShoulderRelative, that.jointRightShoulderRelative, move);
    mse += weight_right * dist(pose.jointRightElbowRelative, that.jointRightElbowRelative, move);
    mse += weight_right * dist(pose.jointRightHandRelative, that.jointRightHandRelative, move);

    mse /= (3 * weight_left + 3 * weight_right);
    
    return mse;
  }
  
  float dist(PVector a, PVector b, Move m) {
    float result = sq(a.x - b.x) * m.weightX;
    result += sq(a.y - b.y) * m.weightY;
    result += sq(a.z - b.z) * m.weightZ;
    result /= (m.weightX + m.weightY + m.weightZ);
    return sqrt(result);
  }
  
  float getPercent(int moveId) {
    return costToPercent(cost[moveId]);
  }
  
  float costToPercent(float cost) {
    float minC = 20;
    float maxC = 150;
    float matching = 1.0 - ((cost - minC) / (maxC - minC));
    return min(1.0, max(0.01, matching));
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

      for (int m = 1; m < framesMoveAnalyzed; m++) {
        D[m][0] = d[moveId][(framesGestureMax + framesMoveAnalyzed-1 - m) % framesGestureMax][startOfBuffer] + D[m-1][0];
        P[moveId][m][0] = 1;
      }

      for (int n = 1; n < framesBufferAnalyzed; n++) {
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
      
      float firstQuantile = (1.0 / quantiles) * M;
      float lastQuantile = (1.0 - firstQuantile) * M;
      
      int beginMove = -1;
      int endMove = -1;

      // Backtracking From [M,N] to [0,0]
      while (m > 0 || n > 0) 
      {
        int currentCell = P[moveId][m][n];

        // average speed values 
        // speed[moveId] -=  n-0.5*M-m;

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
        
        // Next
        if (currentCell >= 0) max(m--, 0);
        if (currentCell <= 0) max(n--, 0);
      }
      
      // Speed calculation
      speed[moveId] = abs(endMove - beginMove);
      speed[moveId] /= framesMoveAnalyzed;

      // better results by normalizing by framesGestureMax instead of steps
      // cost = D[M][N]/((float) move.framesGesture);
      // cost = D[M][N]/steps[moveId];
      
      cost = D[M][N]/(M+N);
      if (cost > maxCost)
        maxCost = cost;
      if (cost < minCost && enoughFrames)
        minCost = cost;
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
    if (!enoughFrames)
      return;

    Move move = moves[gui.highlightedMove];
    int framesBufferAnalyzed = getFramesBufferAnalyzed(move);
    int framesMoveAnalyzed = getFramesMoveAnalyzed(move);

    int squareWidth = bufferWidth / (framesBufferAnalyzed - framesDelayed);
    int squareHeight = bufferHeight / framesMoveAnalyzed;

    float maximum = 0;
    for (int m = 0; m < framesMoveAnalyzed; m++) 
      for (int n = 0; n < framesBufferAnalyzed; n++) 
        if (d[gui.highlightedMove][(framesGestureMax + framesMoveAnalyzed-1 - m) % framesGestureMax][(framesInputMax + startOfBuffer - n) % framesInputMax] > maximum)
          maximum  = d[gui.highlightedMove][(framesGestureMax + framesMoveAnalyzed-1 - m) % framesGestureMax][(framesInputMax + startOfBuffer - n) % framesInputMax];
          
    noStroke();

    for (int m = 0; m < framesMoveAnalyzed; m++) {
      for (int n = 0; n < framesBufferAnalyzed; n++)
      {
        int a = (framesGestureMax + framesMoveAnalyzed-1 - m) % framesGestureMax;
        int b = (framesInputMax + startOfBuffer - n) % framesInputMax;
        float value = 255 - 255 * d[gui.highlightedMove][a][b] / maximum;
        fill(value);
        rect(bufferOrigin.x + n * squareWidth, bufferOrigin.y + m * squareHeight, squareWidth, squareHeight);
      }
    }

    int m = framesMoveAnalyzed - 1;
    int n = framesBufferAnalyzed - framesDelayed - 1;

    for (int i = 0; i < steps[gui.highlightedMove]; i++) 
    {
      int a = (framesGestureMax + framesMoveAnalyzed-1 - m) % framesGestureMax;
      int b = (framesInputMax + startOfBuffer - n) % framesInputMax;
      float value = 255 - 255 * d[gui.highlightedMove][a][b] / maximum;
      fill(value, 0, 0);
      rect(bufferOrigin.x + n * squareWidth, bufferOrigin.y + m * squareHeight, squareWidth, squareHeight);  

      int currentCell = P[gui.highlightedMove][m][n];
      if (currentCell >= 0) max(--m, 0);
      if (currentCell <= 0) max(--n, 0);
    }

    // Reinitializing text
    fill(gui.backgroundColor);
    rect(textOrigin.x, textOrigin.y - 20, bufferWidth, 120);

    textAlign(LEFT);
    textFont(gui.fontA12);
    fill(0);
    text("analysing gesture #" + gui.highlightedMove, textOrigin.x, textOrigin.y);

    // Find best match
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
    text("speed: " + speed[whichcost], textOrigin.x, textOrigin.y + 80); 
  }
  
  private int getFramesBufferAnalyzed(Move move) {
    return move.framesGesture * 2;
  }
  
  private int getFramesMoveAnalyzed(Move move) {
    return move.framesGesture;
  }
}

