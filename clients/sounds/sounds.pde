import beads.*;
import oscP5.*;
import netP5.*;
import java.util.Arrays; 

Client client;

AudioContext ac;
Music music;
Sound takeOnMe;

boolean playing = false;


void setup() {
  
  size(800, 600);
  
  client = new Client(this);
  
  ac = new AudioContext();
  music = new Music(ac);
  takeOnMe = new Sound(ac, "Take On Me.wav");
  music.addSound(takeOnMe);
  
  music.start();
  music.pause();
}

color fore = color(255, 102, 204);
color back = color(0,0,0);

void draw() {
  loadPixels();
  //set the background
  Arrays.fill(pixels, back);
  //scan across the pixels
  for(int i = 0; i < width; i++) {
    //for each pixel work out where in the current audio buffer we are
    int buffIndex = i * ac.getBufferSize() / width;
    //then work out the pixel height of the audio data at that point
    int vOffset = (int)((1 + ac.out.getValue(0, buffIndex)) * height / 2);
    //draw into Processing's convenient 1-D array of pixels
    vOffset = min(vOffset, height);
    pixels[vOffset * height + i] = fore;
  }
  updatePixels();
}

void mousePressed() {
  takeOnMe.setRate(((float) mouseX / (float) width) + 0.5);
}

void keyPressed() {
  switch(key) {
    case(' '):
      if(playing) {
        music.pause();
        playing = false;
      } else {
        music.play();
        playing = true;
      }
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
