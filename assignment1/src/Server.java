
import java.io.*;
import java.net.*;

public class Server {

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendSocket, receiveSocket;

	public Server()
	{
		try {
			// Construct a datagram socket and bind it to any available 
			// port on the local host machine. This socket will be used to
			// send UDP Datagram packets.
			sendSocket = new DatagramSocket();

			// Construct a datagram socket and bind it to port 5000 
			// on the local host machine. This socket will be used to
			// receive UDP Datagram packets.
			receiveSocket = new DatagramSocket(69);

			// to test socket timeout (2 seconds)
			//receiveSocket.setSoTimeout(2000);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		} 
	}

	public void receiveAndEcho()
	{
		// Construct a DatagramPacket for receiving packets up 
		// to 100 bytes long (the length of the byte array).

		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);
		System.out.println("Server: Waiting for Packet.\n");

		// Block until a datagram packet is received from receiveSocket.
		try {        
			System.out.println("Waiting...");
			receiveSocket.receive(receivePacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Process the received datagram.
		System.out.println("Server: Packet received:");
		int len = receivePacket.getLength();
		
		// Form a String from the byte array.
		String received = new String(data,0,len);
		System.out.println(received + "\n");

		if(this.checkValidity(data, len)) {
			System.out.println("Packet is a valid format");
		} else {
			System.exit(1);
		}
		
		// get request type
		int request = data[1];
		len = 4;
		if(request == 1) {			
			data = new byte[] {0, 3, 0, 1};
		} else {
			data = new byte[] {0, 4, 0, 0};
		}
		
		String response = new String(data,0,len);
		System.out.println(response + "\n");
		System.out.println(data);

		sendPacket = new DatagramPacket(data, len,
				receivePacket.getAddress(), receivePacket.getPort());

		System.out.println("Server: Sending packet:");
		len = sendPacket.getLength();
		System.out.println(new String(sendPacket.getData(),0,len));

		// Send the datagram packet to the client via the send socket. 
		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Server: packet sent");

		// We're finished, so close the sockets.
		sendSocket.close();
		receiveSocket.close();
	}

	private boolean checkValidity(byte[] data, int len) {
		byte msg[] = new byte[len];
		System.arraycopy(data, 0, msg, 0, len);
		
		if(msg[0] != 0) {
			return false;
		}
		if(!(msg[1] == 1 || msg[1] == 2)) {
			return false;
		}
		if (msg[msg.length - 1] != 0) {
			return false;
		}
		byte dest[] = new byte[msg.length - 3];
		System.arraycopy(msg, 2, dest, 0, dest.length);

		int count = 0;
		int index = -1;
		for(int i = 0; i < dest.length; ++i) {
			if(dest[i] == 0) {
				++count;
				index = i;
			}
		}

		if(count == 1 && index > 0 && index < dest.length - 1)
			return true;
		else
			return false;
	}

	public static void main( String args[] )
	{
		Server c = new Server();
		c.receiveAndEcho();
	}
}
