package backend.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import backend.db.DatabaseConnection;

public class WorkLogRepo {

    // Query 7 — Aggregation with GROUP BY
    // Total hours logged per lawyer across all their cases.
    // Returns: lawyer_id, name, number of log entries, total hours. Ordered by total hours desc.
    public List<List<String>> totalHoursPerLawyer() {
        List<List<String>> results = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return results;

        String query =
            "SELECT l.lawyer_id, l.name, " +
            "       COUNT(wl.case_id)          AS log_entries, " +
            "       SUM(wl.number_of_hours)    AS total_hours " +
            "FROM Lawyer l " +
            "JOIN WorkLog wl ON l.lawyer_id = wl.lawyer_id " +
            "GROUP BY l.lawyer_id, l.name " +
            "ORDER BY total_hours DESC";

        try (PreparedStatement stm = conn.prepareStatement(query)) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    List<String> row = new ArrayList<>();
                    row.add(String.valueOf(rs.getInt("lawyer_id")));
                    row.add(rs.getString("name"));
                    row.add(String.valueOf(rs.getInt("log_entries")));
                    row.add(String.valueOf(rs.getDouble("total_hours")));
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("GROUP BY query error: " + e.getMessage());
            e.printStackTrace();
        }
        return results;
    }

    // Query 8 — Aggregation with HAVING
    // Lawyers who have logged work on more than one distinct case.
    // Returns: lawyer_id, name, number of distinct cases worked.
    public List<List<String>> lawyersWithMultipleCases() {
        List<List<String>> results = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return results;

        String query =
            "SELECT l.lawyer_id, l.name, " +
            "       COUNT(DISTINCT wl.case_id) AS cases_worked " +
            "FROM Lawyer l " +
            "JOIN WorkLog wl ON l.lawyer_id = wl.lawyer_id " +
            "GROUP BY l.lawyer_id, l.name " +
            "HAVING COUNT(DISTINCT wl.case_id) > 1 " +
            "ORDER BY cases_worked DESC";

        try (PreparedStatement stm = conn.prepareStatement(query)) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    List<String> row = new ArrayList<>();
                    row.add(String.valueOf(rs.getInt("lawyer_id")));
                    row.add(rs.getString("name"));
                    row.add(String.valueOf(rs.getInt("cases_worked")));
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("HAVING query error: " + e.getMessage());
            e.printStackTrace();
        }
        return results;
    }

    // Query 9 — Nested Aggregation with GROUP BY
    // Finds lawyers whose total logged hours >= the average total hours across all lawyers.
    // Outer query groups by lawyer and sums hours.
    // Inner subquery computes the per-lawyer totals, outer AVG averages those totals.
    public List<List<String>> lawyersAboveAverageHours() {
        List<List<String>> results = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return results;

        String query =
            "SELECT l.lawyer_id, l.name, " +
            "       SUM(wl.number_of_hours) AS total_hours " +
            "FROM Lawyer l " +
            "JOIN WorkLog wl ON l.lawyer_id = wl.lawyer_id " +
            "GROUP BY l.lawyer_id, l.name " +
            "HAVING SUM(wl.number_of_hours) >= ( " +
            "    SELECT AVG(lawyer_total) " +
            "    FROM ( " +
            "        SELECT SUM(number_of_hours) AS lawyer_total " +
            "        FROM WorkLog " +
            "        GROUP BY lawyer_id " +
            "    ) " +
            ") " +
            "ORDER BY total_hours DESC";

        try (PreparedStatement stm = conn.prepareStatement(query)) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    List<String> row = new ArrayList<>();
                    row.add(String.valueOf(rs.getInt("lawyer_id")));
                    row.add(rs.getString("name"));
                    row.add(String.valueOf(rs.getDouble("total_hours")));
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Nested aggregation query error: " + e.getMessage());
            e.printStackTrace();
        }
        return results;
    }
}