import java.io.* ;
import java.net.* ;
import java.util.* ;


public class BibliographyProtocol implements Runnable {
    
    final static String CRLF = "\r\n";
    Socket socket;
    
    protected static  List<List<String>> database;

    // Constructor
    BibliographyProtocol(Socket socket,  List<List<String>> db) throws Exception {
    this.socket = socket;
    database = db;
    }
    
    // Implement the run() method of the Runnable interface.
    public void run() {
	try {
	    processRequest();
	} catch (Exception e) {
	    System.out.println(e);
    }
    
    }
    
    static PrintWriter out;
    static BufferedReader in;
    private void processRequest() throws IOException {
        Socket clientSocket;
         out = new PrintWriter(this.socket.getOutputStream(), true);
         in = new BufferedReader(
        new InputStreamReader(
        this.socket.getInputStream()));
        String inputLine, outputLine;
        outputLine = "";

//outputLine = kkp.processInput(null);
// reading from the client
String inputcmd;
while ((inputLine = in.readLine()) != null) {
    // splits array
     String [] inputArray = inputLine.split(",");
      inputcmd = inputArray[0];
      // get sub array that excludes input command and convert it to a list
     List <String> bibliobook  = Arrays.asList(Arrays.copyOfRange(inputArray, 1,inputArray.length));
        System.out.println(inputcmd);


       // outputLine = " ";
    
     switch(inputcmd){
     case "SUBMIT":
     if(processSubmit(bibliobook)) {
         out.println("SUBMIT SUCCESS!");
    } else{
        out.println("Submit failed because it already exist.");

       }
     break;

    case "UPDATE":
   if(processUpdate(bibliobook)){
    out.println("Update SUCCESS!");

   } else{
    out.println("Update failed!");

   }
   
   break;

    case "GET":
    if(database.isEmpty()){
        out.println("Get Unsuccessful because its empty");
    } else{
        processGet(bibliobook, false);       
    }
    break;

    case "BIB":
    if(database.isEmpty()){
        out.println("[Get Unsuccessful because its empty");
    } else{
        processGet(bibliobook, true);       
    }       
    break;
    case "REMOVE":
    if(database.isEmpty()){
        out.println("unsuccessfully removed because its empty");
    } else {
        processRemove(bibliobook);
        out.println("Successfully removed ");
       
    }
   
        break;
    default:
       out.println("Incorrect Command entered");
     
     }
     
    // outputLine = inputLine;
     // writing to the client
     out.println(outputLine);
     if (outputLine.equals("Bye."))
        break;
    }
out.close();
in.close();
this.socket.close();
    }

       // found this code on stack overflow lines 76-80
    private boolean isNumeric(String outputLine){
        for (char c: outputLine.toCharArray()){
            if(!Character.isDigit(c)) return false;
        }
        return true;
    }

    private boolean processSubmit( List<String> bibliobook) {
        // traversing to get isbn comparing it with bibliobooks isbn
       int idx =-1;
       for(int x =0; x< database.size(); x++){
           if(database.get(x).get(0).equals(bibliobook.get(0))){
               idx =x;
           }
       }

        
        if(!isNumeric(bibliobook.get(0))){
          System.out.println("ISBN not entered correctly");
          return false;
        } 
        if(idx == -1 && bibliobook.get(0).length() ==13){
            database.add(bibliobook);


        } else return false;

        return true;
        
    }
    private boolean processUpdate(List<String> bibliobook){
     
       
       // traversing to get isbn comparing it with bibliobooks isbn
       int idx =-1;
        for(int x =0; x< database.size(); x++){
            if(database.get(x).get(0).equals(bibliobook.get(0))){
                idx =x;
            }
        }
       if(idx != -1 && database.get(idx).get(0).equals(bibliobook.get(0))){
           int i = 0;
           while( i < bibliobook.size()){
              
               if(!bibliobook.get(i).equals("")){
                database.get(idx).set(i,bibliobook.get(i));
               }
               i++;

           }

       } else return false;

       return true;
    }
    private void processGet(List<String> bibliobook, boolean bibtex){
        boolean isFound = false;

       String formatStr;
           String output = "";
           
        boolean checker = false;
        for(int i =0; i<database.size(); i++){
            isFound = true;
            for(int j =1; j<bibliobook.size(); j++){
                if(!bibliobook.get(j).isEmpty()){
                    if(!database.get(i).get(j).equals(bibliobook.get(j))){
                       isFound = false;
                        break;
                    }        
                    
                } 
            
            }
            if(isFound){
                checker = true;
                if(bibtex == true){
                        formatStr = 
                    "@book{" +
                    "  ISBN = {%s},\n" +
                    "  TITLE = {%s},\n" +
                    "  AUTHOR = {%s},\n" +
                    "  PUBLISHER = {%s},\n" +
                    "  YEAR = {%s}\n" +
                    "}\n";

                } else {
                    formatStr = 
                    "ISBN = {%s},\n" +
                    "TITLE = {%s},\n" +
                    "AUTHOR = {%s},\n" +
                    "PUBLISHER = {%s},\n" +
                    "YEAR = {%s}\n\n";
                }
                output += String.format(formatStr,
                database.get(i).get(0),
                database.get(i).get(1),
                database.get(i).get(2),
                database.get(i).get(3),
                database.get(i).get(4));
            }

        }
        if(checker == false){
            out.println("No Book found");
        } else {
            out.println(output);
        }

    }

    private void processRemove(List<String> bibliobook){

        boolean isFound = false;

        String formatStr;
            String output = "";
            
         boolean checker = false;
         for(int i =0; i<database.size(); i++){
             isFound = true;
             for(int j =1; j<bibliobook.size(); j++){
                 if(!bibliobook.get(j).isEmpty()){
                     if(!database.get(i).get(j).equals(bibliobook.get(j))){
                        isFound = false;
                         break;
                     }        
                     
                 } 
             
             }
             if(isFound){
                checker = true;
                database.remove(i);
             }
         }

         if(checker){
            out.println("No Book found to remove");
         }

    }

    

    private  List<String> processOutputToList(String outputLine){
        List<String> bibliobook = new ArrayList<String>();
        
       String [] splited = outputLine.split(" ");
       int index =0;
       for(String word: splited){

        // checking if its a component
             int isComponent = checkingComponent(word);
             // if it is a component we will set the index to write to the correct component index of bibliobook
            if(isComponent != -1){
                index = isComponent;

            } else{ 
                // else append data to the index it points to
                // sets bibliobook list with an index
                if(bibliobook.get(index).isEmpty()){
                    // if index is empty then we are just adding the word
                    bibliobook.set(index, word);
                } else{
                    // other case we will add a space then the word
                    bibliobook.set(index,bibliobook.get(index) + " " + word);
                }

             
            }

        }
        return bibliobook;
                  
    }
    private int checkingComponent(String outputLine){
        // Setting each index for a component of the books information

        if(outputLine.equals("AUTHOR")){

            return 2;

        } else if(outputLine.equals("PUBLISHER")){
            return 3;

        } else if(outputLine.equals("YEAR")){
            return 4;

        } else if(outputLine.equals("ISBN")){
            return 0;


        } else if(outputLine.equals("TITLE")){
            return 1;
        }
        return -1;

        
    }
}
