import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Listener implements Runnable {
    
    private DatagramSocket socket;
    InetAddress sIP;
    int sPort;
    int j=0;
    
    public Listener(DatagramSocket s) throws UnknownHostException{ 
        socket = s;
        sIP = InetAddress.getByName("127.0.0.1");
        sPort = 7505;
    }

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

    public void recieve() throws IOException, NullPointerException{
        byte[] receivedByte = new byte[1024];
        DatagramPacket receivedPacket = new DatagramPacket(receivedByte, receivedByte.length);
        socket.receive(receivedPacket);
        
       
        String csr = new String(receivedPacket.getData());
        
        String[] csrarray = csr.split("&&");
        String r = csrarray[1].replaceAll("\\P{Print}","");

        long senderCSValue = Long.parseLong(csrarray[0]);
        long recievedCSValue = (Sender.generateCheckSumValue(r));
        

        String message;
        
        

        
        if(r.contains("newuser")){
            
            String newuser = r.split(" ")[1];
            
            Server.ClientList[j] = new Clients(newuser, 
            receivedPacket.getAddress(), receivedPacket.getPort());
           
            Sender.sendMessage(newuser+" connected to server!", receivedPacket.getAddress(), receivedPacket.getPort());
            j++;
        }
        else if(r.contains("clientrequest")){
            String[] clientrequest = r.split(" ");
            
            for(int i = 0; i < j; i++){
                if((Server.ClientList[i].getName()).compareTo(clientrequest[1]) == 0){
                    Sender.sendMessage(clientrequest[1]+" in system.", receivedPacket.getAddress(), receivedPacket.getPort());
                  
                    return;
                }
            }
        }
        else if(r.contains("connectionrequest ")){
            String[] connectionrequest = r.split(" ");
            message = (connectionrequest[1]+ "%%("+ connectionrequest[2] + " has connected to you.)");
            Sender.sendMessage(message, sIP, sPort);

        }
        else if(r.contains("%%")){
            String[] x = r.split("%%");
            String recipient = x[0];
            
            message = x[1]; 
            String n2 = recipient; 
            for(int i = 0; i < j ; i++){
                
                String n1 = Server.ClientList[i].getName();
                if(n1.equals(n2)){
                   
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
            
           ClientGui.toScreen(r);
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
               
                ClientGui.toScreen(myCSmessage);
                Sender.sendMessage(confirmation, sIP, sPort);
                
            }                                                                       
        }
        //return;

    }    


}



        
