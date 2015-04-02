import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;


public class RunServer {
	public static String SCRIPT_NAME = "";
	public static void main(String[] args) throws IOException {
		int port = 8080;
		String scriptName = "";
		
		String ScientistFirstName = "";
		String ScientistLastName = "";
		String ExperimentName = "";
		String ExperimentDescription = "";
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		if (args.length > 1) {
			scriptName = args[1];
		}
		if (args.length > 2) {
			ScientistFirstName = args[2];
		}
		if (args.length > 3) {
			ScientistLastName = args[3];
		}
		if (args.length > 4) {
			ExperimentName = args[4];
		}
		if (args.length > 5) {
			for (int i = 5; i < args.length; i++) {
				ExperimentDescription += args[i] + " ";
			}
			
		}
		SCRIPT_NAME = scriptName;
		InetSocketAddress serverAddress = new InetSocketAddress(port);
		System.out.println("serverAddress: " + getAddress() + " port: " + serverAddress.getPort());
		WebSocket server = new WebSocket(serverAddress);
		String externalIPAddress = getAddress();
		System.out.println(server.getAddress());
		server.start();
		Sender sender = new Sender();
		System.out.println("Sent to server: " + "ScientistInfo " + externalIPAddress + ":" + serverAddress.getPort() + " " + ScientistFirstName + " " + ScientistLastName + " " + ExperimentName + " " + scriptName + " " + ExperimentDescription);
		sender.sendToServer("ScientistInfo " + externalIPAddress + ":" + serverAddress.getPort() + " " + ScientistFirstName + " " + ScientistLastName + " " + ExperimentName + " " + scriptName + " " + ExperimentDescription);
	}

private static String getAddress() throws IOException {
	URL whatismyip = new URL("http://checkip.amazonaws.com");
	BufferedReader in = new BufferedReader(new InputStreamReader(
	                whatismyip.openStream()));

	String ip = in.readLine(); //you get the IP as a String
	return ip;
}
}