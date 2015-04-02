import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


public class Instrumenter {
	private String fileName = "";
	private String userid = "";
	public Instrumenter(String f, String userid) {
		this.fileName = f;
		this.userid = userid;
	}
	
	public void Instrument() throws IOException, InterruptedException {
		// STUB, INCLUDE CODE TO RUN THE .SH FILE OF SCIENTIST
		if (new File(RunServer.SCRIPT_NAME).exists()) {
		String[] command = {"/bin/bash", RunServer.SCRIPT_NAME, fileName};
	    ProcessBuilder p = new ProcessBuilder(command);
	    Process p2 = p.start();
	   
	    BufferedReader br = new BufferedReader(new InputStreamReader(p2.getInputStream()));
	    String line;
	    System.out.println("Output of running " + command + " is: ");
	    while ((line = br.readLine()) != null) {
	        System.out.println(line);
	    }
	 //   Thread.sleep(20000);
	    System.out.println("Just woke up");
	    p2.waitFor();
	    BufferedReader br2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
	    String line2;
	    System.out.println("Output of running " + command + " is: ");
	    while ((line2 = br2.readLine()) != null) {
	        System.out.println(line2);
	    }
	    p2.waitFor();
	   // p2.wait
		}
		
		// On finish, signal re-send to server
		String instrumentedFileName = "output/" + fileName;
		if (!new File(instrumentedFileName).exists()) {
			instrumentedFileName = fileName;
		}
				
		System.out.println("Instrumentation complete, now to send to server");
		Sender newSender = new Sender();
		newSender.sendToServer(instrumentedFileName, userid);
	}
}
