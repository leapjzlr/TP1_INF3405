import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static ServerSocket listener;
	//private static Socket client;
	
	public static void main(String[] args) throws Exception
	{
		
		String serverAddress = "127.0.0.1";
		int serverPort = 5000;
		
		listener = new ServerSocket();
		listener.setReuseAddress(true);
		InetAddress serverIP = InetAddress.getByName(serverAddress);
		
		listener.bind(new InetSocketAddress(serverIP, serverPort));
		
		System.out.format("The server is running on %s:%d%n", serverAddress, serverPort);
		
		//client = listener.accept();
		
		try {
			
			while(true) {
				verifyLog();
				//new ClientHandler(listener.accept(), clientNumber++).start();
			}
		}
		catch(Exception e) {
			System.out.print("connection refusée");
		}
		finally {
			listener.close();
		}
	}
	private static class ClientHandler extends Thread
	{
		private Socket socket;
		private int clientNumber;
		
		public ClientHandler(Socket socket, int clientNumber) {
			this.socket = socket;
			this.clientNumber = clientNumber;
			System.out.println("New connection with client#" + clientNumber + "at" + socket);
		}
		
		
		
		public void run() {
			try {
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				out.writeUTF("Hello, you are client#" + clientNumber);
			} catch(IOException e) {
				System.out.println("Error handling client#" + clientNumber + ":" + e);
			}
			finally {
				try {
					socket.close();
				} catch(IOException e) {
				System.out.println("couldn't close socket");
				}
			}
			System.out.println("Connection with client#" + clientNumber + "closed");
		}
	}
	
	private static void verifyLog() throws Exception{
		int clientNumber = 0;
		
		BufferedReader input = new BufferedReader(new InputStreamReader(listener.accept().getInputStream()));
		String username = input.readLine();
		String password = input.readLine();

		FileInputStream reader = new FileInputStream("database.txt");
		DataInputStream in = new DataInputStream(reader);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;

		Boolean found = false;
		while((line = br.readLine()) != null) {
			String[] tokens = line.split(" ");

			if (username.equals(tokens[0]) && password.equals(tokens[1])) {
				found = true;
				new ClientHandler(listener.accept(), clientNumber++).start();
			}
			else if(username.equals(tokens[0]) && !password.equals(tokens[1])) {
				found = true;
				System.out.print(tokens[0] + ' ' + tokens[1]);
				listener.close();	
			}
		}
		
		if(!found) {
			FileWriter myWriter = new FileWriter("database.txt", true);
			myWriter.write(username + " " + password +"\n");
			myWriter.close();
			new ClientHandler(listener.accept(), clientNumber++).start();
		}
				
		
	}
}


