package backend.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import backend.db.DatabaseConnection;
import backend.model.CaseFile;
import backend.model.SelectionCondition;

/*
* This layer directly interacts with the database.
* All SQL queries are written here.
*/
public class CaseRepo {
    
    public List<List<String>> selectionQuery(List<SelectionCondition> conditions) {
        List<List<String>> results = new ArrayList<>();

        if (conditions == null || conditions.isEmpty()) { // The query is empty
            return results;
        }

        Connection conn = DatabaseConnection.getConnection(); // Connection to the DB

        if (conn == null) {
            System.out.println("Database connection is null."); // Error message if connection fails
            return results;
        }

        StringBuilder query = new StringBuilder( // Building the query
                "SELECT case_id, case_type, client_id, lawyer_id FROM Court_Case WHERE "
        );

        for (int i = 0; i < conditions.size(); i++) {
            SelectionCondition c = conditions.get(i); 

            if (i > 0) {    // Check the connector for non first condition
                query.append(" ").append(c.getConnector()).append(" "); 
            }

            String attr = c.getAttribute(); 
            String op = c.getOperator().toUpperCase(); // to uppercase for query

            if (op.equals("IS NULL") || op.equals("IS NOT NULL")) {
                query.append(attr).append(" ").append(op);
            } else {
                query.append(attr).append(" ").append(op).append(" ?");
            }
        }

        String debugQuery = buildDebugQuery(query.toString(), conditions); // print the query for easier debugging
        System.out.println("Running selection query: " + debugQuery);

        try (PreparedStatement stm = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;

            for (SelectionCondition c : conditions) {
                String attr = c.getAttribute();
                String op = c.getOperator().toUpperCase();
                String value = c.getValue();

                if (op.equals("IS NULL") || op.equals("IS NOT NULL")) {
                    continue;
                }

                if (isNumericAttribute(attr)) {
                    stm.setInt(paramIndex++, Integer.parseInt(value));
                } else {
                    stm.setString(paramIndex++, value);
                }
            }

            // loop to make the query result
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    List<String> row = new ArrayList<>();
                    row.add(String.valueOf(rs.getInt("case_id")));
                    row.add(rs.getString("case_type"));
                    row.add(String.valueOf(rs.getInt("client_id")));
                    row.add(String.valueOf(rs.getInt("lawyer_id")));
                    results.add(row);
                }
            }

        } catch (SQLException e) {
            System.out.println("Selection query SQL error.");
            e.printStackTrace();
        }

        return results;
    }

    // Insertion query for case
    public void insertQuery(
        int case_id,
        Date opening_date,
        Date closing_date,
        CaseFile.caseTypes case_type,
        int is_successful,
        int client_id,
        int lawyer_id) {

        String query = "INSERT INTO Court_Case (case_id, opening_date, closing_date, case_type, is_successful, client_id, lawyer_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stm = conn.prepareStatement(query)) {

            stm.setInt(1, case_id);
            stm.setDate(2, opening_date);
            stm.setDate(3, closing_date);
            stm.setString(4, case_type.name());
            stm.setInt(5, is_successful);
            stm.setInt(6, client_id);
            stm.setInt(7, lawyer_id);

            stm.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error executing insert query:");
        }
    }

    // Helper method for debugging the query that is being run
    private String buildDebugQuery(String baseQuery, List<SelectionCondition> conditions) {
        String debugQuery = baseQuery;
    
        for (SelectionCondition c : conditions) {
            String op = c.getOperator().toUpperCase();
    
            if (op.equals("IS NULL") || op.equals("IS NOT NULL")) {
                continue;
            }
    
            String value = c.getValue();
    
            // wrap strings in quotes
            if (!isNumericAttribute(c.getAttribute())) {
                value = "'" + value + "'";
            }
    
            debugQuery = debugQuery.replaceFirst("\\?", value);
        }
    
        return debugQuery;
    }

    // helper method
    private boolean isNumericAttribute(String attr) {
        return attr.equals("case_id") ||
               attr.equals("client_id") ||
               attr.equals("lawyer_id") ||
               attr.equals("is_successful");
    }
}
