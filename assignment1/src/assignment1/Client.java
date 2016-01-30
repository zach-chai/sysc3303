
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class Client {

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendReceiveSocket;

	public Client()
	{
		try {
			sendReceiveSocket = new DatagramSocket();
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	public void sendAndReceive()
	{

		String fileNameString = "test.txt";
		final byte read[] = new byte[] {0,1};
		final byte write[] = new byte[] {0,2};
		int requestType = 1;

		for(int i = 0; i < 11; ++i) {
			byte request[];
			int requestInt;
			String requestString;
			
			if(i == 10) {
				requestType = 5;
			}
			

			if(requestType == 1) {
				request = read;
				requestInt = 01;
				requestString = "read";
				requestType = 2;
			} else if(requestType == 2) {
				request = write;
				requestInt = 02;
				requestString = "write";
				requestType = 1;
			} else {
				request = new byte[] {0,5};
				requestInt = 05;
				requestString = "invalid";
			}

			byte mode[] = "netascii".getBytes();
			byte firstHalf[] = this.concatBytes(request, fileNameString.getBytes());
			byte secondHalf[] = new byte[mode.length + 2];
			secondHalf[0] = 0;
			secondHalf[mode.length - 1] = 0;
			System.arraycopy(mode, 0, secondHalf, 1, mode.length);

			byte msg[] = this.concatBytes(firstHalf, secondHalf);

			System.out.println("Request as String: " + requestString);
			System.out.println("Request as bytes: 0" + requestInt);
			System.out.println("FileName: " + new String(fileNameString));
			System.out.println("Mode: " + new String(mode) + "\n");


			try {
				sendPacket = new DatagramPacket(msg, msg.length,
						InetAddress.getLocalHost(), 68);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				System.exit(1);
			}

			// Send the datagram packet to the server via the send/receive socket. 
			try {
				sendReceiveSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}

			// Construct a DatagramPacket for receiving packets
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
			int len = receivePacket.getLength();

			byte bytes[] = new byte[len];
			System.arraycopy(data, 0, bytes, 0, len);
			System.out.print("Response byte array: ");
			for(byte b: bytes) {
				System.out.print(b);
			}
			System.out.println("\n");
		}

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