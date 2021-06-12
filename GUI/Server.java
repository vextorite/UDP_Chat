import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread{
    
    static Clients[] ClientList = new Clients[100];
    static int j;
    static int serverPort;
    
    public static void main(String args[]) throws SocketException, UnknownHostException{   

    String username = "Server";
    
    if(args.length== 1){
        serverPort = Integer.parseInt(args[0]);
    }
    else{
        serverPort = 7505;
    }

    ExecutorService pool = Executors.newFixedThreadPool(2);    
    DatagramSocket serverSocket = new DatagramSocket(serverPort);
    
    
    Sender sendthread = new Sender(serverSocket, username);
    Listener listen = new Listener(serverSocket);

    pool.execute(listen); 
    pool.execute(sendthread);   
    
    System.out.println("Server Running and listening on...");
    System.out.println("Port number, "+ serverPort);

    }


}
