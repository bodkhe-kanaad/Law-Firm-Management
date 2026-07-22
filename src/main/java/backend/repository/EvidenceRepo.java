package backend.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import backend.db.DatabaseConnection;

public class EvidenceRepo {

    public List<List<String>> evidencePerCase() {
        List<List<String>> results = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return results;

        String query =
            "SELECT e.evidence_id, e.evidence_type, e.stored_in, " +
            "       e.case_id, p.hearing_date " +
            "FROM Evidence e " +
            "JOIN Presented p ON e.evidence_id = p.evidence_id " +
            "ORDER BY e.case_id";

        try (PreparedStatement stm = conn.prepareStatement(query)) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    List<String> row = new ArrayList<>();
                    row.add(String.valueOf(rs.getInt("evidence_id")));
                    row.add(rs.getString("evidence_type"));
                    row.add(rs.getString("stored_in"));
                    row.add(String.valueOf(rs.getInt("case_id")));
                    row.add(String.valueOf(rs.getDate("hearing_date")));
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Evidence query error: " + e.getMessage());
        }
        return results;
    }
}
