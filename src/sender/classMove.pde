class Move {
  
  private Pose[] poses = new Pose[framesGestureMax];
  public boolean empty = true;
  public PImage image = null;
  public String name = null;
  public String slug = null;
  
  // settings
  public float weightX = defaultWeightX;
  public float weightY = defaultWeightY;
  public float weightZ = defaultWeightZ;
  public float weightLeftOrRight = defaultWeightLeftOrRight;
  public boolean normRotation = defaultNormRotation;
  public int framesGesture = defaultFramesGesture;
  
  
  public Move() {
    for (int i = 0; i < framesGestureMax; i++) poses[i] = new Pose();
  }
  
  public Pose get(int id) {
    if (id < 0 || id >= framesGestureMax)
      return null;
    return poses[id];
  }
}
