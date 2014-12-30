import beads.*;
import oscP5.*;
import netP5.*;
import java.util.Arrays; 

Client client;

AudioContext ac;
Envelope grainIntervalEnvelope;
Envelope rateEnvelope;
Envelope randomnessEnvelope;
Envelope pitchEnvelope;

boolean playing = false;

void setup() {
  size(300,300);
  client = new Client(this);
  client.login();
  ac = new AudioContext();
  grainIntervalEnvelope = new Envelope(ac, 20);
  rateEnvelope = new Envelope(ac, 1);
  randomnessEnvelope = new Envelope(ac, 0.02);
  pitchEnvelope = new Envelope(ac, 1);
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
  player.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);
  player.getLoopStartEnvelope().setValue(0);
  player.getLoopEndEnvelope().setValue((float)sample.getLength());
  
  // Control the rate of grain firing
  player.setGrainInterval(grainIntervalEnvelope);
  
  // Control the playback rate
  rateEnvelope.addSegment(0.8, 2000);
  player.setRate(rateEnvelope);
  
  // Control the randomness of the song
  player.setRandomness(randomnessEnvelope);
  
  // Control the pitch of the song
  player.setPitch(pitchEnvelope);
  
  Gain g = new Gain(ac, 2, 0.5);
  g.addInput(player);
  ac.out.addInput(g);
  ac.start();
  ac.out.pause(!playing);
}

color fore = color(55, 162, 217);
color back = color(255);


void draw() {
  loadPixels();
  Arrays.fill(pixels, back);
  for(int i = 0; i < width; i++) {
    int buffIndex = i * ac.getBufferSize() / width;
    int vOffset = (int)((1 + ac.out.getValue(0, buffIndex)) * height / 2);
    vOffset = min(vOffset, height - 1);
    vOffset = max(vOffset, 0);
    pixels[vOffset * width + i] = fore;
  }
  updatePixels();
  
  if (!playing) {
    fill(50);
    textSize(14);
    textAlign(CENTER);
    text("press space to play", 0, height - 20, width, 20);
  }
  
  // Uncomment to try the client without having to use linetic
  tryFunctionsWithMouse();
  
}

/* Use it in the draw method to analyze how the song
   is being modified, x for the rate and y for the grain. */
void modifySongWithMouseXY() {
  float ratePercent = (float) mouseX / (float) width;
  float x = (ratePercent * 1.1 - 0.1);
  
  float grainPercent = (float) mouseY / (float) height;
  int y = (int) (grainPercent * 200.0);
  
  rateEnvelope.addSegment(x, 1);
  grainIntervalEnvelope.addSegment(y, 1);
}

/* Use it to test your functions rateFunction and grainFunction
   quickly with the mouse instead of having to use the kinect */
void tryFunctionsWithMouse() {
  float multiplicityPercent = (float) mouseX / (float) width;
  float x = rateFunction(multiplicityPercent);
  float y = grainFunction(multiplicityPercent);
  float z = pitchFunction(multiplicityPercent);
  
  rateEnvelope.addSegment(x, 1);
  grainIntervalEnvelope.addSegment(y, 1);
  pitchEnvelope.addSegment(z, 1);
}

/* The function that, given a percent of multiplicity, 
   returns the new rate of the song */
float rateFunction(float percent) {
  if (percent <= 0.3) {
    return - percent + 0.8;
  } else if (percent <= 0.65) {
    return - 0.2857 * percent + 0.5857;
  } else {
    return - 1.42857 * percent + 1.32857;
  }
}

/* The function that, given a percent of multiplicity, 
   returns the new grain of the song */
float grainFunction(float percent) {
  if (percent <= 0.3) {
    return 20;
  } else {
    return 185.7143 * percent - 35.7143;
  }
}

/* The function that, given a percent of multiplicity, 
   returns the new pitch of the song */
float pitchFunction(float percent) {
  float a = 0.12;
  float b = 1.0;
  float alpha = 0.15;
  
  if (percent <= a) {
    return 1.0;
  } else if (percent <= b) {
    return 1.0 + (alpha / (b-a)) * (percent - a);
  } else {
    return 1.0 + alpha;
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
