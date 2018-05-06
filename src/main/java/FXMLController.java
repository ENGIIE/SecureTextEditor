
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


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
    private Button load;

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

        Parent root = FXMLLoader.load(getClass().getResource("open.fxml"));
        Stage stage = (Stage) login.getScene().getWindow();
        stage.setScene(new Scene(root));
    }


    @FXML
    protected void handleOpenButtonAction(ActionEvent event) throws IOException {
        System.out.println("Open");

        FileChooser chooser = new FileChooser();
        File f = chooser.showOpenDialog((Stage) open.getScene().getWindow());
        Parent root = FXMLLoader.load(getClass().getResource("editor.fxml"));

        Stage stage = (Stage) open.getScene().getWindow();
        textArea.setText("1234");
        stage.setScene(new Scene(root));
    }


    @FXML
    protected void handleNewFileButtonAction(ActionEvent event) throws IOException {
        System.out.println("New File");

        xmlm.setFileName(fileName.getText());

        Parent root = FXMLLoader.load(getClass().getResource("editor.fxml"));
        Stage stage = (Stage) newFile.getScene().getWindow();
        stage.setScene(new Scene(root));

    }

    @FXML
    protected void handleLoadButtonAction(ActionEvent event) throws IOException {
        System.out.println("Load");
        Parent root = FXMLLoader.load(getClass().getResource("open.fxml"));
        Stage stage = (Stage) load.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    protected void handleSaveButtonAction(ActionEvent event) throws IOException {
        System.out.println("Save");

        xmlm.saveText(textArea.getText());

    }


}