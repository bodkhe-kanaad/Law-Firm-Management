package backend.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.OptionalInt;

import backend.db.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainController {

    @FXML private TableView<ObservableList<String>> caseTable;
    @FXML private TableColumn<ObservableList<String>, String> colCaseId;
    @FXML private TableColumn<ObservableList<String>, String> colType;
    @FXML private TableColumn<ObservableList<String>, String> colClientId;
    @FXML private TableColumn<ObservableList<String>, String> colLawyerId;

    @FXML private TextField insertCaseId, insertType, insertClientId, insertLawyerId;
    @FXML private TextField updateCaseId, updateNewType, updateNewClientId, updateNewLawyerId;
    @FXML private TextField deleteCaseId;

    @FXML private Label insertMsg, updateMsg, deleteMsg;

    private Connection conn = DatabaseConnection.getConnection();

    @FXML
    public void initialize() {
        conn = DatabaseConnection.getConnection();

        if (conn == null) {
            showError(insertMsg, "Database connection failed.");
            showError(updateMsg, "Database connection failed.");
            showError(deleteMsg, "Database connection failed.");
            return;
        }

        colCaseId.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().get(0)));
        colType.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().get(1)));
        colClientId.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().get(2)));
        colLawyerId.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().get(3)));

        // Clicking a row pre-fills the update Case ID field
        caseTable.getSelectionModel().selectedItemProperty().addListener((obs, oldRow, newRow) -> {
            if (newRow != null) {
                updateCaseId.setText(newRow.get(0));
            }
        });

        loadTable();
    }

    private void loadTable() {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        try {
            System.out.println("Loading Court_Case table...");

            Statement debugStmt = conn.createStatement();

            ResultSet userRs = debugStmt.executeQuery("SELECT USER FROM dual");
            if (userRs.next()) {
                System.out.println("Java connected as user: " + userRs.getString(1));
            }

            ResultSet countRs = debugStmt.executeQuery("SELECT COUNT(*) FROM Court_Case");
            if (countRs.next()) {
                System.out.println("Java sees Court_Case row count: " + countRs.getInt(1));
            }

            ResultSet rs = debugStmt.executeQuery(
                "SELECT case_id, case_type, client_id, lawyer_id FROM Court_Case"
            );

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(String.valueOf(rs.getInt("case_id")));
                row.add(rs.getString("case_type"));
                row.add(String.valueOf(rs.getInt("client_id")));
                row.add(String.valueOf(rs.getInt("lawyer_id")));
                data.add(row);
                System.out.println("Loaded row: " + row);
            }

            System.out.println("Total rows loaded: " + data.size());

        } catch (SQLException e) {
            System.err.println("Error loading table: " + e.getMessage());
            e.printStackTrace();
        }

        caseTable.setItems(data);
    }

    @FXML
    private void handleInsert() {
        String caseIdText   = insertCaseId.getText().trim();
        String type         = insertType.getText().trim();
        String clientIdText = insertClientId.getText().trim();
        String lawyerIdText = insertLawyerId.getText().trim();

        if (caseIdText.isBlank())   { showError(insertMsg, "Case ID is required.");   return; }
        if (type.isBlank())         { showError(insertMsg, "Case Type is required."); return; }
        if (clientIdText.isBlank()) { showError(insertMsg, "Client ID is required."); return; }
        if (lawyerIdText.isBlank()) { showError(insertMsg, "Lawyer ID is required."); return; }

        OptionalInt caseId   = parseInt(caseIdText);
        OptionalInt clientId = parseInt(clientIdText);
        OptionalInt lawyerId = parseInt(lawyerIdText);

        if (caseId.isEmpty())   { showError(insertMsg, "Case ID must be a whole number.");   return; }
        if (clientId.isEmpty()) { showError(insertMsg, "Client ID must be a whole number."); return; }
        if (lawyerId.isEmpty()) { showError(insertMsg, "Lawyer ID must be a whole number."); return; }

        try {
            conn.setAutoCommit(true);
            PreparedStatement stm = conn.prepareStatement(
                "INSERT INTO Court_Case (case_id, case_type, client_id, lawyer_id, opening_date) " +
                "VALUES (?, ?, ?, ?, SYSDATE)"
            );
            stm.setInt(1, caseId.getAsInt());
            stm.setString(2, type);
            stm.setInt(3, clientId.getAsInt());
            stm.setInt(4, lawyerId.getAsInt());
            stm.executeUpdate();
            showSuccess(insertMsg, "Case " + caseId.getAsInt() + " inserted successfully.");
            insertCaseId.clear(); insertType.clear(); insertClientId.clear(); insertLawyerId.clear();
            loadTable();
        } catch (SQLException e) {
            showError(insertMsg, friendlySqlError(e));
        }
    }

    @FXML
    private void handleUpdate() {
        String caseIdText    = updateCaseId.getText().trim();
        String newType       = updateNewType.getText().trim();
        String newClientText = updateNewClientId.getText().trim();
        String newLawyerText = updateNewLawyerId.getText().trim();

        if (caseIdText.isBlank()) {
            showError(updateMsg, "Case ID is required.");
            return;
        }
        OptionalInt caseId = parseInt(caseIdText);
        if (caseId.isEmpty()) {
            showError(updateMsg, "Case ID must be a whole number.");
            return;
        }

        // Validate optional integer fields before touching the DB
        OptionalInt newClientId = OptionalInt.empty();
        OptionalInt newLawyerId = OptionalInt.empty();
        if (!newClientText.isBlank()) {
            newClientId = parseInt(newClientText);
            if (newClientId.isEmpty()) {
                showError(updateMsg, "New Client ID must be a whole number.");
                return;
            }
        }
        if (!newLawyerText.isBlank()) {
            newLawyerId = parseInt(newLawyerText);
            if (newLawyerId.isEmpty()) {
                showError(updateMsg, "New Lawyer ID must be a whole number.");
                return;
            }
        }

        // Build SET clause dynamically — only include fields the user filled in
        StringBuilder setClauses = new StringBuilder();
        if (!newType.isBlank())          { setClauses.append("case_type = ?, "); }
        if (newClientId.isPresent())     { setClauses.append("client_id = ?, "); }
        if (newLawyerId.isPresent())     { setClauses.append("lawyer_id = ?, "); }

        if (setClauses.length() == 0) {
            showError(updateMsg, "Please fill in at least one field to update (Type, Client ID, or Lawyer ID).");
            return;
        }

        // Strip trailing ", "
        String setClause = setClauses.substring(0, setClauses.length() - 2);
        String sql = "UPDATE Court_Case SET " + setClause + " WHERE case_id = ?";

        try {
            conn.setAutoCommit(true);
            PreparedStatement stm = conn.prepareStatement(sql);

            int paramIndex = 1;
            if (!newType.isBlank())      { stm.setString(paramIndex++, newType); }
            if (newClientId.isPresent()) { stm.setInt(paramIndex++, newClientId.getAsInt()); }
            if (newLawyerId.isPresent()) { stm.setInt(paramIndex++, newLawyerId.getAsInt()); }
            stm.setInt(paramIndex, caseId.getAsInt());

            int rows = stm.executeUpdate();
            if (rows == 0) {
                showError(updateMsg, "No case found with ID " + caseId.getAsInt() + ".");
            } else {
                showSuccess(updateMsg, "Case " + caseId.getAsInt() + " updated successfully.");
                updateCaseId.clear(); updateNewType.clear();
                updateNewClientId.clear(); updateNewLawyerId.clear();
                loadTable();
            }
        } catch (SQLException e) {
            showError(updateMsg, friendlySqlError(e));
        }
    }

    @FXML
    private void handleDelete() {
        String caseIdText = deleteCaseId.getText().trim();

        if (caseIdText.isBlank()) { showError(deleteMsg, "Case ID is required."); return; }

        OptionalInt caseId = parseInt(caseIdText);
        if (caseId.isEmpty()) { showError(deleteMsg, "Case ID must be a whole number."); return; }

        try {
            conn.setAutoCommit(true);
            PreparedStatement stm = conn.prepareStatement(
                "DELETE FROM Court_Case WHERE case_id = ?"
            );
            stm.setInt(1, caseId.getAsInt());
            int rows = stm.executeUpdate();
            if (rows == 0) {
                showError(deleteMsg, "No case found with ID " + caseId.getAsInt() + ".");
            } else {
                showSuccess(deleteMsg, "Case " + caseId.getAsInt() + " deleted successfully.");
                deleteCaseId.clear();
                loadTable();
            }
        } catch (SQLException e) {
            showError(deleteMsg, friendlySqlError(e));
        }
    }

    @FXML
    private void handleRefresh() {
        loadTable();
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/landing.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) caseTable.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- helpers ---

    private OptionalInt parseInt(String text) {
        try { return OptionalInt.of(Integer.parseInt(text)); }
        catch (NumberFormatException e) { return OptionalInt.empty(); }
    }

    private void showError(Label label, String message) {
        label.setText(message);
        label.setStyle("-fx-text-fill: #cc0000; -fx-font-size: 12px;");
    }

    private void showSuccess(Label label, String message) {
        label.setText(message);
        label.setStyle("-fx-text-fill: #006600; -fx-font-size: 12px;");
    }

    private String friendlySqlError(SQLException e) {
        String msg = e.getMessage();
        if (msg != null) {
            if (msg.contains("ORA-00001")) return "A case with that ID already exists.";
            if (msg.contains("ORA-02291")) return "Client ID or Lawyer ID does not exist in the system.";
            if (msg.contains("ORA-01400")) return "A required field is missing a value.";
            if (msg.contains("ORA-02292")) return "Cannot delete — this case is referenced by other records.";
        }
        return "Database error: " + (msg != null ? msg.split("\n")[0] : "unknown");
    }
}
