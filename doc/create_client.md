
Create your own Client
======================

Let's analyze the minimal code to create a client. Note that the minimal code is available in [/clients/empty_example][Empty Example].

*empty_example.pde*
```java
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
```

*ClientClass.pde*
```java
public class Client {
  OscP5 oscP5;
  NetAddress serverLocation; 

  int listeningPort = 12000;
  int serverPort = 32000;
  String serverAddress = "127.0.0.1";

  public Client(PApplet parrent) {
    oscP5 = new OscP5(parrent, listeningPort);
    serverLocation = new NetAddress(serverAddress, serverPort);
  }
  
  public void login() {
    OscMessage m = new OscMessage("/server/connect", new Object[0]);
    oscP5.flush(m, serverLocation);  
  }
  
  public void logout() {
    OscMessage m = new OscMessage("/server/disconnect", new Object[0]);
    oscP5.flush(m, serverLocation);
  }

  public void read(OscMessage m) {
    if (m.addrPattern().equals("/analysis") && m.typetag().equals("sf")) {
      handleAnalyzer(m.get(0).stringValue(), m.get(1).floatValue());
    }
  }
  
  public void handleAnalyzer(String name, float value) {
    if(name.equals("speed")) {
        println("Speed : " + value);
        // ...
    }
    if(name.equals("hands_proximity")) {
        println("Hands Proximity : " + value);
        // ...
    }
    // ...
  }
  
}
``` 

To understand how the class `Client` works please [read the client-server documentation][Client/Server Architecture].

The use of the data sent by the server (Linetic) is concentrated in the function `handleAnalyzer` that takes the slug (identifier) of the `Analyzer` and its final result as arguments and do something with the data.

Therefore you can create the processing application of your choice and use the results of the Analyzer to trigger anything in your client.

Concrete example
----------------

**For a concrete example, have a look at the client [music_consciousness][Music Consciousness].**
It plays the music of your choice (using the beads library - Make sure your import this library before running this client) and uses the multiplicity analyzer to modify the rate and the grain of the song.

>**Multiplicity Analyzer**  : The more parts of your body you move the bigger the result will be. You can keep this analyzer close to 0% if you move step by step, member by member without being brutal in your moves.

The bigger the value of the Multiplicity Analyzer is, the bigger the deformation of the music will be. That way it forces the user to keep it multiplicity as low as possible and move step by step therefore being more aware of its own movements.

[Empty Example]: /clients/empty_example
[Client/Server Architecture]: /doc/server_client.md
[Music Consciousness]: /clients/music_consciousness
