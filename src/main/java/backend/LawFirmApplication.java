package backend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LawFirmApplication extends Application {
    // Check the Database Connection File for setup.
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/landing.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        stage.setTitle("Legal Case Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}