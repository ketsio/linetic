
class Client {
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
    if(name.equals("multiplicity")) {
        float multiplicityPercent = value / 100.0;
        float x = rateFunction(multiplicityPercent);
        float y = grainFunction(multiplicityPercent);
        float z = pitchFunction(multiplicityPercent);
        
        rateEnvelope.addSegment(x, 1);
        grainIntervalEnvelope.addSegment(y, 1);
        pitchEnvelope.addSegment(z, 1);
        println("multiplicity : " + multiplicityPercent);
    }
  }
  
}

