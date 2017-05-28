import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client{
	
	private static Socket socket;
	
	public static void main(String args[]){
		try {
			socket = new Socket("127.0.0.1",3001);
			
			OutputStream outputStream = socket.getOutputStream(); // contains text
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream); // converts text to bytestream
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			
			String number = "2";
			String sendMessage = number + "\n";
			bufferedWriter.write(sendMessage);
			bufferedWriter.flush();
			System.out.println("Message sent to the server : "+sendMessage);
			
			InputStream inputStream = socket.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			String message = bufferedReader.readLine();
			System.out.println("Message received from the server : " +message);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
