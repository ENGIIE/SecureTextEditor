import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMLView extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws IOException {

        // Loading first fxml file to be displayed in parent (root)
        Parent root = FXMLLoader.load(getClass().getResource("keymenu.fxml"));

        // Create new scene with with parent (root)
        Scene scene = new Scene(root, 500, 500);

        // Set title scene and display window
        primaryStage.setTitle("FXML Welcome");
        primaryStage.setScene(scene);
        primaryStage.show();

    }


}
