
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.IOException;

public class FXMLController {

    //dir = new File("./src/main/java/savefiles/textfiles.xml");

    XMLManager xmlm = new XMLManager();


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
        Parent root = FXMLLoader.load(getClass().getResource("editor.fxml"));
        Stage stage = (Stage) open.getScene().getWindow();
        stage.setScene(new Scene(root));
    }


    @FXML
    protected void handleNewFileButtonAction(ActionEvent event) throws IOException {
        System.out.println("New File");

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