import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.FileReader;
import java.io.FileWriter;


public class XMLManager {

    String text;
    FileWriter outputStream;
    FileReader inputStream;


    public XMLManager () {
        //Path fpath = Paths.get("./src/main/java/savefiles/textfiles.xml");
        text = null;
        outputStream = null;
        inputStream = null;

    }

    public void saveText (String text) {

        this.text = text;

        try {

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }


}
