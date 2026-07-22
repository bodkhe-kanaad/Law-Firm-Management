package backend.controller;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JoinResultsController {

    @FXML private VBox root;
    @FXML private ComboBox<String> caseTypeField;
    @FXML private TableView<ObservableList<String>> resultTable;
    @FXML private Label statusLabel;

    private final LawyerController lawyerController = new LawyerController();

    @FXML
    public void initialize() {
        caseTypeField.setItems(FXCollections.observableArrayList("Family", "Criminal", "Corporate"));
    }

    // Column headers matching LawyerRepo.joinByCaseType() result order
    private static final String[] COLUMNS =
            {"case_id", "case_type", "opening_date", "closing_date", "client_name", "lawyer_name"};

    @FXML
    private void handleRunJoin() {
        String caseType = caseTypeField.getValue() == null ? "" : caseTypeField.getValue();

        if (caseType.isBlank()) {
            showError("Please enter a case type before running the query.");
            return;
        }

        List<List<String>> data = lawyerController.joinByCaseType(caseType);

        populateTable(data);

        if (data.isEmpty()) {
            showInfo("No cases found with case type \"" + caseType + "\".");
        } else {
            showSuccess("Found " + data.size() + " case" + (data.size() == 1 ? "" : "s")
                    + " with case type \"" + caseType + "\".");
        }
    }

    private void populateTable(List<List<String>> data) {
        resultTable.getColumns().clear();

        for (int i = 0; i < COLUMNS.length; i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(COLUMNS[i]);
            col.setCellValueFactory(cell ->
                    new javafx.beans.property.SimpleStringProperty(
                            cell.getValue().get(colIndex)));
            resultTable.getColumns().add(col);
        }

        ObservableList<ObservableList<String>> tableData = FXCollections.observableArrayList();
        for (List<String> row : data) {
            tableData.add(FXCollections.observableArrayList(row));
        }
        resultTable.setItems(tableData);
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/landing.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #cc0000; -fx-font-size: 12px;");
    }

    private void showInfo(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #cc6600; -fx-font-size: 12px;");
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #006600; -fx-font-size: 12px;");
    }
}
