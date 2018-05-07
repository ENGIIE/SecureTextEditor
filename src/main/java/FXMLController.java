
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class FXMLController {

    //dir = new File("./src/main/java/savefiles/textfiles.xml");
    private static XMLManager xmlm= new XMLManager();

    public  FXMLController () {

    }



    @FXML
    private Button register;

    @FXML
    private Button back;

    @FXML
    private Button login;

    @FXML
    private Button open;

    @FXML
    private Button newFile;

    @FXML
    private Button save;

    @FXML
    private TextArea textArea;

    @FXML
    private TextField fileName;

    @FXML
    private ListView<String> listView;
    private Set<String> stringSet;
    ObservableList observableList = FXCollections.observableArrayList();

    @FXML
    protected void handleRegisterButtonAction(ActionEvent event) throws IOException {
        System.out.println("Register");
        Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
        Stage stage = (Stage) register.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    protected void handleBackButtonAction(ActionEvent event) throws IOException {
        System.out.println("Back");
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = (Stage) back.getScene().getWindow();
        stage.setScene(new Scene(root));
    }


    @FXML
    protected void handleLoginButtonAction(ActionEvent event) throws IOException {
        System.out.println("Login");

        Parent root = FXMLLoader.load(getClass().getResource("startmenu.fxml"));
        Stage stage = (Stage) login.getScene().getWindow();
        stage.setScene(new Scene(root));
    }





    @FXML
    protected void handleOpenButtonAction(ActionEvent event) throws IOException {
        System.out.println("Open");




        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File f = chooser.showOpenDialog(open.getScene().getWindow());

        if (f != null) {

            xmlm.setPath(f.getPath());

            Parent root = FXMLLoader.load(getClass().getResource("editor.fxml"));

            System.out.println(f);

            Stage stage = (Stage) open.getScene().getWindow();
            Scene scene = new Scene(root);

            Node node = scene.lookup("#textArea");
            TextArea txta = (TextArea) node;
            txta.setText(xmlm.loadText(f));

            stage.setScene(scene);

        }


    }


    @FXML
    protected void handleNewFileButtonAction(ActionEvent event) throws IOException {
        System.out.println("New File");

        //xmlm.setFileName(fileName.getText());
        xmlm.setToNewFile();

        Parent root = FXMLLoader.load(getClass().getResource("editor.fxml"));
        Stage stage = (Stage) newFile.getScene().getWindow();
        Scene scene = new Scene(root);

        Node node = scene.lookup("#textArea");
        TextArea txta = (TextArea) node;
        txta.setText(null);

        stage.setScene(scene);

    }


    @FXML
    protected void handleSaveButtonAction(ActionEvent event) throws IOException {
        System.out.println("Save");

        if(xmlm.isNewFile() == true) {

            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File f = chooser.showSaveDialog(save.getScene().getWindow());

            if (f != null) {

                try {
                    xmlm.setToNewFile();
                    xmlm.saveText(f.getAbsolutePath(),textArea.getText());

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }

            }



        }else {

            xmlm.saveText(null,textArea.getText());

        }



    }


}