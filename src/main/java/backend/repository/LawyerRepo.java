package backend.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import backend.db.DatabaseConnection;

public class LawyerRepo {

    // Query 6 — Join: Court_Case + Client + Lawyer
    // Returns all cases of a user-specified case type, with client and lawyer names.
    // Covers 3 relations. User provides the WHERE value (case_type).
    public List<List<String>> joinByCaseType(String caseType) {
        List<List<String>> results = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return results;

        String query =
            "SELECT cc.case_id, cc.case_type, " +
            "       cc.opening_date, cc.closing_date, " +
            "       c.name AS client_name, " +
            "       l.name AS lawyer_name " +
            "FROM Court_Case cc " +
            "JOIN Client c ON cc.client_id = c.client_id " +
            "JOIN Lawyer l ON cc.lawyer_id = l.lawyer_id " +
            "WHERE cc.case_type = ?";

        try (PreparedStatement stm = conn.prepareStatement(query)) {
            stm.setString(1, caseType);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    List<String> row = new ArrayList<>();
                    row.add(String.valueOf(rs.getInt("case_id")));
                    row.add(rs.getString("case_type"));
                    row.add(rs.getDate("opening_date") != null
                            ? rs.getDate("opening_date").toString() : "");
                    row.add(rs.getDate("closing_date") != null
                            ? rs.getDate("closing_date").toString() : "Open");
                    row.add(rs.getString("client_name"));
                    row.add(rs.getString("lawyer_name"));
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Join query error: " + e.getMessage());
            e.printStackTrace();
        }
        return results;
    }

    // Query 10 — Division
    // Finds lawyers who have logged work on EVERY case assigned to them.
    // Uses MINUS (Oracle equivalent of EXCEPT) inside NOT EXISTS for relational division.
    // A lawyer fails if: (their assigned cases) MINUS (cases they logged) is non-empty.
    public List<List<String>> divisionLawyersAllCasesLogged() {
        List<List<String>> results = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return results;

        String query =
            "SELECT l.lawyer_id, l.name " +
            "FROM Lawyer l " +
            "WHERE NOT EXISTS ( " +
            "    SELECT cc.case_id " +
            "    FROM Court_Case cc " +
            "    WHERE cc.lawyer_id = l.lawyer_id " +
            "    MINUS " +
            "    SELECT wl.case_id " +
            "    FROM WorkLog wl " +
            "    WHERE wl.lawyer_id = l.lawyer_id " +
            ") " +
            "ORDER BY l.lawyer_id";

        try (PreparedStatement stm = conn.prepareStatement(query)) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    List<String> row = new ArrayList<>();
                    row.add(String.valueOf(rs.getInt("lawyer_id")));
                    row.add(rs.getString("name"));
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Division query error: " + e.getMessage());
            e.printStackTrace();
        }
        return results;
    }
}
