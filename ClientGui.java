import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;



public class ClientGui extends JFrame implements ActionListener{
  /**
  *
  */
 private static final long serialVersionUID = 1L;
  JPanel pane1;
  JPanel pane2;
  JPanel pane3;
  JScrollPane pane1_2;
  JPanel pane1_3;
  JTextArea textl;
  JTextArea history;
  JButton terminate;
  JButton sendAll;
  JButton connect;
  JButton send;
  JLabel label1;
  JLabel ipLabel;
  JLabel portLabel;
  JTextField ipText;
  JTextField portText;


   public void window(){
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);
    setLocationRelativeTo(null);
    setFocusable(true);
    requestFocus();
    requestFocusInWindow();
    
    pane1 = new JPanel();
    pane2 = new JPanel();
    pane3 = new JPanel();
    history = new JTextArea(40, 70);
    history.setEditable(false);
    textl = new JTextArea(15,60);


    pane1_2 = new JScrollPane(history);
    pane1_2.setBorder(BorderFactory.createTitledBorder("Chat Log"));
    pane1_3 = new JPanel();
    pane1_3.setBorder(BorderFactory.createTitledBorder("Please Enter text:"));


    terminate = new JButton("Terminate");
    sendAll = new JButton("Broadcast");
    connect = new JButton("Connect");
    connect.addActionListener(this);
    send =  new JButton("Send");
    send.addActionListener((ActionListener) this);

    String st = " Is listening ** port";
    label1 = new JLabel(st,JLabel.RIGHT);
    ipLabel =  new JLabel("IP Number is: ", JLabel.RIGHT);
    portLabel = new JLabel("Port Number is: ", JLabel.RIGHT);

    ipText= new JTextField(10);
    ipText.setText("LocalHost");
    portText = new JTextField(5);
    portText.setText("85858");


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
    pane3.add(connect);
    pane3.add(send);
    pane3.add(sendAll);
    pane3.add(terminate);


    setVisible(true);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      Object source = e.getSource();
      if(connect.equals(source)){
       connect.setEnabled(false);
      }
      else if(send.equals(source)){
        if(textl.getText().equals("")){
         JOptionPane.showMessageDialog(pane1,"No Blank Messages Please!");
        }
        else{
          history.append(textl.getText());
          textl.setText("");
        }
      }
   }


 
}