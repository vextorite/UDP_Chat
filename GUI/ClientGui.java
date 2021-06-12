import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.*;
import java.awt.event.ActionListener;

import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.*;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;





public class ClientGui extends JFrame implements ActionListener{

 private static final long serialVersionUID = 1L;
  JPanel pane1;
  JPanel pane2;
  JPanel pane3;
  JScrollPane pane1_2;
  JPanel pane1_3;
  JTextArea textl;
  static JTextArea history = new JTextArea(40, 70);
  JButton terminate;
  JButton sendAll;
  
  JButton send;
  JLabel label1;
  JLabel ipLabel;
  JLabel portLabel;
  JTextField ipText;
  JTextField portText;

  public  int PORT = 7505;
    public static DatagramSocket senderSocket;
    public static String recipient;
    public String reciever;
    public String name;
    public int myPort;
    public InetAddress IP;

    public ClientGui(String name, String recipient1, int myPort, InetAddress serverIP, int serverPort){
      this.name = name;
      recipient = recipient1;
      this.reciever = recipient1;
      this.myPort = myPort;
      this.PORT = serverPort;
      this.IP = serverIP;
      window();
    }


   private void window(){
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);
    setLocationRelativeTo(null);
    setFocusable(true);
    requestFocus();
    requestFocusInWindow();
    
    pane1 = new JPanel();
    pane2 = new JPanel();
    pane3 = new JPanel();
    //history = new JTextArea(40, 70);
    history.setEditable(false);
    textl = new JTextArea(15,60);
    textl.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) { 

        if (e.getKeyCode() == KeyEvent.VK_ENTER ){
            if (textl.getText().equals("")){
                JOptionPane.showMessageDialog(pane1,"No blank messages can be sent!"); 
                return;
            }else {
                
                try {
                  Sender.sendMessage(recipient+"%"+name+": "+ textl.getText(), IP, PORT);
                  history.append("\n"+ name+":"+textl.getText());
                  textl.setText("");
                } catch (IOException x) {
                  JOptionPane.showMessageDialog(pane1,"Error in message input!");
                }

            }
        }
    }
});


    pane1_2 = new JScrollPane(history);
    pane1_2.setBorder(BorderFactory.createTitledBorder("Chat Log"));
    pane1_3 = new JPanel();
    pane1_3.setBorder(BorderFactory.createTitledBorder("Please Enter text:"));


    terminate = new JButton("Terminate");
    sendAll = new JButton("Broadcast");
 
    send =  new JButton("Send");
    send.addActionListener((ActionListener) this);
    terminate.addActionListener((ActionListener) this);

    String st = " Is listening ** port";
    label1 = new JLabel(st,JLabel.RIGHT);
    ipLabel =  new JLabel("IP Number is: ", JLabel.RIGHT);
    portLabel = new JLabel("Port Number is: ", JLabel.RIGHT);

    ipText= new JTextField(10);
    ipText.setText("127.0.0.1");
    portText = new JTextField(5);
    portText.setText("7505");


    setLayout(new BorderLayout());
    add(pane1, BorderLayout.NORTH);
    add(pane2,BorderLayout.CENTER);
    add(pane3,BorderLayout.SOUTH);

    pane1.setLayout(new FlowLayout());
    pane1.add(label1);
    pane2.setLayout(new GridLayout(2,1));
    pane2.add(pane1_2);
    pane2.add(pane1_3);

    pane1_3.add(textl);

    pane3.setLayout(new FlowLayout());
    pane3.add(ipLabel);
    pane3.add(ipText);
    pane3.add(portLabel);
    pane3.add(portText);
    
    pane3.add(send);
    pane3.add(sendAll);
    pane3.add(terminate);

    history.append("User You are Chatting To is:  ");
    history.append(recipient);
    setVisible(true);
   }
/**
 * destroys the clients gui
 */
   public void terminate(){
     dispose();
   }
/**
 * a static method that prints to user screen a message
 * @param message
 */
  public static void toScreen(String message){
    try {
      history.append("\n"+ message);
      
    } catch (Exception e) {
      e.printStackTrace();
    }
     
  }


   @Override
   public void actionPerformed(ActionEvent e) {
      Object source = e.getSource();
      
      if(send.equals(source)){
        if(textl.getText().equals("")){//if no text to send a pop up will show
         JOptionPane.showMessageDialog(pane1,"No Blank Messages Please!");
        }
        else{
          
          try {
            Sender.sendMessageRisky(recipient+"%%"+name+": "+ textl.getText(), InetAddress.getByName("127.0.0.1"), PORT);
            history.append("\n"+ name+":"+textl.getText());
            textl.setText("");
          } catch (IOException x) {
            JOptionPane.showMessageDialog(pane1,"Error in message input!");
          }
          
          

          
        }
      }
      else if(terminate.equals(source)){
        try {
          Sender.sendMessageRisky(recipient+"%%"+name+": Has left the chat", InetAddress.getByName("127.0.0.1"), PORT);
          terminate();
        } catch (Exception y) {
          y.printStackTrace();
        }
      }
   }


 
}