

import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.StringTokenizer;
import java.util.TreeMap;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.server.WebSocketServer;


public class WebSocket extends WebSocketServer {
	public WebSocket(InetSocketAddress address) {
		super(address);
		// TODO Auto-generated constructor stub
	}
	private org.java_websocket.WebSocket mWebSocketServer;
	private NewConnHandler commsThread;
	private String appName = "tempHolder";
	public int fileSizeStatic;
	public byte[] fileBytesStatic;
	private String userid;
	private boolean isOpen;

//	public websockets(WebSocketClient mWebSocketClient) {
//		this.mWebSocketClient = mWebSocketClient;
//	}
	

	@Override
	public void onClose(org.java_websocket.WebSocket arg0, int arg1,
			String arg2, boolean arg3) {
		 commsThread.setFinal(true);
	     System.out.println("Websocket " + "Closed " + arg2);
		
	}

	@Override
	public void onError(org.java_websocket.WebSocket arg0, Exception arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(org.java_websocket.WebSocket arg0, String arg1) {
		System.out.println("received message " + arg1);
		
	}
	
	@Override
	public void onMessage(org.java_websocket.WebSocket arg0, ByteBuffer bytes) {
		byte[] messageReceived = new byte[bytes.capacity()];
	    bytes.get(messageReceived, 0, bytes.capacity());
	      String message = new String(messageReceived);
	   //   System.out.println("Message received: " + message);
	   //   Log.d("received byte message", message);
	      StringTokenizer messageTok = new StringTokenizer(message);
	      String firstToken = "";
	      if (messageTok.hasMoreTokens()) {
	    	  firstToken = messageTok.nextToken();
	      }
	      if (firstToken.equals("receive")) {
	    	  System.out.println("received byte message " + message);
	    	  commsThread = new NewConnHandler(appName, 1000,
						mWebSocketServer, userid);
			  new Thread(commsThread).start();
	      } else if (firstToken.equals("appname")) {
	    	  appName = message.substring(7);
	    	  appName = appName.trim();
	      } else if (firstToken.equals("noOfPackets")) {
	    	  if (messageTok.hasMoreTokens()) {
	    		  commsThread.setNoOfPackets(Integer.parseInt(messageTok.nextToken()));
	    		  
	    		//  System.out.println("noOfPackets" +" set to: " + noOfPackets);
	    	  }
	    	//  noOfPackets = Integer.parseInt(messageTok.nextToken());
	      } else if (firstToken.equals("User")) {
	    	  if (messageTok.hasMoreTokens()) {
	    		  userid = messageTok.nextToken();
	    	  }
	      } else {
	    	//  Log.d("received message", message);
	    	  commsThread.newPacket(messageReceived);
	    	  
	      }
		
	}

	@Override
	public void onOpen(org.java_websocket.WebSocket arg0, ClientHandshake arg1) {
		System.out.println("Websocket Opened");
	      isOpen = true;
	      mWebSocketServer = arg0;
		
	}

}
