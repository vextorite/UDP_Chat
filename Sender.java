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
    
    /**
     * Class constructor specifying Socket, name, and recipient.
     * @param senderSocket Socket
     * @param name Username
     * @param r Recipient Username
     */
    public Sender(DatagramSocket senderSocket, String name, String r){
        this.senderSocket = senderSocket;
        this.name = name;
        recipient = r;
    }

    /**
     * Class constructor specifying Socket, name, recipient and server port.
     * @param senderSocket Socket
     * @param name Username
     * @param r Recipient Username
     * @param serverPort Server Port number
     */
    public Sender(DatagramSocket senderSocket, String name, String r, int serverPort){
        this.senderSocket = senderSocket;
        this.name = name;
        recipient = r;
        PORT = serverPort;
    }

    /**
     * Class constructor specifying Socket and name
     * @param senderSocket Socket
     * @param name Username
     */
    public Sender(DatagramSocket senderSocket, String name){
        this.senderSocket = senderSocket;
        this.name = name;
    }

    /**
     * Generates a checksum and appends it to the message before sending it through a socket.
     * @param message The message that will have it's checksum generated, appended and sent
     * @param IP The IP to be used
     * @param p The port to be used
     */
    public static void sendMessage(String message, InetAddress IP, int p) throws IOException { 
        //long csvalue = generateCheckSumValue(message);
        String csmessage = generateCSString(message);//(csvalue + "&&" + message);
        //byte[] buffer = message.getBytes(StandardCharsets.UTF_8);
        byte[] buffer = csmessage.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, IP, p);
        senderSocket.send(packet);
    }

     /**
     * Returns a long object that is the checksum value of the string object parsed.
     *
     * @param data The string object that will have it's checksum calculated
     * @return the checksum of the string object
     */
    public static long generateCheckSumValue(String data){
        long toReturn;
        byte[] bdata = data.getBytes();
        Checksum cs = new CRC32();
        cs.update(bdata, 0, bdata.length);
        toReturn = cs.getValue();
        return toReturn;
    }

    /**
     * Returns a String object that is a concatenation of the object with it's checksum.
     * @param data The String object to have it's checksum calculated
     * @return a new String object with the checksum appended
     */
    public static String generateCSString(String data){
        String toReturn = (generateCheckSumValue(data) + "&&" + data);
        return toReturn;
    }

    /**
     * Like the normal sendMessage() but with a 10% of causing an error
     * Errors: 5% chance checksum will fail, 5% chance message won't deliver
     * Normal sendMessage(): Generates a checksum and appends it to the message before sending it through a socket.
     * @param message The message that will have it's checksum generated, appended and sent
     * @param IP The IP to be used
     * @param p The port to be used
     * @throws IOException
     */
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
