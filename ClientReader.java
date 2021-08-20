import java.io.* ;
import java.net.* ;
import java.util.* ;

import javax.swing.JTextArea;

public class ClientReader implements Runnable{
    
    private BufferedReader in;
    private Socket socket;
    private JTextArea textArea;

    ClientReader(Socket socket, BufferedReader in, JTextArea textArea) {
        this.socket = socket;
        this.in = in;
        this.textArea = textArea;
    }

    public void run(){
        try{
            String getResult = "";
            while(((getResult = in.readLine())) != null){
                System.out.println(getResult);
                this.textArea.append(getResult + "\n");

            }
        } catch( Exception e){
            System.out.println(e);
        }
    }
}
