import beads.*;
import oscP5.*;
import netP5.*;
import java.util.Arrays; 

Client client;

void setup() {
  
  size(400, 400);
  client = new Client(this);
  client.login();
  // ...
  
}
void draw() {
  // ...
}

void keyPressed() {
  switch(key) {
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
