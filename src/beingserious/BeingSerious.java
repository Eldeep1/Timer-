package beingserious;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BeingSerious extends Application {
    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/HomePage/HomePage.fxml"));
        Scene scene = new Scene(root, Color.ORANGERED);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
