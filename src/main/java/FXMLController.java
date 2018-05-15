import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class FXMLController {

    //dir = new File("./src/main/java/savefiles/textfiles.xml");

    /**
     * Create FileManager Object
     */
    private static FileManager manager = new FileManager();


    // FXML File Elements
    @FXML
    private Button open;

    @FXML
    private Button newFile;

    @FXML
    private Button save;

    @FXML
    private TextArea textArea;

    @FXML
    private MenuItem misave;

    @FXML
    private MenuItem miaes;

    @FXML
    private MenuItem miopen;

    @FXML
    private MenuItem miopenaes;


    //FXML Action Events

    /**
     * ActionEvent Open Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleOpenButtonAction(ActionEvent event) throws IOException {
        System.out.println("Open");

        //Creating FileChooser
        FileChooser chooser = new FileChooser();

        //Filter only Txt Files
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        //Show Open Dialog
        File f = chooser.showOpenDialog(open.getScene().getWindow());

        //Check if an Document was found
        if (f != null) {

            //Read filename into String and Set it
            String filename = null;
            int pos = f.getName().lastIndexOf(".");
            if(pos != -1) {
                filename = f.getName().substring(0, pos);
            }
            manager.setFileName(filename);

            //Save CurrentPath of file
            manager.setPath(f.getPath());


            //Load fxml file in Parent root
            Parent root = FXMLLoader.load(getClass().getResource("editor.fxml"));

            //Get current Stage
            Stage stage = (Stage) open.getScene().getWindow();

            //Create Scene with root element
            Scene scene = new Scene(root);

            //Get textArea from editor scene and load Content in text Area
            Node node = scene.lookup("#textArea");
            TextArea txta = (TextArea) node;
            txta.setText(manager.loadText(f));

            //Set the new Scene on the Stage
            stage.setScene(scene);

        }


    }


    /**
     * ActionEvent Open Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleAESOpenButtonAction(ActionEvent event) throws IOException {
        System.out.println("Open AES");

        //Creating FileChooser
        FileChooser chooser = new FileChooser();

        //Filter only Txt Files
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        //Show Open Dialog
        File f = chooser.showOpenDialog(open.getScene().getWindow());

        //Check if an Document was found
        if (f != null) {

            //Read filename into String and Set it
            String filename = null;
            int pos = f.getName().lastIndexOf(".");
            if(pos != -1) {
                filename = f.getName().substring(0, pos);
            }
            manager.setFileName(filename);

            //Save CurrentPath of file
            manager.setPath(f.getPath());


            //Load fxml file in Parent root
            Parent root = FXMLLoader.load(getClass().getResource("editor.fxml"));

            //Get current Stage
            Stage stage = (Stage) open.getScene().getWindow();

            //Create Scene with root element
            Scene scene = new Scene(root);

            //Get textArea from editor scene and load Content in text Area
            Node node = scene.lookup("#textArea");
            TextArea txta = (TextArea) node;
            txta.setText(manager.aesDecryptText(f));

            //Set the new Scene on the Stage
            stage.setScene(scene);

        }


    }


    /**
     * ActionEvent NewFile Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleNewFileButtonAction(ActionEvent event) throws IOException {
        System.out.println("New File");

        //Set boolean nFile true
        manager.setPath(null);
        manager.setFileName(null);

        //Load fxml file in Parent root
        Parent root = FXMLLoader.load(getClass().getResource("editor.fxml"));

        //Get current Stage
        Stage stage = (Stage) newFile.getScene().getWindow();

        //Create Scene with root element
        Scene scene = new Scene(root);

        //Get textArea from editor scene and clear it
        Node node = scene.lookup("#textArea");
        TextArea txta = (TextArea) node;
        txta.setText("");

        //Set the new Scene on the Stage
        stage.setScene(scene);

    }


    /**
     * ActionEvent Save Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleSaveButtonAction(ActionEvent event) throws IOException {
        System.out.println("Save");

        //If there is no currentPath
        if(manager.getPath()== null) {
            //Creating FileChooser
            FileChooser chooser = new FileChooser();

            //Filter only Txt Files
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            //Show Save Dialog
            File f = chooser.showSaveDialog(save.getScene().getWindow());

            //If file is saved
            if (f != null) {

                //Save fileName
                manager.setFileName(f.getName());

                //Save currentPath
                manager.setPath(f.getPath());

                try {
                    //Save Document in Path
                    manager.saveText(textArea.getText());

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }

            }
        }else{
            //Save Document in Path
            manager.saveText(textArea.getText());
        }


    }

    /**
     * ActionEvent AES Save Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleAESSaveButtonAction(ActionEvent event) throws IOException {
        System.out.println("AES");

        //If there is no currentPath
        if(manager.getPath()== null) {
            //Creating FileChooser
            FileChooser chooser = new FileChooser();

            //Filter only Txt Files
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            //Show Save Dialog
            File f = chooser.showSaveDialog(save.getScene().getWindow());

            //If file is saved
            if (f != null) {

                //Save fileName
                manager.setFileName(f.getName());

                //Save currentPath
                manager.setPath(f.getPath());

                try {
                    //Save Document in Path
                    manager.aesEncryptText(textArea.getText());

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }

            }


        }else{
            //Save Document in Path
            manager.aesEncryptText(textArea.getText());
        }



    }


}