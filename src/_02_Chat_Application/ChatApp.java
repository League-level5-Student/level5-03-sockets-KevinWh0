package _02_Chat_Application;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import _02_Chat_Application.State.ScreenState;

/*
 * Using the Click_Chat example, write an application that allows a server computer to chat with a client computer.
 */

public class ChatApp {

	String ip = "192.168.56.1";
	int port = 8080;
	boolean s = true;
	ServerSocket serverSocket;
	Socket socket;
	DataOutputStream DOP;
	DataInputStream DIP;

	String addToLocalScreen = null;
	
	JFrame frame = new JFrame();
	JPanel panel = new JPanel();
	JButton client = new JButton("Join a server");
	JButton server = new JButton("Start a server");

	ArrayList<String> Chat = new ArrayList<String>();

	JTextArea textArea = new JTextArea("");

	JLabel chatLog = new JLabel("");

	JScrollPane ChatTextArea = new JScrollPane(chatLog);

	JScrollPane areaScrollPane = new JScrollPane(textArea);

	ScreenState state = ScreenState.Options;

	void showOptions() {
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.add(server);
		panel.add(client);

		server.addActionListener((e) -> {
			System.out.println("Starting Server");
			panel.remove(server);
			panel.remove(client);
			state = ScreenState.Hosting;
			try {
				serverSocket = new ServerSocket(port);
				serverSocket.setSoTimeout(40000);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			runChatApp();
		});
		client.addActionListener((e) -> {
			System.out.println("Attempting to join a Server");
			panel.remove(server);
			panel.remove(client);
			state = ScreenState.Joining;
			try {
				socket = new Socket(ip, port);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			runChatApp();
		});

		frame.setVisible(true);
		frame.pack();

	}

	void runChatApp() {
		frame.setPreferredSize(new Dimension(800, 800));
		frame.add(panel);

		chatLog.setPreferredSize(new Dimension(800 - 20, 700 - 40));

		// ChatTextArea.setPreferredSize(new Dimension(800 - 20, 700 - 40));
		// ChatTextArea.add(chatLog);
		areaScrollPane.setPreferredSize(new Dimension(800 - 20, 40));
		textArea.setBounds(10, 800 - 50, 800 - 20, 40);
		panel.add(chatLog);
		panel.add(areaScrollPane);
		frame.pack();
		textArea.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// System.out.println("Pressed " + (int) e.getKeyChar());
				if (e.getKeyCode() == 10) {
					//System.out.println(Chat.size());
					addToLocalScreen = textArea.getText();
					Chat.add("You: " + addToLocalScreen);
					//System.out.println(Chat.size());

					try {
				        InetAddress inetAddress = InetAddress.getLocalHost();
						DOP.writeUTF(inetAddress.getHostAddress() + " : " + addToLocalScreen);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					String str = "<html>";
					for (int i = 0; i < Chat.size(); i++) {
						str = str + Chat.get(i) + "<br/>";
					}
					str = str + "<html/>";

					chatLog.setText(str + "    ");
					
					textArea.setText("");

				} else if (e.getKeyChar() == 27) {
					try {
						socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					System.exit(0);
				}
			}
		});

		try {
			if (state == ScreenState.Hosting) {
				socket = serverSocket.accept();
			}

			DOP = new DataOutputStream(socket.getOutputStream());
			DIP = new DataInputStream(socket.getInputStream());

		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		
		Thread t = new Thread(() -> {
			while (s) {
				

				try {
//					String[] str = DIP.readUTF().split("~/");
//					System.err.println(DIP.readUTF() + "    " + str.length);
//					Chat.add("User #" + str[0] + " : " + str[1]);
					Chat.add("User # " + String.valueOf(DIP.readUTF()));

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					try {
						Chat.add("User #" + DIP.readUTF());

					} catch (Exception e2) {
						// TODO: handle exception
						e1.printStackTrace();
						e2.printStackTrace();

					}
				}

				
				String str = "<html>";
				for (int i = 0; i < Chat.size(); i++) {
					str = str + Chat.get(i) + "<br/>";
				}
				str = str + "<html/>";

				chatLog.setText(str + "    ");
			}
			
		});
		t.start();

	}
	

	public static void main(String[] args) {
		new ChatApp().showOptions();
	}
}
