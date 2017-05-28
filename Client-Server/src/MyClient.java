import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class MyClient {
	Socket myclient;
	DataInputStream dataInputStream;
	
	public MyClient() {
		try {
			myclient = new Socket("127.0.0.1",3001);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}