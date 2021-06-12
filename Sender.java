import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Sender implements Runnable{

    public  int PORT = 7505;
    public static DatagramSocket senderSocket;
    public String recipient;
    public String name;
    public InetAddress IP;

    public Sender(DatagramSocket sendSocket){
        this.senderSocket = senderSocket;
    }
    
    public Sender(DatagramSocket senderSocket, String name, String r){
        this.senderSocket = senderSocket;
        this.name = name;
        recipient = r;
    }

    public Sender(DatagramSocket senderSocket, String name, String r, int serverPort){
        this.senderSocket = senderSocket;
        this.name = name;
        recipient = r;
        PORT = serverPort;
    }

    public Sender(DatagramSocket senderSocket, String name){
        this.senderSocket = senderSocket;
        this.name = name;
    }
    public static void sendMessage(String message, InetAddress IP, int p) throws IOException { 
        //long csvalue = generateCheckSumValue(message);
        String csmessage = generateCSString(message);//(csvalue + "&&" + message);
        //byte[] buffer = message.getBytes(StandardCharsets.UTF_8);
        byte[] buffer = csmessage.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, IP, p);
        senderSocket.send(packet);
    }

    public static long generateCheckSumValue(String data){
        long toReturn;
        byte[] bdata = data.getBytes();
        Checksum cs = new CRC32();
        cs.update(bdata, 0, bdata.length);
        toReturn = cs.getValue();
        return toReturn;
    }

    public static String generateCSString(String data){
        String toReturn = (generateCheckSumValue(data) + "&&" + data);
        return toReturn;
    }

    public static void sendMessageRisky(String message, InetAddress IP, int p) throws IOException { 
        
        int portRisky = p;
        long csValueRisky = generateCheckSumValue(message);

        Random random = new Random();
        int randomNumber = random.nextInt(100)+1;
        if(randomNumber < 6){
            portRisky = 7503;
        }
        else if(randomNumber < 11){
            //message = message + " - added so that checksum fails -";
            csValueRisky = 23453459;
        }
        
        String csmessage = (csValueRisky + "&&" + message);
        //byte[] buffer = message.getBytes(StandardCharsets.UTF_8);
        byte[] buffer = csmessage.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, IP, portRisky);
        senderSocket.send(packet);
    }
    
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        //BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        Scanner input = new Scanner(System.in);

        while(true){
            try {
                //sendMessage(recipient+"%"+name+": "+ input.readLine(), InetAddress.getByName("127.0.0.1"), PORT);
                sendMessageRisky(recipient+"%%"+name+": "+ input.nextLine(), InetAddress.getByName("127.0.0.1"), PORT);
            } catch (IOException e) {
                System.out.println("Error in message input :(");
            }
        }

    }
}
