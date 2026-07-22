package backend.controller;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AnalyticsController {

    @FXML private VBox root;
    @FXML private TableView<ObservableList<String>> resultTable;
    @FXML private Label statusLabel;
    @FXML private Label tableTitle;

    private final WorkLogController workLogController = new WorkLogController();
    private final LawyerController  lawyerController  = new LawyerController();
    private final HearingController  hearingController  = new HearingController();
    private final EvidenceController evidenceController = new EvidenceController();

    @FXML
    private void handleGroupBy() {
        List<List<String>> data = workLogController.totalHoursPerLawyer();
        String[] cols = {"lawyer_id", "name", "log_entries", "total_hours"};
        display("Total Hours per Lawyer", cols, data);
    }

    @FXML
    private void handleHaving() {
        List<List<String>> data = workLogController.lawyersWithMultipleCases();
        String[] cols = {"lawyer_id", "name", "cases_worked"};
        display("Lawyers on Multiple Cases", cols, data);
    }

    @FXML
    private void handleNestedAggregation() {
        List<List<String>> data = workLogController.lawyersAboveAverageHours();
        String[] cols = {"lawyer_id", "name", "total_hours"};
        display("Lawyers Above Average Hours", cols, data);
    }

    @FXML
    private void handleDivision() {
        List<List<String>> data = lawyerController.divisionLawyersAllCasesLogged();
        String[] cols = {"lawyer_id", "name"};
        display("Lawyers with Work Logged on Every Assigned Case", cols, data);
    }

    @FXML
    private void handleHearingSchedule() {
        List<List<String>> data = hearingController.hearingSchedule();
        String[] cols = {"case_id", "case_type", "hearing_date", "judge_name", "city", "province"};
        display("Hearing Schedule", cols, data);
    }

    @FXML
    private void handleEvidencePerCase() {
        List<List<String>> data = evidenceController.evidencePerCase();
        String[] cols = {"evidence_id", "evidence_type", "stored_in", "case_id", "hearing_date"};
        display("Evidence per Case", cols, data);
    }

    private void display(String title, String[] cols, List<List<String>> data) {
        tableTitle.setText(title);
        resultTable.getColumns().clear();

        for (int i = 0; i < cols.length; i++) {
            final int idx = i;
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(cols[i]);
            col.setCellValueFactory(cell ->
                    new javafx.beans.property.SimpleStringProperty(
                            cell.getValue().get(idx)));
            resultTable.getColumns().add(col);
        }

        ObservableList<ObservableList<String>> tableData = FXCollections.observableArrayList();
        for (List<String> row : data) {
            tableData.add(FXCollections.observableArrayList(row));
        }
        resultTable.setItems(tableData);

        if (data.isEmpty()) {
            showInfo("No results returned.");
        } else {
            showSuccess("Showing " + data.size() + " row" + (data.size() == 1 ? "" : "s") + ".");
        }
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

    private void showInfo(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #cc6600; -fx-font-size: 12px;");
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #006600; -fx-font-size: 12px;");
    }
}
