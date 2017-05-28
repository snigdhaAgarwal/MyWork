import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class MyServer {
	ServerSocket myserver;
	Socket clientSocket;
	
	MyServer(){
		try {
			myserver = new ServerSocket(3001);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(true){
			try {
				clientSocket = myserver.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
