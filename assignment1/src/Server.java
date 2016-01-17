
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

   public static void main( String args[] )
   {
      Server c = new Server();
      c.receiveAndEcho();
   }
}
