import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.TreeMap;


public class Receiver {
	private String fileName;
	private String userid;
	private org.java_websocket.WebSocket mWebSocketServer;
	public static int fileSizeStatic;
	public static byte[] fileBytesStatic;
	private byte[] nextPacket;
	private int noOfPackets = 100;
	private boolean packetsSet = false;
	private boolean shouldFinish = false;
	private String filePath;
	private boolean startDownload = false;
	private TreeMap<Integer, byte[]> packets = new TreeMap<Integer, byte[]>(); 
	
//	public websockets(WebSocketClient mWebSocketClient) {
//		this.mWebSocketClient = mWebSocketClient;
//	}
	public void setFinal(boolean isFinal) {
		shouldFinish = isFinal;
	}
	public void setFilePath(String filepath) {
		filePath = filepath;
	}
	public void setNoOfPackets(int number) {
		noOfPackets = number;
		packetsSet = true;
		if (number == 0) {
			shouldFinish = true;
			System.out.println("Zero size packet received");
		}
	}
	public void setStartDownload(boolean shouldStart) {
		startDownload = shouldStart;
	}
	public void newPacket(byte[] packet) {
		 ByteBuffer byteBuffer = ByteBuffer.wrap(packet, 0, 7);
         int packetNo = (int) byteBuffer.getInt(0);
        
         if (!packets.containsKey(packetNo)) {
    //   	  System.out.println("receiver thread " + "inserted packet: " + packetNo + " into packets[]");
       	  packets.put(packetNo, packet);
       	  if (packetNo == 0) {
       		  startDownload = true;
       	  }
         }

     }
	

	
	public Receiver(String fileName, org.java_websocket.WebSocket socket, int noOfPackets, String userid) {
		this.fileName = fileName;
		this.mWebSocketServer = socket;
		this.noOfPackets = noOfPackets;
		this.userid = userid;
	}
	
	 public void receive() {
	        int expectedSeqNumber = 0;
	        try {
	        //	websockets.nextPacket = new byte[1029];
	       // 	Thread.sleep(1500);
	            // Array b will hold the packet received. Has a header of size 3 to indicate
	            // the packet number and whether it is the last packet or not
	            // Array ack will hold the acknowledgement that the receiver will send back to the sender
	            // for each packet it receives
	            byte[] b = new byte[1031];
	   //         byte[] ack = new byte[4];
	         //   DatagramSocket udpSocket = new DatagramSocket(port);
	            FileOutputStream f = new FileOutputStream(fileName.trim());
	            ByteBuffer byteBuffer;
	     //       ByteBuffer toSendByteBuffer = ByteBuffer.wrap(ack);
	            while (!packetsSet) {
	            	Thread.sleep(25);
	            }
	            while (!shouldFinish) {
	            	Thread.sleep(25);
	            	
	            	if (noOfPackets == 0) {
	            		System.out.println("error detected " + "closing thread");
	                	packetsSet = false;
	                	break;
	                }
	                // Receive a packet and store it in array b
//	                DatagramPacket packet = new DatagramPacket(b, b.length);
//	                udpSocket.receive(packet);
	            	int packetNo = 0;
	            	int packetSize = 0;
	            	if (packets.containsKey(expectedSeqNumber) && startDownload) {
	            		try {
	            		b = packets.get(expectedSeqNumber).clone();
	            		 // Use a byte buffer to decipher the packet number and packet size
		                byteBuffer = ByteBuffer.wrap(b, 0, 7);
		                packetNo = (int) byteBuffer.getInt(0);
		             //   Log.d("received packet", packetNo + "");
		                
		                packetSize = (int) byteBuffer.getShort(5);
		               	 
	                    f.write(b, 7, packetSize);
	             //       packets.remove(packetNo);
		                    mWebSocketServer.send(("ack " + expectedSeqNumber).getBytes());
	                    expectedSeqNumber++;
	            		} catch (NullPointerException e) {
	            			e.printStackTrace();
	            		}
	                    while (packets.containsKey(expectedSeqNumber)) {
	                    	try {
	                    	b = packets.get(expectedSeqNumber).clone();
	                    	byteBuffer = ByteBuffer.wrap(b, 0, 7);
	 		                packetNo = (int) byteBuffer.getInt(0);
	 		    
			                
			                packetSize = (int) byteBuffer.getShort(5);
			         //       System.out.println("Packet Number written to file: "+packetNo + " ack sent: " + expectedSeqNumber);
			               	 
		                    f.write(b, 7, packetSize);
		                    if (expectedSeqNumber > 0) {
		                    	 mWebSocketServer.send(("ack " + expectedSeqNumber).getBytes());
		                    }                   
		                    expectedSeqNumber++;
	                    	
	                    
	                    }catch (NullPointerException e) {
	            			e.printStackTrace();
	            		}
	                    }
	            	} else {
	            		int ackToSend = expectedSeqNumber-1;
	            		if (ackToSend < 0) {
	            			ackToSend = 0;
	            		}
	            	//	System.out.println("receiver " + "not found packet: " + expectedSeqNumber + " sent ack: " + ackToSend);
		            		mWebSocketServer.send(("ack " + (ackToSend)).getBytes());
	            	}
	               

	                if (b[4] == 1) {
	                	startDownload = false;
	                	packetsSet = false;
	                	mWebSocketServer.send("ack final".getBytes());
	                	packets.clear();
	                	System.out.println("Download complete, now to signal instrumentation");
	                	NewInstrumentationHandler newInstr = new NewInstrumentationHandler(fileName, userid);
	                	new Thread(newInstr).start();
	                    break;
	                }
	                
	            }
	        //    udpSocket.close();
	            f.close();
	        } catch (Exception e) {
	            System.out.println("Exception: " + e);
	            e.printStackTrace();
	        }
	    }
}
