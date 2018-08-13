import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;


public class FXMLController {

    /**
     * Create FileManager Object
     */
    private static FileManager fileManager = new FileManager();
    private static Settings settingoptions = new Settings();
    private static SettingsManager settingsManager = new SettingsManager();

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
    private Button nkFile;
    @FXML
    private Button okFile;
    @FXML
    private TextArea textArea;
    @FXML
    private Button apply;
    @FXML
    private Button settings;
    @FXML
    private CheckBox cbpbe;
    @FXML
    private PasswordField pfpbe;
    @FXML
    private ComboBox cbalgo;
    @FXML
    private ComboBox keysize;
    @FXML
    private ComboBox cbmode;
    @FXML
    private ComboBox cbpad;
    @FXML
    private ComboBox cbhash;
    @FXML
    private CheckBox chbhash;



    //FXML Action Events

    /**
     * ActionEvent Settings Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleSettingsButtonAction(ActionEvent event) throws IOException, SAXException {
        System.out.println("Settings");
        //Load fxml file in Parent root
        Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
        //New Stage
        Stage stage = new Stage ();
        stage.initModality(Modality.WINDOW_MODAL);
        //Create Scene with root element
        Scene scene = new Scene(root);


        try {
            settingsManager.setDocument("./src/main/java/Settingsdata.xml");
            Document document1 = settingsManager.getDocument();


            //ADD PBE Checkbox
            Node node = scene.lookup("#cbpbe");
            CheckBox cbpbe = (CheckBox) node;
            cbpbe.setSelected(fileManager.getData().getPbe());

            //ADD PBE Passwordfield
            node = scene.lookup("#pfpbe");
            PasswordField pfpbe = (PasswordField) node;
            if(!cbpbe.isSelected()){pfpbe.setDisable(true);}else{pfpbe.setDisable(false);}

            //ADD Algorithms to ComboBox
            node = scene.lookup("#cbalgo");
            ComboBox cba = (ComboBox) node;
            settingsManager.addAllElements("algorithm", cba, fileManager.getData().getAlgo(), "name");
            cba.getSelectionModel().select(fileManager.getData().getAlgo());

            //ADD Keysizes to ComboBox
            node = scene.lookup("#keysize");
            ComboBox ks = (ComboBox) node;
            settingsManager.addAllElements("keysize", ks, fileManager.getData().getKeysize(),"name");
            ks.getSelectionModel().select( settingsManager.getIndex("keysize", fileManager.getData().getKeysize()));

            //ADD Modes to ComboBox
            node = scene.lookup("#cbmode");
            ComboBox cbm = (ComboBox) node;
            settingsManager.addAllElements("mode", cbm, fileManager.getData().getPad(), "name");
            cbm.getSelectionModel().select(fileManager.getData().getMode());

            //ADD Paddings to ComboBox
            node = scene.lookup("#cbpad");
            ComboBox cbp = (ComboBox) node;
            settingsManager.addAllElements("padding", cbp, fileManager.getData().getPad(),"name");
            cbp.getSelectionModel().select(fileManager.getData().getPad());

            //ADD HMACS Checkbox
            node = scene.lookup("#chbhash");
            CheckBox chbh = (CheckBox) node;
            chbh.setSelected(fileManager.getData().getHMAC());


            //ADD Hashfunctions to ComboBox
            node = scene.lookup("#cbhash");
            ComboBox cbh = (ComboBox) node;
            settingsManager.addAllElements("hashfunction", cbh, fileManager.getData().getHash(),"name");
            cbh.getSelectionModel().select(fileManager.getData().getHash());

            //HMACCheckbox initial
            if(chbh.isSelected() == true){
                cbh.getItems().clear();
                settingsManager.getElementsByBoolean("hashfunction", "hmac", cbh);
                System.out.println("HMAC NAME: " +settingsManager.getElementName("hashfunction", fileManager.getData().getHash()));
                int index=0;
                for(int i = 0; i<cbh.getItems().size();i++){
                    if(cbh.getItems().get(i).toString() == settingsManager.getElementName("hashfunction", fileManager.getData().getHash())){
                        index = i;
                    }
                }
                cbh.getSelectionModel().select(index);
            }

            //HMAC Checkbox Listener
            chbh.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    System.out.println(cbpbe.isSelected());
                    if(chbh.isSelected() == true){
                        cbh.getItems().clear();
                        settingsManager.getElementsByBoolean("hashfunction", "hmac", cbh);
                        cbh.getSelectionModel().select(fileManager.getData().getHash());

                    }else {
                        cbh.getItems().clear();
                        settingsManager.addAllElements("hashfunction", cbh, fileManager.getData().getHash(),"name");
                        cbh.getSelectionModel().select(0);
                    }
                    System.out.println("Changed");
                }
            });


            //PBECheckbox initial
            if(cbpbe.isSelected() == true){
                pfpbe.setDisable(false);
                cba.getItems().clear();
                settingsManager.getElementsByBoolean("algorithm", "pbe", cba);
                cba.getSelectionModel().select(fileManager.getData().getAlgo());

            }

            //PBE Checkbox Listener
            cbpbe.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    System.out.println(cbpbe.isSelected());
                    if(cbpbe.isSelected() == true){
                        pfpbe.setDisable(false);
                        cba.getItems().clear();
                        settingsManager.getElementsByBoolean("algorithm", "pbe", cba);
                        cba.getSelectionModel().select(fileManager.getData().getAlgo());

                    }else {
                        pfpbe.setDisable(true);
                        cba.getItems().clear();
                        settingsManager.addAllElements("algorithm", cba, fileManager.getData().getAlgo(),"name");
                        cba.getSelectionModel().select(0);
                    }
                    System.out.println("Changed");
                }
            });


            //Algorithm initial
            //Modes
            int algid = -1;
            algid = fileManager.getData().getAlgo();
            for(int i=0; i<cba.getItems().size(); i++){
             if(cba.getItems().get(i).toString() == settingsManager.getElementName("algorithm", algid)){
                 cba.getSelectionModel().select(i);
             }
            }
            System.out.println("AlgoID:"+algid);
            cbm.getItems().clear();
            settingsManager.addSubelements("algorithm", algid, "mode", "modeid",cbm);
            cbm.getSelectionModel().select(fileManager.getData().getMode());

            //Keysize
            ks.getItems().clear();
            settingsManager.addSubelements("algorithm", algid, "keysize", "keyid", ks);
            int indx = 0;
            indx = settingsManager.getIndex("keysize", fileManager.getData().getKeysize());
            ks.getSelectionModel().select(indx);

            //Algorithm Listener
            cba.valueProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue == null) {

                } else {

                    //Modes
                    int algoid = -1;
                    algoid = settingsManager.getIndex("algorithm",  cba.getSelectionModel().getSelectedItem().toString());
                    System.out.println("AlgoID:"+algoid);
                    cbm.getItems().clear();
                    settingsManager.addSubelements("algorithm", algoid, "mode", "modeid",cbm);
                    cbm.getSelectionModel().select(fileManager.getData().getMode());


                    //Keysize
                    ks.getItems().clear();
                    settingsManager.addSubelements("algorithm", algoid, "keysize", "keyid", ks);
                    int index = 0;
                    index = settingsManager.getIndex("keysize", fileManager.getData().getKeysize());
                    ks.getSelectionModel().select(index);

                }
            });


            //Mode initial
            System.out.println("else");
            int modid = cbm.getSelectionModel().getSelectedIndex();
            System.out.println("ModeID: "+modid);
            cbp.getItems().clear();
            if(modid != -1) {
                settingsManager.addSubelements("mode", modid, "padding", "paddingid", cbp);
            }
            cbp.getSelectionModel().select(fileManager.getData().getPad());

            //Mode Listener
            cbm.valueProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue == null) {
                    System.out.println("if");
                    System.out.println(cbm.getItems().isEmpty());
                    if(cbm.getItems().isEmpty()) {
                        cbp.getItems().clear();
                    }

                } else {
                    System.out.println("else");
                    int modeid = cbm.getSelectionModel().getSelectedIndex();
                    System.out.println("ModeID: "+modeid);
                    cbp.getItems().clear();
                    settingsManager.addSubelements("mode", modeid, "padding", "paddingid",cbp);
                    cbp.getSelectionModel().select(fileManager.getData().getPad());
                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }


        //Set the new Scene on the Stage
        stage.setScene(scene);

        stage.show();


    }

    /**
     * ActionEvent Back Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleApplyButtonAction(ActionEvent event) throws IOException {
        System.out.println("Apply");

        fileManager.getData().setPbe(cbpbe.isSelected());
        System.out.println("PBE: "+fileManager.getData().getPbe());
        if(cbpbe.isSelected()) {
            fileManager.setPassword(pfpbe.getText());
        }
        System.out.println("PASSWORD: "+pfpbe.getText());
        fileManager.getData().setAlgo(settingsManager.getIndex("algorithm", cbalgo.getSelectionModel().getSelectedItem().toString()));
        System.out.println("ALGO: "+fileManager.getData().getAlgo());
        if(!keysize.getItems().isEmpty()){
            System.out.println("KEYSIZE: "+Integer.parseInt(keysize.getSelectionModel().getSelectedItem().toString()));
            fileManager.getData().setKeysize(Integer.parseInt(keysize.getSelectionModel().getSelectedItem().toString()));
        }else {
            fileManager.getData().setKeysize(0);
        }
        int algoid = settingsManager.getIndex("algorithm", cbalgo.getSelectionModel().getSelectedItem().toString());
        fileManager.getData().setBlocksize(settingsManager.getBlocksize("algorithm", algoid));
        if(!cbmode.getItems().isEmpty()) {
            fileManager.getData().setMode(settingsManager.getIndex("mode", cbmode.getSelectionModel().getSelectedItem().toString()));
        } else {
            fileManager.getData().setMode(-1);
        }
        System.out.println("MODE: "+fileManager.getData().getMode());
        if(!cbpad.getItems().isEmpty()) {
            fileManager.getData().setPad(settingsManager.getIndex("padding", cbpad.getSelectionModel().getSelectedItem().toString()));
        }else {
            fileManager.getData().setPad(-1);
        }
        System.out.println("PAD: "+fileManager.getData().pad);
        fileManager.getData().setHMAC(chbhash.isSelected());
        System.out.println("HMAC: "+fileManager.getData().getHMAC());
        fileManager.getData().setHash(settingsManager.getIndex("hashfunction", cbhash.getSelectionModel().getSelectedItem().toString()));
        System.out.println("HASH: "+fileManager.getData().getHash());
        Stage stage = (Stage) apply.getScene().getWindow();
        stage.close();

    }

    /**
     * ActionEvent NewFile Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleNewFileButtonAction(ActionEvent event) throws IOException, SAXException {
        System.out.println("New File");
        //Set boolean nFile true
        fileManager.setPath(null);
        fileManager.setFileName(null);
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
     * ActionEvent NewKeyFile Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleNewKeyFileButtonAction(ActionEvent event) throws IOException, SAXException {
        System.out.println("New Key File");
        //Set boolean nFile true

        //Creating FileChooser
        FileChooser chooser = new FileChooser();
        //Filter only Txt Files
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        //Show Save Dialog
        File f = chooser.showSaveDialog(nkFile.getScene().getWindow());
        if (f != null) {
            //Save fileName
            fileManager.setKeyFileName(f.getName());
            System.out.println(f.getName());

            //Save currentPath
            fileManager.setKeyPath(f.getPath());
            System.out.println(f.getPath());

            try {
                //Save Document in Path
                String text= fileManager.getKeyFileText(); // fileManager.getKeyFileText();
                System.out.println(text);
                fileManager.saveKeyText(text);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        //Load fxml file in Parent root
        Parent root = FXMLLoader.load(getClass().getResource("startmenu.fxml"));
        //Get current Stage
        Stage stage = (Stage) nkFile.getScene().getWindow();
        //Create Scene with root element
        Scene scene = new Scene(root);
        //Set the new Scene on the Stage
        stage.setScene(scene);
    }


    /**
     * ActionEvent OpenKeyFile Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleOpenKeyFileButtonAction(ActionEvent event) throws IOException, SAXException {
        System.out.println("Open Key File");
        //Creating FileChooser
        FileChooser chooser = new FileChooser();
        //Filter only Txt Files
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        //Show Open Dialog
        File f = chooser.showOpenDialog(okFile.getScene().getWindow());
        //Check if an Document was found
        if (f != null) {
            //Read filename into String and Set it
            String filename = null;
            int pos = f.getName().lastIndexOf(".");
            if (pos != -1) {
                filename = f.getName().substring(0, pos);
            }
            fileManager.setKeyFileName(filename);
            //Save CurrentPath of file
            fileManager.setKeyPath(f.getPath());
            //Load fxml file in Parent root
            Parent root = FXMLLoader.load(getClass().getResource("startmenu.fxml"));
            //Get current Stage
            Stage stage = (Stage) okFile.getScene().getWindow();
            //Create Scene with root element
            Scene scene = new Scene(root);
            //Set the new Scene on the Stage
            stage.setScene(scene);
        }
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
            fileManager.setFileName(filename);
            //Save CurrentPath of file
            fileManager.setPath(f.getPath());
            //Load fxml file in Parent root
            Parent root = FXMLLoader.load(getClass().getResource("editor.fxml"));
            //Get current Stage
            Stage stage = (Stage) mbopen.getScene().getWindow();
            //Create Scene with root element
            Scene scene = new Scene(root);
            //Get textArea from editor scene and load Content in text Area
            Node node = scene.lookup("#textArea");
            TextArea txta = (TextArea) node;
            txta.setText(fileManager.loadText(f));
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
        if(fileManager.getPath()== null) {
            //Creating FileChooser
            FileChooser chooser = new FileChooser();
            //Filter only Txt Files
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            //Show Save Dialog
            File f = chooser.showSaveDialog(mbsave.getScene().getWindow());
            //If file is saved
            if (f != null) {
                //Save fileName
                fileManager.setFileName(f.getName());
                //Save currentPath
                fileManager.setPath(f.getPath());
                try {
                    //Save Document in Path
                    fileManager.saveText(textArea.getText());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }else{
            //Save Document in Path
            fileManager.saveText(textArea.getText());
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
        fileManager.setPath(null);
        fileManager.setFileName(null);
        handleSaveButtonAction(event);
    }


    /**
     * ActionEvent DEC Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleDECButtonAction(ActionEvent event) throws IOException {
        System.out.println("Open DEC");

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
            fileManager.setFileName(filename);
            //Save CurrentPath of file
            fileManager.setPath(f.getPath());
            //Load fxml file in Parent root
            Parent root = FXMLLoader.load(getClass().getResource("editor.fxml"));
            //Get current Stage
            Stage stage = (Stage) mbopen.getScene().getWindow();
            //Create Scene with root element
            Scene scene = new Scene(root);
            //Get textArea from editor scene and load Content in text Area
            Node node = scene.lookup("#textArea");
            TextArea txta = (TextArea) node;
            txta.setText(fileManager.decryptText(f));
            //Set the new Scene on the Stage
            stage.setScene(scene);
        }
    }


    /**
     * ActionEvent ENC Save Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleENCSaveButtonAction(ActionEvent event) throws IOException {
        System.out.println("ENC");
        //If there is no currentPath
        if(fileManager.getPath()== null) {
            //Creating FileChooser
            FileChooser chooser = new FileChooser();
            //Filter only Txt Files
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            //Show Save Dialog
            File f = chooser.showSaveDialog(mbsave.getScene().getWindow());
            //If file is saved
            if (f != null) {
                //Save fileName
                fileManager.setFileName(f.getName());
                //Save currentPath
                fileManager.setPath(f.getPath());
                try {
                    //Save Document in Path
                    fileManager.encryptText(textArea.getText());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }else{
            //Save Document in Path
            fileManager.encryptText(textArea.getText());
        }
    }


    /**
     * ActionEvent ENC SaveAs Button
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleENCSaveAsButtonAction(ActionEvent event) throws IOException {
        System.out.println("ENC Save as ...");
        fileManager.setPath(null);
        fileManager.setFileName(null);
        handleENCSaveButtonAction(event);
    }






}