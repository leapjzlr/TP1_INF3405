import java.io.DataInputStream;
import java.net.Socket;
import java.util.Scanner; //classe pour avoir le input de l'utilisateur

public class Client {
	private static Socket socket;
	
	
	
	public static void main(String[] args) throws Exception
	{
		Scanner IPAddressInput = new Scanner(System.in);
		System.out.println("Enter server IP Address");
		String IPAddress = IPAddressInput.nextLine();
		String[] IPAddressSplit = IPAddress.split(".");
		
		/*while(IPAddressSplit.length != 4) {
			IPAddressInput = new Scanner(System.in);
			System.out.println("Enter server IP Address. Ip Adress must be 4 bytes");
			IPAddress = IPAddressInput.nextLine();
			IPAddressSplit = IPAddress.split(".");
		}*/
		
		boolean validIP=false;
		
		while(!validIP) { //répétition de code (voir ligne 15)
			if(IPAddressSplit.length == 4) {
				for(int i = 0; i<IPAddressSplit.length; i++) {
					/*if(!IPAddressSplit[i].isDigit()) {}*/
					Integer IPPart = Integer.parseInt(IPAddressSplit[i]);
					if(IPPart < 255 || IPPart > 0) {
						validIP=true;
					}
				}

			}
			if(!validIP) {
				IPAddressInput = new Scanner(System.in);
				System.out.println("Enter server IP Address. Ip Adress numbers must be between 0 and 255 and of 4 bytes");
				IPAddress = IPAddressInput.nextLine();
				IPAddressSplit = IPAddress.split(".");
			}
		}
		
		Scanner portInput = new Scanner(System.in);
		System.out.println("Enter server port");
		String portStr = portInput.nextLine();
		int port = Integer.parseInt(portStr);
		
		while(port > 5050 || port < 5000) {
			portInput = new Scanner(System.in);
			System.out.println("Enter server port. Port must be  between 5000 and 5050");
			portStr = portInput.nextLine();
			port = Integer.parseInt(portStr);
		}
		
		String serverAddress = "127.0.0.1";
		int port = 5000;
		
		socket = new Socket(serverAddress, port);
		
		System.out.format("The server is running on %s:%d%n", serverAddress, port);
		
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		String message = in.readUTF();
		System.out.println(message);
		
		socket.close();
	}
}
