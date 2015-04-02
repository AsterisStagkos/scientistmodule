import java.io.IOException;



public class NewInstrumentationHandler implements Runnable {

		String fileName = "";
		String userid = "";
		public NewInstrumentationHandler(String fileName, String userid) {
			this.fileName = fileName;
			this.userid = userid;
		}

		@Override
		public void run() {
			Instrumenter i = new Instrumenter(fileName, userid);		
			try {
				i.Instrument();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
}
