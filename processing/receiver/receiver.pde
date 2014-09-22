import oscP5.*;
import netP5.*;

Connection connection;

void setup() {
  size(400, 400);
  background(255);
  frameRate(25);
  connection = new Connection();
}


void draw() {
}


void keyPressed() {
  switch(key) {
    case('c'):
    connection.login();  
    break;
    case('d'):
    connection.logout();  
    break;
  }
}


