package _01_Intro_To_Sockets.server;

import java.net.*;
import java.net.SocketTimeoutException;

import javax.swing.JOptionPane;

import java.io.*;

public class ServerGreeter extends Thread {
	//1. Create an object of the ServerSocket class
    String ip = "192.168.56.1";
    int port = 8080;
	ServerSocket s;
	
	public ServerGreeter() throws IOException {
		//2. Initialize the ServerSocket object. In the parameters,
		//   you must define the port at which the server will listen for connections.
		 s = new ServerSocket(port);
		 s.setSoTimeout(40000);
		//*OPTIONAL* you can set a time limit for the server to wait by using the 
		//  ServerSocket's setSoTimeout(int timeInMilliSeconds) method
	}

	public void run() {
		//3. Create a boolean variable and initialize it to true.
		boolean b = true;
		//4. Make a while loop that continues looping as long as the boolean created in the previous step is true.
			while(b) {
			//5. Make a try-catch block that checks for two types Exceptions: SocketTimeoutException and IOException.
			//   Put steps 8 - 15 in the try block.
				try {
				//8. Let the user know that the server is waiting for a client to connect.
					JOptionPane.showMessageDialog(null, "The server is ready", "Message", JOptionPane.INFORMATION_MESSAGE);

				//9. Create an object of the Socket class and initialize it to serverSocket.accept();
				//   Change serverSocket to match the ServerSocket member variable you created in step 1.
				//   The program will wait her until either a client connects or the timeout expires.
					Socket s1 = s.accept();
				//10. Let the user know that the client has connected.
					JOptionPane.showMessageDialog(null, "You have connected", "Message", JOptionPane.INFORMATION_MESSAGE);

				//11. Create a DataInputStream object. When initializing it, use the Socket object you created in step 9 to call the getInputStream() method.
					DataInputStream DIS = new DataInputStream(s1.getInputStream());
				//12. Print the message from the DataInputStream object using the readUTF() method
				System.out.println(DIS.readUTF());
				//13. Create a DataOutputStream object. When initializing it, use the Server object you created in step 9 to call the getOutputStream() method.
				DataOutputStream DOS = new DataOutputStream(s1.getOutputStream());
				//14. Use the DataOutputStream object to send a message to the client using the writeUTF(String message) method.
				DOS.writeUTF("Hello");
				//15. Close the client server
				s1.close();
				}catch (SocketTimeoutException e) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "The server has timed out. \n Full reason: " + e, "Error", JOptionPane.ERROR_MESSAGE);

					b = false;
				}catch (IOException e) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "IO Exception Occured \n Full reason: " + e, "Error", JOptionPane.ERROR_MESSAGE);

					b = false;
				}
			//6. If the program catches a SockeTimeoutException, let the user know about it and set loop's boolean variable to false.

			//7. If the program catches a IOException, let the user know about it and set the loop's boolean variable to false.
			}
		
	}

	public static void main(String[] args) {
		//16. In a new thread, create an object of the ServerGreeter class and start the thread. Don't forget the try-catch.
		try {
			new ServerGreeter().run();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
