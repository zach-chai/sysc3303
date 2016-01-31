package assignment1;

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

	public void relay() {

		byte data[]; 

		while(true) {
			data = new byte[100];
			receivePacket = new DatagramPacket(data, data.length);

			try {
				System.out.println("Waiting...");
				receiveSocket.receive(receivePacket);
			} catch (IOException e) {
				System.out.println("Receive Socket Timed Out.\n" + e);
				e.printStackTrace();
				System.exit(1);
			}

			InetAddress clientAddress = receivePacket.getAddress();
			int clientPort = receivePacket.getPort();
			int len = receivePacket.getLength();

			// extract info
			int requestInt = data[1];
			String requestString = requestInt == 1 ? "read" : "write";
			byte fileName[] = extractInfo(data, 2);
			byte mode[] = extractInfo(data, fileName.length + 3);

			System.out.println("Intermediate Host: Sending packet to server:");
			System.out.println("Request as String: " + requestString);
			System.out.println("Request as bytes: 0" + requestInt);
			System.out.println("FileName: " + new String(fileName));
			System.out.println("Mode: " + new String(mode) + "\n");

			try {
				sendPacket = new DatagramPacket(data, len,
						InetAddress.getLocalHost(), 69);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				System.exit(1);
			}

			String relayData = new String(data,0,len);   

			receivePacket = new DatagramPacket(data, data.length);

			// sends datagram to host via send/receive socket port 69
			try {
				sendReceiveSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}

			try {
				sendReceiveSocket.receive(receivePacket);
			} catch(IOException e) {
				e.printStackTrace();
				System.exit(1);
			}

			System.out.println("Intermediate Host: Sending packet to client:");

			len = receivePacket.getLength();
			byte bytes[] = new byte[len];
			System.arraycopy(data, 0, bytes, 0, len);
			for(byte b: bytes) {
				System.out.print(b);
			}
			System.out.println("\n");

			sendPacket = new DatagramPacket(data, len,
					clientAddress, clientPort);

			// Send the datagram packet to the client via the send socket. 
			try {
				sendSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	private byte[] extractInfo(byte[] data, int startPos) {
		int index = 0;
		for(int i = startPos; i < data.length; ++i) {
			if(data[i] == 0) {
				index = i;
				break;
			}
		}

		byte[] dest = new byte[index - startPos];
		System.arraycopy(data, startPos, dest, 0, dest.length);

		return dest;
	}

	public static void main(String[] args) {

		IntermediateHost ih = new IntermediateHost();
		ih.relay();

	}

}
