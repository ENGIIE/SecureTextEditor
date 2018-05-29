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

    /**
     * Create FileManager Object
     */
    private static FileManager manager = new FileManager();

    // FXML File Elements
    @FXML
    private Button open;
    @FXML
    private MenuButton mbopen;
    @FXML
    private MenuButton mbsave;
    @FXML
    private MenuButton mbsaveas;
    @FXML
    private Button newFile;
    @FXML
    private TextArea textArea;
    @FXML
    private Button back;
    @FXML
    private Button settings;


    //FXML Action Events

    /**
     * ActionEvent Settings Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleSettingsButtonAction(ActionEvent event) throws IOException {
        System.out.println("Settings");
        //Load fxml file in Parent root
        Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
        //Get current Stage
        Stage stage = (Stage) settings.getScene().getWindow();
        //Create Scene with root element
        Scene scene = new Scene(root);
        //Set the new Scene on the Stage
        stage.setScene(scene);
    }

    /**
     * ActionEvent Back Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleBackButtonAction(ActionEvent event) throws IOException {
        System.out.println("Back");
        //Load fxml file in Parent root
        Parent root = FXMLLoader.load(getClass().getResource("editor.fxml"));
        //Get current Stage
        Stage stage = (Stage) back.getScene().getWindow();
        //Create Scene with root element
        Scene scene = new Scene(root);
        //Set the new Scene on the Stage
        stage.setScene(scene);
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
     * ActionEvent Normal Open Button
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
        File f = chooser.showOpenDialog(mbopen.getScene().getWindow());
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
            Stage stage = (Stage) mbopen.getScene().getWindow();
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
     * ActionEvent Normal Save Button
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
            File f = chooser.showSaveDialog(mbsave.getScene().getWindow());
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
     * ActionEvent Normal SaveAs Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleSaveAsButtonAction(ActionEvent event) throws IOException {
        System.out.println("Save as ...");
        manager.setPath(null);
        manager.setFileName(null);
        handleSaveButtonAction(event);
    }


    /**
     * ActionEvent AES Open Button
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
        File f = chooser.showOpenDialog(mbopen.getScene().getWindow());
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
            Stage stage = (Stage) mbopen.getScene().getWindow();
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
            File f = chooser.showSaveDialog(mbsave.getScene().getWindow());
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


    /**
     * ActionEvent AES SaveAs Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleAESSaveAsButtonAction(ActionEvent event) throws IOException {
        System.out.println("Save as ...");
        manager.setPath(null);
        manager.setFileName(null);
        handleAESSaveButtonAction(event);
    }


    /**
     * ActionEvent DES Open Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleDESOpenButtonAction(ActionEvent event) throws IOException {
        System.out.println("Open DES");
        //Creating FileChooser
        FileChooser chooser = new FileChooser();
        //Filter only Txt Files
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        //Show Open Dialog
        File f = chooser.showOpenDialog(mbopen.getScene().getWindow());
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
            Stage stage = (Stage) mbopen.getScene().getWindow();
            //Create Scene with root element
            Scene scene = new Scene(root);
            //Get textArea from editor scene and load Content in text Area
            Node node = scene.lookup("#textArea");
            TextArea txta = (TextArea) node;
            txta.setText(manager.desDecryptText(f));
            //Set the new Scene on the Stage
            stage.setScene(scene);
        }
    }


    /**
     * ActionEvent DES Save Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleDESSaveButtonAction(ActionEvent event) throws IOException {
        System.out.println("DES");
        //If there is no currentPath
        if(manager.getPath()== null) {
            //Creating FileChooser
            FileChooser chooser = new FileChooser();
            //Filter only Txt Files
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            //Show Save Dialog
            File f = chooser.showSaveDialog(mbsave.getScene().getWindow());
            //If file is saved
            if (f != null) {
                //Save fileName
                manager.setFileName(f.getName());
                //Save currentPath
                manager.setPath(f.getPath());
                try {
                    //Save Document in Path
                    manager.desEncryptText(textArea.getText());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }else{
            //Save Document in Path
            manager.desEncryptText(textArea.getText());
        }
    }


    /**
     * ActionEvent DES SaveAs Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleDESSaveAsButtonAction(ActionEvent event) throws IOException {
        System.out.println("Save as ...");
        manager.setPath(null);
        manager.setFileName(null);
        handleDESSaveButtonAction(event);
    }

}