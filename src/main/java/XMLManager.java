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
    Boolean nFile = true;
    String path;


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


    public void saveText (String path, String text) throws IOException {


        try {

            if(path != null){
                //outputStream = new FileWriter("./src/main/java/savefiles/"+tempname+".txt");
                outputStream = new FileWriter(path);
                outputStream.write(text);
                outputStream.close();
                this.path = path;
            }else {
                outputStream = new FileWriter(this.path);
                outputStream.write(text);
                outputStream.close();
            }


        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public  String loadText (File file) throws IOException {


        try {

            nFile = false;

            String filename = null;
            int pos = file.getName().lastIndexOf(".");
            if(pos != -1) {
                filename = file.getName().substring(0, pos);
            }

            tempname = filename;

            inputStream = new FileReader(file);
            BufferedReader br = new BufferedReader(inputStream);

            String zeile = "";

            while( (zeile = br.readLine()) != null )
            {
                text+= zeile;
            }
            inputStream.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        System.out.println(text);
        return text;
    }


    public boolean isNewFile (){
        return nFile;
    }

    public void setToNewFile () {
        nFile = true;
    }

    public void setPath (String path) {
        this.path = path;
    }

}
