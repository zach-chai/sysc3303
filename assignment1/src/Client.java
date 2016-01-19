
import java.io.*;
import java.net.*;

public class Client {

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendReceiveSocket;

	public Client()
	{
		try {
			sendReceiveSocket = new DatagramSocket();
		} catch (SocketException se) {   // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
	}

	public void sendAndReceive()
	{

		String fileNameString = "test.txt";


		byte read[] = new byte[] {0,1};
		byte write[] = new byte[] {0,2};
		byte mode[] = "netascii".getBytes();
		byte firstHalf[] = this.concatBytes(read, fileNameString.getBytes());
		byte secondHalf[] = new byte[mode.length + 2];
		secondHalf[0] = 0;
		secondHalf[mode.length - 1] = 0;
		System.arraycopy(mode, 0, secondHalf, 1, mode.length);

		byte msg[] = this.concatBytes(firstHalf, secondHalf);

		System.out.println("Client: sending a packet containing:\n Bytes: " + msg + "\n String: " + new String(msg,0,msg.length));

		try {
			sendPacket = new DatagramPacket(msg, msg.length,
					InetAddress.getLocalHost(), 68);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("To host: " + sendPacket.getAddress());
		System.out.println("Destination host port: " + sendPacket.getPort());
		int len = sendPacket.getLength();

		// Send the datagram packet to the server via the send/receive socket. 
		try {
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Client: Packet sent.\n");

		// Construct a DatagramPacket for receiving packets up 
		// to 100 bytes long (the length of the byte array).
		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);

		try {
			// Block until a datagram is received via sendReceiveSocket.  
			sendReceiveSocket.receive(receivePacket);
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Process the received datagram.
		System.out.println("Client: Packet received:");
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());
		len = receivePacket.getLength();
		System.out.println("Length: " + len);
		System.out.print("Containing: ");

		// Form a String from the byte array.
		String received = new String(data,0,len);   
		System.out.println(received);

		// We're finished, so close the socket.
		sendReceiveSocket.close();
	}

	private byte[] concatBytes(byte[] a, byte[] b) {
		byte[] dest = new byte[a.length + b.length];
		System.arraycopy(a, 0, dest, 0, a.length);
		System.arraycopy(b, 0, dest, a.length, b.length);
		return dest;
	}

	public static void main(String args[])
	{
		Client c = new Client();
		c.sendAndReceive();
	}
}