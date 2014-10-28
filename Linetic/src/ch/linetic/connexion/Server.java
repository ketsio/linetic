package ch.linetic.connexion;

import netP5.NetAddress;
import netP5.NetAddressList;
import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;

public final class Server {
	private final OscP5 oscP5;
	private final NetAddressList netAddressList = new NetAddressList();

	private static final int listeningPort = 32000;
	private static final int broadcastPort = 12000;

	private static final String connectPattern = "/server/connect";
	private static final String disconnectPattern = "/server/disconnect";

	public Server(PApplet parrent) {
		oscP5 = new OscP5(parrent, listeningPort);
	}

	public void send(String analyzer, float result) {
		OscMessage m = new OscMessage("/analysis");
		m.add(analyzer);
		m.add(result);
		oscP5.send(m, netAddressList);
	}

	public void read(OscMessage m) {
		if (m.addrPattern().equals(connectPattern))
			connect(m.netAddress().address());
		else if (m.addrPattern().equals(disconnectPattern))
			disconnect(m.netAddress().address());
	}

	private void connect(String ip) {
		if (!netAddressList.contains(ip, broadcastPort)) {
			netAddressList.add(new NetAddress(ip, broadcastPort));
			System.out.println("### adding " + ip + " to the list.");
		} else {
			System.out.println("### " + ip + " is already connected.");
		}
	}

	private void disconnect(String ip) {
		if (netAddressList.contains(ip, broadcastPort)) {
			netAddressList.remove(ip, broadcastPort);
			System.out.println("### removing " + ip + " from the list.");
		} else {
			System.out.println("### " + ip + " is not connected.");
		}
	}
	
	@Override
	public String toString() {
		return "### currently there are "
				+ netAddressList.list().size()
				+ " remote locations connected.";
	}
}
