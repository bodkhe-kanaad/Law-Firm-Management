package backend.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LandingController {

    @FXML private VBox root;

    @FXML
    private void handleGoToCrud() {
        navigate("/main.fxml");
    }

    @FXML
    private void handleGoToSelection() {
        navigate("/case_selection.fxml");
    }

    @FXML
    private void handleGoToProjection() {
        navigate("/invoice_projection.fxml");
    }

    @FXML
    private void handleGoToJoin() {
        navigate("/join_results.fxml");
    }

    @FXML
    private void handleGoToAnalytics() {
        navigate("/analytics.fxml");
    }

    private void navigate(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
