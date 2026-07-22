package backend.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import backend.db.DatabaseConnection;

public class HearingRepo {

    public List<List<String>> hearingSchedule() {
        List<List<String>> results = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return results;

        String query =
            "SELECT cc.case_id, cc.case_type, " +
            "       h.hearing_date, " +
            "       ch.judge_name, ch.city, ch.province " +
            "FROM Court_Case cc " +
            "JOIN Hearing h ON cc.case_id = h.case_id " +
            "JOIN CourtHouse ch ON h.courtid = ch.courtid " +
            "ORDER BY h.hearing_date";

        try (PreparedStatement stm = conn.prepareStatement(query)) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    List<String> row = new ArrayList<>();
                    row.add(String.valueOf(rs.getInt("case_id")));
                    row.add(rs.getString("case_type"));
                    row.add(String.valueOf(rs.getDate("hearing_date")));
                    row.add(rs.getString("judge_name"));
                    row.add(rs.getString("city"));
                    row.add(rs.getString("province"));
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Hearing schedule query error: " + e.getMessage());
        }
        return results;
    }
}
