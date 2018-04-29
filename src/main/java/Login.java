import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;


public class Login extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        //Pane
        GridPane gpane = new GridPane();
        gpane.setAlignment(Pos.CENTER);
        gpane.setHgap(10);
        gpane.setVgap(10);
        gpane.setPadding(new Insets(25,25,25,25));


        // Scene Items

        //Text

        Text headerText = new Text("Welcome");
        headerText.setFont(Font.font("Calibri", FontWeight.NORMAL, 40));
        gpane.add(headerText,1,0,1, 1);

        //Username

        Label labelusername = new Label("Username:");
        gpane.add(labelusername,1,1,1,1);

        TextField tfusername =  new TextField();
        gpane.add(tfusername,2,1,1,1);


        //Passwort

        Label labelpassword = new Label("Password:");
        gpane.add(labelpassword,1,2,1,1);

        PasswordField pfpassword = new PasswordField();
        gpane.add(pfpassword,2,2,1,1);


        //HBox Login Register
        HBox formControls = new HBox(10);
        formControls.setAlignment(Pos.BOTTOM_RIGHT);

        Button login = new Button("Login");
        formControls.getChildren().add(login);

        Button register = new Button("Register");
        formControls.getChildren().add(register);

        gpane.add(formControls,2,3,1,1);


        // Group and Scene
        //Group root = new Group();
        Scene scene = new Scene(gpane, 500, 500);


        //Primary Stage
        primaryStage.setTitle("Secure Text Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
