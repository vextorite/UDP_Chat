import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Listener implements Runnable {
    
    private DatagramSocket socket;
    InetAddress sIP;
    int sPort;
    int j = 0;
    
    
    /**
     * Class constructor specifying Socket
     * @param s Socket to listen on
     * @throws UnknownHostException
     */
    public Listener(DatagramSocket s) throws UnknownHostException{ 
        socket = s;
        sIP = InetAddress.getByName("127.0.0.1");
        sPort = 7505;
    }

    /**
     * Class constructor specifying Socket, Server IP and Server Port number
     * @param s Socket to listen on
     * @param serverIP Server IP Address
     * @param serverPort Server port number
     * @throws UnknownHostException
     */
    public Listener(DatagramSocket s, InetAddress serverIP, int serverPort) throws UnknownHostException{ 
        socket = s;
        sIP = serverIP;
        sPort = serverPort;
    }

    /**
     * Class constructor specifying Socket, and Server Port number
     * @param s Socket to listen on
     * @param serverPort Server port number
     * @throws UnknownHostException
     */
    public Listener(DatagramSocket s, int serverPort) throws UnknownHostException{ 
        socket = s;
        sIP = InetAddress.getByName("127.0.0.1");
        sPort = serverPort;
    }

    @Override
    public void run(){
        while(true){
            try {
                recieve();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    /**
     * Filter and/or routes messages recieved by the Listener threads
     * The first 4 "if" statements will only be accessed by the server. 
     * If the message passes all these checks and gets to the last "else" statement, it is at the destination client.
     * @throws IOException
     */
    public void recieve() throws IOException{
        byte[] receivedByte = new byte[1024];
        DatagramPacket receivedPacket = new DatagramPacket(receivedByte, receivedByte.length);
        socket.receive(receivedPacket);
        
        //String r = new String(receivedPacket.getData());
        String csr = new String(receivedPacket.getData());
        //System.out.println("recieved by listener: " + csr);
        String[] csrarray = csr.split("&&");
        String r = csrarray[1].replaceAll("\\P{Print}","");

        long senderCSValue = Long.parseLong(csrarray[0]);
        long recievedCSValue = (Sender.generateCheckSumValue(r));
        //System.out.println("Sender: " + senderCSValue);
        //System.out.println("Reciever: " + recievedCSValue);

        String message;
        //System.out.println("String received: "+r);
        

        
        if(r.contains("newuser")){
            //System.out.println(r);
            String newuser = r.split(" ")[1];
            //System.out.println("String split: "+newuser);
            //System.out.println(Server.j);
            Server.ClientList[Server.j] = new Clients(newuser, 
            receivedPacket.getAddress(), receivedPacket.getPort());
            //System.out.println("Array at index 0 "+Server.ClientList[0].getName());
            Sender.sendMessage(newuser+" connected to server!", receivedPacket.getAddress(), receivedPacket.getPort());
            Server.j++;
            //System.out.println(Server.j);
            
        }
        else if(r.contains("clientrequest")){
            String[] clientrequest = r.split(" ");
            //System.out.println(clientrequest.length);
            //System.out.println(clientrequest[0]+" "+clientrequest[1]+" "+clientrequest[2]);
            int length = Server.j;
            //System.out.println(length);
            for(int i = 0; i < length; i++){
                if((Server.ClientList[i].getName()).compareTo(clientrequest[1]) == 0){
                    Sender.sendMessage(clientrequest[1]+" in system. Connect? (y/n)", receivedPacket.getAddress(), receivedPacket.getPort());
                    //Thread.sleep(1000);
                    //Sender.sendMessage("connectionrequest$$"+ clientrequest[2],Server.ClientList[i].getIP(),Server.ClientList[i].getPort());
                    return;
                }
            }
            //return;
        }
        else if(r.contains("connectionrequest ")){
            String[] connectionrequest = r.split(" ");
            message = (connectionrequest[1]+ "%%("+ connectionrequest[2] + " has connected to you.)");
            Sender.sendMessage(message, sIP, sPort);

        }
        else if(r.contains("%%")){
            String[] x = r.split("%%");
            String recipient = x[0];
            //System.out.println("Recipient: " + x[0]);
            message = x[1]; 
            String n2 = recipient; //.replaceAll("\\P{Print}",""); //might be able to remove
            for(int i = 0; i < Server.j + 1; i++){
                //System.out.println("."+Server.ClientList[i].getName() + ". vs ." + recipient + ".");
                String n1 = Server.ClientList[i].getName();//.replaceAll("\\P{Print}","");
                if(n1.equals(n2)){
                    //System.out.println("MATCH!");
                    if(message.contains("connected") || message.contains("Delivered")){
                        Sender.sendMessage(message, Server.ClientList[i].getIP(), 
                        Server.ClientList[i].getPort());
                    }
                    else{
                        Sender.sendMessageRisky(message, Server.ClientList[i].getIP(), 
                        Server.ClientList[i].getPort()); 
                    }
                    
                    Sender.sendMessage("(Delivered to Server)", receivedPacket.getAddress(), receivedPacket.getPort());
                    
                    return; 
                }
  
            }
            Sender.sendMessage((recipient+" not found"), 
            receivedPacket.getAddress(), receivedPacket.getPort());     

        }
        else{
            System.out.println(r);
            if(r.contains(": ")){                 
                String csResult;
                String myCSmessage = "(Sending delivery report...)";
                
                if(recievedCSValue==senderCSValue){
                    csResult = "and passed Checksum test)";
                }
                else{
                    csResult = "BUT failed checksum test! It may be corrupted. Please resend message)";
                    myCSmessage = "(Message failed checksum test. It may be corrupted." + "Sending delivery report!)";
                }
                
                String sender = r.split(":")[0];
                String confirmation = (sender + "%%" + "(Delivered to User " + "\n" + csResult); 
                System.out.println(myCSmessage);
                Sender.sendMessage(confirmation, sIP, sPort);
                
            }                                                                       
        }
        

    }    


}

 