class Move {

  private Pose[] poses = new Pose[framesGestureMax];
  private int id;
  public boolean empty = true;
  public PImage image = null;
  public String name = null;
  public String slug = null;
  public float triggerAt = 0.7;

  // settings
  public float weightX = defaultWeightX;
  public float weightY = defaultWeightY;
  public float weightZ = defaultWeightZ;
  public float weightLeftOrRight = defaultWeightLeftOrRight;
  public boolean normRotation = defaultNormRotation;
  public int framesGesture = defaultFramesGesture;


  public Move(int moveId) {
    this.id = moveId;
    this.slug = slug();
    for (int i = 0; i < framesGestureMax; i++) poses[i] = new Pose();
  }

  private String slug() {
    return "move" + id;
  }

  public Pose get(int id) {
    if (id < 0 || id >= framesGestureMax)
      return null;
    return poses[id];
  }

  public void load() {
    File f = new File(dataPath(slug + ".data"));      
    if (!f.exists())
      return;

    data.load(dataPath(slug + ".data"));
    for (int i = 0; i < framesGestureMax; i++)
      poses[i] = data.readPose();

    empty = false;
    f = new File(dataPath(slug + ".png"));
    if (f.exists()) 
      image = loadImage(dataPath(slug + ".png"));
  }

  public void save() {
    data.beginSave();
    for (int i = 0; i < framesGestureMax; i++)
      data.add(poses[i]);
    data.endSave(dataPath(slug + ".data"));
  }
  
  public void capture() {
    highlightedUser.record(this);
    pg.save(dataPath(slug + ".png")); 
    save();
    image = loadImage(dataPath(slug + ".png"));
    empty = false;
  }
}

