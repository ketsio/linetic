class User {
  private int id;
  public String name;
  public color c;
  public RingBuffer rb;
  public int warning;

  public User(int userId) {
    this.id = userId;
    String[] dwarfs = {
      "Doc", "Grumpy", "Happy", "Sleepy", "Bashful", "Sneezy", "Dopey"
    };
    this.name = dwarfs[int(random(dwarfs.length))];
    this.rb = new RingBuffer();
    this.c = color(random(50, 255), random(50, 255), random(0, 100));
    this.warning = -1;
  }

  void hello() {
    println("Hello " + name + " !");
  }

  void bye() {
    println("Bye " + name + ".");
  }

  void fillBuffer(Pose pose) {
    rb.fillBuffer(pose);
    rb.fillBufferNormalized(pose.normalizeRotation());
  }

  void record(Move move) {
    this.rb.record(move);
  }
  
  void stopRecording() {
    this.rb.stopRecording();
  }

  void updateWarning(PVector jointNeck2D, PVector jointNeck3D) {
    warning = -1; 

    if (jointNeck2D.x < 100) 
      warning = 0;

    if (jointNeck2D.x > 540) 
      warning = 4;

    if (jointNeck3D.z > 4000) {
      warning = 2;
      if (jointNeck2D.x < 100)
        warning = 1;
      if (jointNeck2D.x > 540)
        warning = 3;
    }

    if (jointNeck3D.z < 1500) {
      warning = 6;
      if (jointNeck2D.x < 100) 
        warning = 7;
      if (jointNeck2D.x > 540)
        warning = 5;
    }
  }

  void evaluateSkeleton(SimpleOpenNI kinect) {
    // capture and draw
    Pose pose = (new Pose()).capture(kinect, this);
    if (NORMALIZE_SIZE) pose.normalizeSize();

    // add to the buffer
    fillBuffer(pose);
  }
}

