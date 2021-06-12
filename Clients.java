import java.net.InetAddress;

public class Clients {

    String name;
    InetAddress IP;
    int port;

    public Clients(String n, InetAddress ip, int p){
        name = n;
        IP = ip;
        port = p;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getIP(){
        return IP;
    }

}
