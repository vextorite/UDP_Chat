import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ManageClient {
    public static void main(String[] args) throws IOException, InterruptedException{
        
        ExecutorService pool = Executors.newFixedThreadPool(2);
        
        InetAddress sIP;
        int sPort;
        DatagramSocket messageSocket;
        Listener listener;

        //if(args.length == 1 && args[0].compareTo("test")==0){}
        
        if(args.length == 3){
            sIP = InetAddress.getByName(args[0]); //server IP
            sPort = Integer.parseInt(args[1]); //server port number
            messageSocket = new DatagramSocket(Integer.parseInt(args[2])); //myportnumber
            listener = new Listener(messageSocket, sIP, sPort);
        }
        else{
            sIP = InetAddress.getByName("127.0.0.1");
            sPort = 7505;
            messageSocket = new DatagramSocket();
            listener = new Listener(messageSocket);
        }
        


        
        pool.execute(listener);

        //Sender sender = new Sender(messageSocket);
        /*
        Send Username as a packet to the server for soring in an array of users
         */
        byte[] tempSendBuffer = new byte[1024]; 
        byte[] tempSendBuffer2 = new byte[1024]; 
        
        Scanner nameScan = new Scanner(System.in);
        System.out.println("Enter your unique username: ");
        String username = nameScan.nextLine().toLowerCase();
        String newuser = ("newuser " + username);
        //long csvalue = Sender.generateCheckSumValue(newuser);
        String bnewuser = Sender.generateCSString(newuser);//(csvalue + "&&" + newuser);
        tempSendBuffer = bnewuser.getBytes(StandardCharsets.UTF_8);
        DatagramPacket usernamePacket = new DatagramPacket(tempSendBuffer, tempSendBuffer.length, sIP, sPort);
        messageSocket.send(usernamePacket);

        Thread.sleep(1000);
        
        String recipient = "norecipient";

        while(true){
            System.out.println("Search for user you want to connect to: ");
            recipient = nameScan.nextLine().toLowerCase();
            System.out.println("Searching... If no response, press Enter");
            byte[] buffer2 = ((Sender.generateCSString("clientrequest "+recipient+" "+username)).getBytes());
            messageSocket.send(new DatagramPacket(buffer2, buffer2.length, sIP, sPort));
            if(nameScan.nextLine().toLowerCase().compareTo("y") == 0){
              String cRequest = Sender.generateCSString("connectionrequest "+recipient+ " "+username);//(csvalue + "&&" + newuser);
              //System.out.println("Sent out by client, "+cRequest);
              tempSendBuffer2 = cRequest.getBytes(StandardCharsets.UTF_8);
              DatagramPacket bcrPacket = new DatagramPacket(tempSendBuffer2, tempSendBuffer2.length, sIP, sPort);
              messageSocket.send(bcrPacket);
              //System.out.println("Waiting for response...");  
              break;
            }
            else{
                System.out.println("User not in system yet");
            }
        }
        
        System.out.println("Connected. Send messages IF " + recipient + " has also connected to you");
        //System.out.println("-Connected to " + recipient + ".You can send messages if "+ recipient + " has also connected to you");
        /*
        Start Threads for sending and listening
         */
        //sender.setName(username);
        //sender.setRecipient(recipient);
        Sender sender = new Sender(messageSocket, username, recipient, sPort);
        pool.execute(sender);
        
    }


}
