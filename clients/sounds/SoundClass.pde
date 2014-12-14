
class Sound {
  
  private static final int DEFAULT_SWITCH_TIME = 10;
  private static final int SETTER_INTERVAL = 5;
  
  public Gain out;
  
  private AudioContext ac;
  private SamplePlayer sp;
  
  private Glide rate;
  private Glide volume;
  
  private float volumeAVG = 0;
  private float rateAVG = 0;
  private int volumeCounter = 0;
  private int rateCounter = 0;
  
  public Sound(AudioContext ac, String filename) {
    try { 
      this.ac = ac;
      this.sp = new SamplePlayer(ac, new Sample(sketchPath("") + filename));
      this.sp.setKillOnEnd(false);
    } catch(Exception e) {
      println("Exception while attempting to load file : " + filename);
      e.printStackTrace();
      exit();
    }
    
    this.rate = new Glide(ac, 1, DEFAULT_SWITCH_TIME);
    sp.setRate(rate);
    
    this.volume = new Glide(ac, 0.5, DEFAULT_SWITCH_TIME);
    this.out = new Gain(ac, 1, volume);
    out.addInput(sp);
  }
  
  public void setRate(float newRate) {
    if(rateCounter == SETTER_INTERVAL) {
      this.rate.setValueImmediately(rateAVG);
      rateAVG = 0;
      rateCounter = 0;
    } else {
      rateAVG = rateCounter == 0 ? newRate : (rateAVG * rateCounter + newRate) / (rateCounter + 1);
    }
    rateCounter++;
  }
  
  public void setRate(float newRate, float time) {
    this.rate.setGlideTime(time);
    this.rate.setValue(newRate);
  }
  
  public void setVolume(float newVolume) {
    if(volumeCounter == SETTER_INTERVAL) {
      this.volume.setValueImmediately(volumeAVG);
      volumeAVG = 0;
      volumeCounter = 0;
    } else {
      volumeAVG = volumeCounter == 0 ? newVolume : (volumeAVG * volumeCounter + newVolume) / (volumeCounter + 1);
    }
    volumeCounter++;
  }
  
  public void setVolume(float newVolume, float time) {
    this.volume.setGlideTime(time);
    this.volume.setValue(newVolume);
  }
  
  
}
