
class Music {
  
  private AudioContext ac;
  
  private Glide volume;
  
  public Music(AudioContext ac) {
    this.ac = ac;
  }
  
  public void addSound(Sound sound) {
    ac.out.addInput(sound.out);
  }
  
  public void start() {
    ac.start();
  }
  
  public void play() {
    ac.out.pause(false);
  }
  
  public void pause() {
    ac.out.pause(true);
  }
  
}
