import beads.*;
import oscP5.*;
import netP5.*;
import java.util.Arrays; 

Client client;

AudioContext ac;
Envelope grainIntervalEnvelope;
Envelope rateEnvelope;

boolean playing = false;

void setup() {
  size(300,300);
  client = new Client(this);
  ac = new AudioContext();
  grainIntervalEnvelope = new Envelope(ac, 30);
  rateEnvelope = new Envelope(ac, 1);
  selectInput("Select an audio file:", "fileSelected");
}

/*
 * This code is used by the selectInput() method to get the filepath.
 */
void fileSelected(File selection) {
  String audioFileName = selection.getAbsolutePath();
  Sample sample = SampleManager.sample(audioFileName);
  GranularSamplePlayer player = new GranularSamplePlayer(ac, sample);
  
  // Loop the sample at its end points
  player.setLoopType(SamplePlayer.LoopType.LOOP_ALTERNATING);
  player.getLoopStartEnvelope().setValue(0);
  player.getLoopEndEnvelope().setValue((float)sample.getLength());
  
  // Control the rate of grain firing
  grainIntervalEnvelope.addSegment(20, 1);
  player.setGrainIntervalEnvelope(grainIntervalEnvelope);
  
  // Control the playback rate
  rateEnvelope.addSegment(0.8, 2000);
  player.setRateEnvelope(rateEnvelope);
  
  player.getRandomnessEnvelope().setValue(0.02);
  Gain g = new Gain(ac, 2, 0.2);
  g.addInput(player);
  ac.out.addInput(g);
  ac.start();
  ac.out.pause(!playing);
}

color fore = color(255, 102, 204);
color back = color(0,0,0);


void draw() {
  loadPixels();
  Arrays.fill(pixels, back);
  for(int i = 0; i < width; i++) {
    int buffIndex = i * ac.getBufferSize() / width;
    int vOffset = (int)((1 + ac.out.getValue(0, buffIndex)) * height / 2);
    vOffset = min(vOffset, height);
    pixels[vOffset * height + i] = fore;
  }
  updatePixels();
  
  /*
  float ratePercent = (float) mouseX / (float) width;
  float x = (ratePercent * 1.1 - 0.1);
  
  float grainPercent = (float) mouseY / (float) height;
  int y = (int) (grainPercent * 200.0);
  
  float notUnicityPercent = (float) mouseX / (float) width;
  float x = rateFunction(notUnicityPercent);
  int y = grainFunction(notUnicityPercent);
  
  rateEnvelope.addSegment(x, 1);
  grainIntervalEnvelope.addSegment(y, 1);
  */
  
}

float rateFunction(float percent) {
  if (percent <= 0.3) {
    return - percent + 0.8;
  }
  else if (percent <= 0.65) {
    return - 0.2857 * percent + 0.5857;
  }
  else {
    return - 1.42857 * percent + 1.32857;
  }
}

int grainFunction(float percent) {
  if (percent <= 0.3) {
    return 20;
  }
  else {
    return (int) (185.7143 * percent - 35.7143);
  }
}

void keyPressed() {
  switch(key) {
    case(' '):
      ac.out.pause(playing);
      playing = !playing;
      break;
    case('c'):
      client.login();
      break;
    case('d'):
      client.logout();
      break;
  }  
}

void oscEvent(OscMessage m) {
  client.read(m);
}
