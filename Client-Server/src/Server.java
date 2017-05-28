import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	private static Socket socket;
	
	public static void main(String args[]){
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(3001);
			while(true){
				socket = serverSocket.accept();
				InputStream inputStream = socket.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				
				String number = bufferedReader.readLine();
				System.out.println("Message received from the client : " +number);
				
				//Multiplying the number by 2 and forming the return message
                String returnMessage;
                try
                {
                    int numberInIntFormat = Integer.parseInt(number);
                    int returnValue = numberInIntFormat*2;
                    returnMessage = String.valueOf(returnValue) + "\n";
                }
                catch(NumberFormatException e)
                {
                    //Input was not a number. Sending proper message back to client.
                    returnMessage = "Please send a proper number\n";
                }
                
				OutputStream outputStream = socket.getOutputStream();
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
				BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
				bufferedWriter.write(returnMessage);
				System.out.println("Message sent to the client is: " + returnMessage);
				bufferedWriter.flush();//write any unwritten bytes in writer
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
