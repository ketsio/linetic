class User {
  private int id;
  public String name;
  public color c;
  public RingBuffer rb;

  public User(int userId) {
    this.id = userId;
    String[] dwarfs = {
      "Doc", "Grumpy", "Happy", "Sleepy", "Bashful", "Sneezy", "Dopey"
    };
    this.name = dwarfs[int(random(dwarfs.length))];
    this.rb = new RingBuffer();
    this.c = color(random(255), random(255), random(255));
  }

  void hello() {
    println("Hello " + name + " !");
  }

  void fillBuffer(Pose pose) {
    rb.fillBuffer(pose);
    rb.fillBufferNormalized(pose.normalizeRotation());
  }
  
  void saveMyMove(Move move) {
    this.rb.copyBuffer(move);
  }
}

