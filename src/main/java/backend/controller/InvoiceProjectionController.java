package backend.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InvoiceProjectionController {

    @FXML private VBox root;

    @FXML private CheckBox invoiceIdCheck;
    @FXML private CheckBox amountCheck;
    @FXML private CheckBox caseIdCheck;
    @FXML private CheckBox statusCodeCheck;

    @FXML private ListView<String> selectedColumnsList;
    @FXML private TableView<ObservableList<String>> resultTable;
    @FXML private Label statusLabel;

    private InvoiceController controller = new InvoiceController();

    @FXML
    public void initialize() {
        invoiceIdCheck.setOnAction(e  -> updateSelection("invoice_id",  invoiceIdCheck.isSelected()));
        amountCheck.setOnAction(e     -> updateSelection("amount",      amountCheck.isSelected()));
        caseIdCheck.setOnAction(e     -> updateSelection("case_id",     caseIdCheck.isSelected()));
        statusCodeCheck.setOnAction(e -> updateSelection("status_code", statusCodeCheck.isSelected()));
    }

    private void updateSelection(String column, boolean selected) {
        if (selected) {
            if (!selectedColumnsList.getItems().contains(column)) {
                selectedColumnsList.getItems().add(column);
            }
        } else {
            selectedColumnsList.getItems().remove(column);
        }
    }

    @FXML
    private void handleMoveUp() {
        int idx = selectedColumnsList.getSelectionModel().getSelectedIndex();
        if (idx > 0) {
            String item = selectedColumnsList.getItems().remove(idx);
            selectedColumnsList.getItems().add(idx - 1, item);
            selectedColumnsList.getSelectionModel().select(idx - 1);
        }
    }

    @FXML
    private void handleMoveDown() {
        int idx = selectedColumnsList.getSelectionModel().getSelectedIndex();
        if (idx >= 0 && idx < selectedColumnsList.getItems().size() - 1) {
            String item = selectedColumnsList.getItems().remove(idx);
            selectedColumnsList.getItems().add(idx + 1, item);
            selectedColumnsList.getSelectionModel().select(idx + 1);
        }
    }

    @FXML
    private void runProjection() {
        List<String> columns = new ArrayList<>(selectedColumnsList.getItems());

        if (columns.isEmpty()) {
            showError("Please check at least one column before running the query.");
            return;
        }

        List<List<String>> data = controller.projectionQueryPage(columns);

        resultTable.getColumns().clear();

        for (int i = 0; i < columns.size(); i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns.get(i));
            column.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(
                            cellData.getValue().get(colIndex)));
            resultTable.getColumns().add(column);
        }

        ObservableList<ObservableList<String>> tableData = FXCollections.observableArrayList();
        for (List<String> row : data) {
            tableData.add(FXCollections.observableArrayList(row));
        }
        resultTable.setItems(tableData);

        if (data.isEmpty()) {
            showInfo("No invoices found.");
        } else {
            showSuccess("Showing " + data.size() + " invoice" + (data.size() == 1 ? "" : "s")
                    + " — " + columns.size() + " column" + (columns.size() == 1 ? "" : "s") + ".");
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

    // --- helpers ---

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
