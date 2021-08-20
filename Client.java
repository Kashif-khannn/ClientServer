
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Color;

public class Client  {
	static Socket kkSocket = null;
    static PrintWriter out = null;
    static BufferedReader in = null;
	
	public static void main(String[] args) throws Exception
    {
		JFrame gui=new JFrame();
		gui.setLayout(null);
		gui.setSize(800, 820);
		gui.setTitle("Client");
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JLabel ipNum = new JLabel("IP Number: ");
		ipNum.setBounds(200,50,100,10);
		gui.add(ipNum);
		JTextField ip_num = new JTextField(10);
		ip_num.setBounds(200,70,200,20);
		gui.add(ip_num);
		
		
		JLabel portNum = new JLabel("Port Number: ");
		portNum.setBounds(500,50,100,10);
		gui.add(portNum);
		JTextField port_num = new JTextField(10);
		port_num.setBounds(500,70,200,20);
		gui.add(port_num);
		
		JButton conn = new JButton("Connect"); 
		conn.setBounds(300, 100, 100,30);
		gui.add(conn);
		
		JButton disconn = new JButton("DisConnect"); 
		disconn.setBounds(500, 100,100,30);
		gui.add(disconn);
		disconn.setEnabled(false);
		
		
		JLabel msg = new JLabel("Type command here:");
		msg.setBounds(200, 150, 200,20);
		gui.add(msg);
		JTextArea write=new JTextArea(2,30);
		write.setBounds(200,180,130 ,50);
		gui.add(write);
		
		
		 JButton send=new JButton("Send");
		 send.setBounds(500, 180, 100, 30);
		 gui.add(send);
		 send.setEnabled(false);
		 

		 
		 

		 
		 JLabel isbn = new JLabel("ISBN");
		 isbn.setBounds(200,300,200,10);
		 gui.add(isbn);
         JTextField isbn1 =  new JTextField(30);
         isbn1.setBounds(260,295, 300, 30);
         gui.add(isbn1);
         
         JLabel title = new JLabel("Title");
		 title.setBounds(200,350,200,10);
		 gui.add(title);
         JTextField title1 =  new JTextField(30);
         title1.setBounds(260,345, 300, 30);
         gui.add(title1);
         
         
         JLabel author = new JLabel("Author");
		 author.setBounds(200,400,200,10);
		 gui.add(author);
         JTextField author1 =  new JTextField(30);
         author1.setBounds(260,395, 300, 30);
         gui.add(author1);
       
         JLabel publisher = new JLabel("Publisher");
		 publisher.setBounds(200,450,200,10);
		 gui.add(publisher);
         JTextField publisher1 =  new JTextField(30);
         publisher1.setBounds(260,445, 300, 30);
         gui.add(publisher1);
         
         JLabel year = new JLabel("Year");
		 year.setBounds(200,500,200,10);
		 gui.add(year);
         JTextField year1 =  new JTextField(30);
         year1.setBounds(260,495, 300, 30);
		 gui.add(year1);
		 




		 JLabel res = new JLabel("Result");
         res.setBounds(200, 550, 200,10);
         JTextArea text=new JTextArea();
         JScrollPane scroll=new JScrollPane();
         scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
         scroll.setBounds(240, 550, 400, 200);
		  text.setBounds(240, 550, 400, 200);
		  text.setBackground(Color.WHITE);
		  text.setLineWrap(true);
		  text.setWrapStyleWord(true);

         scroll.getViewport().add(text);
		 gui.getContentPane().add(scroll);


		 JCheckBox check=new JCheckBox("BIB text");
         check.setBounds(140, 600, 100, 30);
		 gui.add(check);
      
		 
		
//===========================================================================================================

        conn.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent ae)
    	    {
    			 try {
    				int num1=Integer.parseInt(port_num.getText());
 		            kkSocket = new Socket(ip_num.getText(), num1);
 		            out = new PrintWriter(kkSocket.getOutputStream(), true);
 		            in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
					 JOptionPane.showMessageDialog(null,"Connected!");
					 send.setEnabled(true);
					 disconn.setEnabled(true);


					 Thread thread = new Thread(new ClientReader(kkSocket, in, text));
					 thread.start();
 		        } catch (UnknownHostException e) {
 		            System.err.println("Don't know about host: taranis.");
 		            
 		        } catch (IOException e) {
 		            System.err.println("Sever is not running.");
 		            
 		        } 
 		 
    	    }
    		
    	});
		
        disconn.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent ae)
    	    {
    			out.close();
				JOptionPane.showMessageDialog(null,"Disconnected!");
		
    			
    	    }
        });
        
        send.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent ae)
    	    { 
				String str=(String) write.getText();

				int reply = JOptionPane.YES_OPTION;

				if (str.equals("REMOVE")) {
					reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove?",
					"Remove Confirmation", JOptionPane.YES_NO_OPTION);
				}

				if (reply != JOptionPane.NO_OPTION) {
					if(str.equals("GET") && check.isSelected()){
						str = "BIB";
					} 

					String res=str + "," +isbn1.getText()+ "," +title1.getText()+ "," +author1.getText()+ "," +publisher1.getText()+ "," +year1.getText();
					out.println(res);
				}
    			
    	    }
        });
        gui.setVisible(true);
    }
	
	 

}