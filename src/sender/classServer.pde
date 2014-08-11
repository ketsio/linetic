class Server {
  OscP5 oscP5;
  NetAddressList netAddressList = new NetAddressList();

  int listeningPort = 32000;
  int broadcastPort = 12000;

  String connectPattern = "/server/connect";
  String disconnectPattern = "/server/disconnect";

  public Server() {
    oscP5 = new OscP5(this, listeningPort);
  }

  public void send(String affect) {
    OscMessage m = new OscMessage("/affect");
    m.add(affect);
    oscP5.send(m, netAddressList);
  }
  
  public void send(Move move, int userId) {
    OscMessage m = new OscMessage("/move");
    // sending the move slug ?
    // m.add(move.slug);
    // m.add(userId);
    oscP5.send(m, netAddressList);
  }

  void oscEvent(OscMessage m) {
    if (m.addrPattern().equals(connectPattern))
      connect(m.netAddress().address());
    else if (m.addrPattern().equals(disconnectPattern))
      disconnect(m.netAddress().address());
  }

  private void connect(String ip) {
    if (!netAddressList.contains(ip, broadcastPort)) {
      netAddressList.add(new NetAddress(ip, broadcastPort));
      println("### adding "+ip+" to the list.");
    } else {
      println("### "+ip+" is already connected.");
    }
    println("### currently there are "+netAddressList.list().size()+" remote locations connected.");
  }

  private void disconnect(String ip) {
    if (netAddressList.contains(ip, broadcastPort)) {
      netAddressList.remove(ip, broadcastPort);
      println("### removing "+ip+" from the list.");
    } else {
      println("### "+ip+" is not connected.");
    }
    println("### currently there are "+netAddressList.list().size()+" remote locations connected.");
  }
}

