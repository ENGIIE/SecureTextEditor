import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.*;


public class XMLManager {

    FileWriter outputStream;
    FileReader inputStream;
    String tempname;
    String text;


    public XMLManager () {
        //Path fpath = Paths.get("./src/main/java/savefiles/textfiles.xml");
        outputStream = null;
        inputStream = null;
        tempname = null;
        System.out.println("Aufruf");
    }


    public void setFileName (String filename) {
        tempname = filename;
    }


    public void saveText (String text) throws IOException {


        try {

            //inputStream = new FileReader("test.txt");
            outputStream = new FileWriter("./src/main/java/savefiles/"+tempname+".txt");

            outputStream.write(text);
            outputStream.close();
           // int c;
            //while ((c = inputStream.read()) != -1) {
                //outputStream.write(c);
           // }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public  String loadText (File file) throws IOException {


        try {

            inputStream = new FileReader(file);
            int c;
            while ((c = inputStream.read()) != -1) {
                text += c;
            }
            inputStream.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return text;
    }


}
