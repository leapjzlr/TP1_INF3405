import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner; //classe pour avoir le input de l'utilisateur

public class Client {
	private static Socket socket;
	
	
	public static boolean verifyLength(String[] IPAddressSplit) {
		if(IPAddressSplit.length ==4) {
			return true;
		}
		return false;
	}
	
	public static boolean verifyRange(String[] IPAddressSplit) {
		for(int i = 0; i<IPAddressSplit.length; i++) {
			if(!Character.isDigit(IPAddressSplit[i].charAt(0))) {
				return false;
			}
			Integer IPPart = Integer.parseInt(IPAddressSplit[i]);
			if(IPPart > 255 || IPPart < 0) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean verifyPort(int port) {
		if(port > 5050 || port < 5000) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) throws Exception
	{
		Scanner IPAddressInput = new Scanner(System.in);
		System.out.println("Enter server IP Address");
		String IPAddress = IPAddressInput.nextLine();
		String[] IPAddressSplit = IPAddress.split("\\.");
		
		boolean validIP=false;
		
		while(!verifyLength(IPAddressSplit) && verifyRange(IPAddressSplit)) {
			IPAddressInput = new Scanner(System.in);
			System.out.println("Enter server IP Address. Ip Adress numbers must be between 0 and 255 and of 4 bytes");
			IPAddress = IPAddressInput.nextLine();
			IPAddressSplit = IPAddress.split("\\.");
		}
		
		Scanner portInput = new Scanner(System.in);
		System.out.println("Enter server port");
		String portStr = portInput.nextLine();
		int port = Integer.parseInt(portStr);
		
		while(verifyPort(port)) {
			portInput = new Scanner(System.in);
			System.out.println("Enter server port. Port must be  between 5000 and 5050");
			portStr = portInput.nextLine();
			port = Integer.parseInt(portStr);
		}
		
		
		//String serverAddress = "127.0.0.1"; //utiliser adresse ip v4 pour en avoir 2 (?) --ipconfig pour la get
		
		socket = new Socket(IPAddress, port);
		
		System.out.format("The server is running on %s:%d%n", IPAddress, port);
		
		Scanner username = new Scanner(System.in);
		System.out.println("Enter username");
		String usernameStr = username.nextLine();
		
		Scanner password = new Scanner(System.in);
		System.out.println("Enter password");
		String passwordStr = password.nextLine();
		
		PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		output.println(usernameStr);
		output.println(passwordStr);
		output.flush();
		
		try {
			BufferedReader response = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			response.readLine();
		}
		catch(IOException e){
			System.out.print("Erreur dans la saisie du mot de passe");
		}

		
		socket.close();
	}
}
