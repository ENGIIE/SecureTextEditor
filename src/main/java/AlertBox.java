import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class AlertBox {

    static char[] password;


    public static void display(String title, String message) {

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setText(message);
        Button applyButton = new Button("Apply");
        //applyButton.setOnAction(e -> window.close());

        applyButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                System.out.println("PASSWORD FIELD"+passwordField.getText());
                password = passwordField.getText().toCharArray();
                window.close();
            }
        });


        VBox layout = new VBox(10);
        layout.getChildren().addAll(passwordField, applyButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

    }

}