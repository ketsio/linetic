import oscP5.*;
import netP5.*;

Server server;

void setup() {
  size(400, 400);
  background(0);
  frameRate(25);
  server = new Server();
}


void draw() {
}


void mousePressed() {
  server.send("happiness");
}


