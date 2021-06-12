import java.net.InetAddress;


public class Clients {

    String name;
    InetAddress IP;
    int port;

    /**
     * Class constructor.
     * @param n Username
     * @param ip User' IP
     * @param p User's PORT
     */
    public Clients(String n, InetAddress ip, int p){
        name = n;
        IP = ip;
        port = p;
    }
    /**
     * Getter method to return user's username.
     * @return the username of the user.
     */
    public String getName() {
        return name;
    }
    /**
     * Getter method to return the user's port.
     * @return the user's port
     */
    public int getPort() {
        return port;
    }
    /**
     * Getter method to retuen the user's IP.
     * @return the user's IP
     */
    public InetAddress getIP(){
        return IP;
    }

}
