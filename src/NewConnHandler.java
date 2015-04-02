import java.io.File;


import org.java_websocket.client.WebSocketClient;



public class NewConnHandler implements Runnable {

	String f = "";
	int timeout = 0;
	private org.java_websocket.WebSocket mWebSocketServer;
	private WebSocketClient mWebSocketClient;
	private int noOfPackets = 0;
	private Receiver newReceiver;
	private Sender newSender;
	String userid;
	
	public NewConnHandler(String fileName, int timeOut, org.java_websocket.WebSocket socket, String userid) {
		this.f = fileName;
		this.timeout = timeOut;
		this.mWebSocketServer = socket;
		this.userid = userid;
	}
	public NewConnHandler(String fileName, int timeOut, WebSocketClient socket, String userid) {
		this.f = fileName;
		this.timeout = timeOut;
		this.mWebSocketClient = socket;
		this.userid = userid;
	}
	@Override
	public void run() {
		
		if (mWebSocketServer != null) {
			System.out.println("Server running receiver");
			newReceiver = new Receiver(f, mWebSocketServer, noOfPackets, userid);
			newReceiver.receive();
		} else if (mWebSocketClient != null) {
			System.out.println("Server running sender");
			newSender = new Sender();
			File file = new File(f);
			newSender.send(file, 9, mWebSocketClient, 128);
		}
					
	}
	public void setStats(String fileName, int timeOut, org.java_websocket.WebSocket socket) {
		System.out.println("stats set, server socket set to: " + socket);
		this.f = fileName;
		this.timeout = timeOut;
		this.mWebSocketServer = socket;
	}
	public void setStats(String fileName, int timeOut, WebSocketClient socket) {
		System.out.println("stats set, client socket set to: " + socket);
		this.f = fileName;
		this.timeout = timeOut;
		this.mWebSocketClient = socket;
	}
	public void setAck(int ack) {
		newSender.setAck(ack);
	}
	public void newPacket(byte[] packet) {
		newReceiver.newPacket(packet);
	}
	public void setNoOfPackets(int packets) {
		newReceiver.setNoOfPackets(packets);
	}
	public void setFinal(boolean close) {
		System.out.println("Set to final from Pinger Actor");
		if (newReceiver != null) {
			newReceiver.setFinal(close);
		}
		if (newSender != null) {
			newSender.setClose(close);
		}
	//	newReceiver.setFinal(close);
	}
}