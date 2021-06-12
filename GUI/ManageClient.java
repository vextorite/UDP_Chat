import java.io.IOException;

import java.net.*;
import java.nio.charset.StandardCharsets;
//import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.event.ActionEvent;
//import java.awt.event.*;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import java.lang.Thread;


public class ManageClient extends JFrame implements ActionListener{

    private static final long serialVersionUID = 1L;
    JPanel pane1;
    JTextField port;
    JTextField ip;
    JLabel iplabel;
    JLabel portLabel;
    JTextField sPort;
    JTextField sip;
    JLabel sIPlabel;
    JLabel sPortLabel;
    JTextField name;
    JLabel namelabel;
    JTextField connectTo;
    JLabel connectLabel;
    JButton logiButton;
    String username;
    int userPort=0;
    String connectToUser;
    InetAddress serverIP;
    int serverPort;
    ClientGui cg;
/**
 * Sets up a gui login window of the client
 */
    public void setWindow(){
        setResizable(false);
        setTitle("login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(380, 380);
        setLocationRelativeTo(null);
        requestFocus();
         requestFocusInWindow();
        pane1 = new JPanel();
        pane1.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(pane1);
        pane1.setLayout(null);

        name = new JTextField();
        name.setBounds(100, 40, 165, 28);
        pane1.add(name);

        namelabel = new JLabel("Username:");
        namelabel.setBounds(100, 24, 65, 16);
        pane1.add(namelabel);
        
        port = new JTextField("Default");
        port.setBounds(100, 96, 165, 28);
        pane1.add(port);
      

        portLabel = new JLabel("My Port:");
        portLabel.setBounds(100, 76, 77, 16);
        pane1.add(portLabel);
      

        sip = new JTextField("127.0.0.1");
        sip.setBounds(100, 161, 165, 28);
        pane1.add(sip);
        
        sIPlabel = new JLabel("Server IP Address:");
        sIPlabel.setBounds(100, 141, 150, 16);
        pane1.add(sIPlabel);
        

        sPort = new JTextField("7505");
        sPort.setBounds(100, 221, 165, 28);
        pane1.add(sPort);
        

        sPortLabel = new JLabel("Server Port:");
        sPortLabel.setBounds(100, 201, 77, 16);
        pane1.add(sPortLabel);
       


        connectTo = new JTextField("James");
        connectTo.setBounds(100, 271, 165, 28);
        pane1.add(connectTo);
        

        connectLabel = new JLabel("User Sending to:");
        connectLabel.setBounds(100, 251, 200, 16);
        pane1.add(connectLabel);
        

        logiButton = new JButton("Login");
        logiButton.addActionListener((ActionListener) this);
        logiButton.setBounds(121, 311, 117, 29);
        pane1.add(logiButton);

        setVisible(true);
    }
/**
 * Destroys the login window
 */
    public void loggedIn(){
        dispose();
    }
/**
 * When the login button is pressed it takes the values in the text fields and initiates the 
 * client gui then calls the loggedin function
 */
    @Override
    public void actionPerformed(ActionEvent e)  {
        Object source = e.getSource();
        if(logiButton.equals(source)){// 
            try {
                username = name.getText();
                connectToUser = connectTo.getText();
                String sIP1 = sip.getText();
                serverIP = InetAddress.getByName(sIP1);
                serverPort = Integer.parseInt(sPort.getText());
                if(!port.getText().equalsIgnoreCase("Default")){
                    userPort = Integer.parseInt(port.getText());
                }
                cg = new ClientGui(username, connectToUser,userPort,serverIP,serverPort);
                cg.label1.setText(" Is listening to port "+ cg.PORT);
                loggedIn();
            } catch (Exception x) {
                x.printStackTrace();
            }
           
        }
        
    }



    public static void main(String[] args) throws IOException, UnknownHostException{
        ManageClient manage = new ManageClient();//creates a manageclient instance
        manage.setWindow();//launches the log gui window
        ExecutorService pool = Executors.newFixedThreadPool(2);
       

        DatagramSocket messageSocket;
        InetAddress servIP ;
        int servPort;
        try{
            Thread.sleep(15000); //makes the thread sleep so that the user has enough time to fill in the login gui form
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        manage.cg.textl.setText("Wait for connection...");
        if(manage.cg.myPort!=0){
            servIP = manage.cg.IP;
            servPort = manage.cg.PORT;
            messageSocket = new DatagramSocket(manage.cg.myPort);
        }
        else{
            servIP = manage.cg.IP;
            servPort = manage.cg.PORT;
            messageSocket = new DatagramSocket();
        }
        
        Listener listener = new Listener(messageSocket);
        pool.execute(listener);

        byte[] tempSendBuffer = new byte[1024]; 
        byte[] tempSendBuffer2 = new byte[1024]; 
        String username = manage.cg.name.toLowerCase();
        String newuser = ("newuser " + username);
    
        String bnewuser = Sender.generateCSString(newuser);//(csvalue + "&&" + newuser);
        tempSendBuffer = bnewuser.getBytes(StandardCharsets.UTF_8);
        DatagramPacket usernamePacket = new DatagramPacket(tempSendBuffer, tempSendBuffer.length, servIP, servPort);
        messageSocket.send(usernamePacket);

       ;
        try{
            manage.cg.textl.setText("Attempting to connect...");
            Thread.sleep(1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        
        
        String recipient = "norecipient";
        
      
        while(true){ // loop that establishes a connection , default answer is 'y'
            
            recipient = manage.cg.reciever.toLowerCase();
            byte[] buffer2 = ((Sender.generateCSString("clientrequest "+recipient+" "+username)).getBytes());
            messageSocket.send(new DatagramPacket(buffer2, buffer2.length, servIP, servPort));
            try{
                manage.cg.textl.setText("");
                manage.cg.textl.setText("Dont press enter when typing in Y or N"+"\n");
                Thread.sleep(1000);
                manage.cg.textl.setText("");
                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            manage.cg.textl.setText("y");
            if(manage.cg.textl.getText().toLowerCase().compareTo("y") == 0){
                manage.cg.textl.setText("");
              String cRequest = Sender.generateCSString("connectionrequest "+recipient+ " "+username);
              tempSendBuffer2 = cRequest.getBytes(StandardCharsets.UTF_8);
              DatagramPacket bcrPacket = new DatagramPacket(tempSendBuffer2, tempSendBuffer2.length, servIP, servPort);
              messageSocket.send(bcrPacket);
               
              break;
            }
            else{
                ClientGui.toScreen("User not in system yet");
            }
        }
        ClientGui.toScreen("Connected. You can now start typing messages to " + recipient);

       
        Sender sender = new Sender(messageSocket, username, recipient);
        pool.execute(sender);
        manage.cg.textl.setText("");
       
    }




   


}
