package backend.controller;

import java.util.ArrayList;
import java.util.List;

import backend.model.SelectionCondition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CaseSelectionController {

    @FXML private VBox conditionsContainer;
    @FXML private TableView<ObservableList<String>> resultTable;
    @FXML private Label statusLabel;

    private final CaseController controller = new CaseController();

    private final List<String> attributes = List.of(
            "case_id", "case_type", "client_id", "lawyer_id", "is_successful", "closing_date"
    );
    private final List<String> operators = List.of(
            "=", "<", ">", "<=", ">=", "<>", "LIKE", "IS NULL", "IS NOT NULL"
    );
    private final List<String> connectors = List.of("AND", "OR");

    @FXML
    public void initialize() {
        addConditionRow(false, "AND");
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/landing.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) conditionsContainer.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            showError("Could not return to main screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddAnd() {
        addConditionRow(true, "AND");
    }

    @FXML
    private void handleAddOr() {
        addConditionRow(true, "OR");
    }

    @FXML
    private void handleRunSelection() {
        List<SelectionCondition> conditions = new ArrayList<>();

        for (int i = 0; i < conditionsContainer.getChildren().size(); i++) {
            HBox row = (HBox) conditionsContainer.getChildren().get(i);

            int offset = 0;
            String connector = null;

            if (i > 0) {
                ComboBox<String> connectorBox = (ComboBox<String>) row.getChildren().get(0);
                connector = connectorBox.getValue();
                offset = 1;
                if (connector == null) {
                    showError("Condition " + (i + 1) + ": please select AND or OR.");
                    return;
                }
            }

            ComboBox<String> attrBox = (ComboBox<String>) row.getChildren().get(offset);
            ComboBox<String> opBox   = (ComboBox<String>) row.getChildren().get(offset + 1);
            TextField valueField     = (TextField)        row.getChildren().get(offset + 2);

            String attribute = attrBox.getValue();
            String operator  = opBox.getValue();
            String value     = valueField.getText() == null ? "" : valueField.getText().trim();

            if (attribute == null) {
                showError("Condition " + (i + 1) + ": please choose an attribute.");
                return;
            }
            if (operator == null) {
                showError("Condition " + (i + 1) + ": please choose an operator.");
                return;
            }
            if (!(operator.equals("IS NULL") || operator.equals("IS NOT NULL")) && value.isBlank()) {
                showError("Condition " + (i + 1) + ": please enter a value for \"" + attribute + " " + operator + "\".");
                return;
            }

            conditions.add(new SelectionCondition(connector, attribute, operator, value));
        }

        List<List<String>> data = controller.selectionQuery(conditions);

        populateTable(data);

        if (data.isEmpty()) {
            showInfo("No matching court cases found.");
        } else {
            showSuccess("Found " + data.size() + " court case" + (data.size() == 1 ? "" : "s") + ".");
        }
    }

    private void addConditionRow(boolean includeConnector, String defaultConnector) {
        HBox row = new HBox(10);
        row.setPadding(new Insets(4));

        if (includeConnector) {
            ComboBox<String> connectorBox = new ComboBox<>();
            connectorBox.setItems(FXCollections.observableArrayList(connectors));
            connectorBox.setValue(defaultConnector);
            connectorBox.setPrefWidth(90);
            row.getChildren().add(connectorBox);
        }

        ComboBox<String> attrBox = new ComboBox<>();
        attrBox.setItems(FXCollections.observableArrayList(attributes));
        attrBox.setPromptText("Attribute");

        ComboBox<String> opBox = new ComboBox<>();
        opBox.setItems(FXCollections.observableArrayList(operators));
        opBox.setPromptText("Operator");

        TextField valueField = new TextField();
        valueField.setPromptText("Value");

        attrBox.valueProperty().addListener((obs, oldAttr, newAttr) -> {
            if (newAttr == null) return;
            switch (newAttr) {
                case "case_id"       -> valueField.setPromptText("e.g. 1001");
                case "case_type"     -> valueField.setPromptText("e.g. Family");
                case "client_id"     -> valueField.setPromptText("e.g. 1");
                case "lawyer_id"     -> valueField.setPromptText("e.g. 101");
                case "is_successful" -> valueField.setPromptText("e.g. 0");
                case "closing_date"  -> valueField.setPromptText("e.g. 2025-03-01");
                default              -> valueField.setPromptText("Value");
            }
        });

        opBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("IS NULL".equals(newVal) || "IS NOT NULL".equals(newVal)) {
                valueField.clear();
                valueField.setDisable(true);
            } else {
                valueField.setDisable(false);
            }
        });

        Button removeBtn = new Button("Remove");
        removeBtn.setOnAction(e -> {
            conditionsContainer.getChildren().remove(row);
            if (conditionsContainer.getChildren().isEmpty()) {
                addConditionRow(false, "AND");
            }
        });

        row.getChildren().addAll(attrBox, opBox, valueField, removeBtn);
        conditionsContainer.getChildren().add(row);
    }

    private void populateTable(List<List<String>> data) {
        resultTable.getColumns().clear();

        String[] cols = {"case_id", "case_type", "client_id", "lawyer_id"};

        for (int i = 0; i < cols.length; i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(cols[i]);
            column.setCellValueFactory(cell ->
                    new javafx.beans.property.SimpleStringProperty(cell.getValue().get(colIndex)));
            resultTable.getColumns().add(column);
        }

        ObservableList<ObservableList<String>> tableData = FXCollections.observableArrayList();
        for (List<String> row : data) {
            tableData.add(FXCollections.observableArrayList(row));
        }
        resultTable.setItems(tableData);
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
