class Connection {
  OscP5 oscP5;
  NetAddress serverLocation; 

  int listeningPort = 12000;
  int serverPort = 32000;
  String serverAddress = "127.0.0.1";

  public Connection() {
    oscP5 = new OscP5(this, listeningPort);
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

  void oscEvent(OscMessage m) {
    background(255,0,0);
    println("### received an osc message with addrpattern "+m.addrPattern()+" and typetag "+m.typetag());
    m.print();
  }
}

