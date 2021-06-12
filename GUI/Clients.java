import java.net.InetAddress;

public class Clients {

    String name;
    InetAddress IP;
    int port;

    public Clients(String n, InetAddress ip, int p){
        this.name = n;
        this.IP = ip;
        this.port = p;
    }

    public String getName() {
        return this.name;
    }

    public int getPort() {
        return this.port;
    }

    public InetAddress getIP(){
        return this.IP;
    }

}
