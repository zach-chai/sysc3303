
import java.io.*;
import java.net.*;

public class IntermediateHost {

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket receiveSocket, sendReceiveSocket, sendSocket;


	public IntermediateHost() {
		try {
			sendReceiveSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(68);
			sendSocket = new DatagramSocket();
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	public void receiveAndSend() {

		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);
		System.out.println("Intermediate Host: Waiting for Packet.\n");

		try {        
			System.out.println("Waiting..."); // so we know we're waiting
			receiveSocket.receive(receivePacket);
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Intermediate Host: Packet received:");
		InetAddress clientAddress = receivePacket.getAddress();
		int clientPort = receivePacket.getPort();
		int len = receivePacket.getLength();
		String received = new String(data,0,len);   
		System.out.println(received + "\n");

		try {
			sendPacket = new DatagramPacket(data, len,
					InetAddress.getLocalHost(), 69);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println( "Intermediate Host: Sending packet:");

		try {
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		receivePacket = new DatagramPacket(data, data.length);

		try {
			// Block until a datagram is received via sendReceiveSocket.  
			sendReceiveSocket.receive(receivePacket);
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Intermediate Host: Packet received:");

		sendPacket = new DatagramPacket(data, len,
				clientAddress, clientPort);

		System.out.println("Intermediate Host: Sending packet:");
		len = sendPacket.getLength();
		System.out.println(new String(sendPacket.getData(),0,len));

		// Send the datagram packet to the client via the send socket. 
		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Intermediate Host: packet sent");

		sendReceiveSocket.close();
		receiveSocket.close();
		sendSocket.close();
	}

	public static void main(String[] args) {

		IntermediateHost ih = new IntermediateHost();
		ih.receiveAndSend();

	}

}
