package backend.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import backend.db.DatabaseConnection;

/*
* This layer directly interacts with the database.
* All SQL queries are written here.
*/
public class InvoiceRepo {

    public List<List<String>> projectionQuery(List<String> selectedCols) {

        List<List<String>> results = new ArrayList<>();

        if(selectedCols == null || selectedCols.isEmpty()) {
            return results;
        }

        Connection conn = DatabaseConnection.getConnection();

        String query = "SELECT " + String.join(",", selectedCols) + " FROM Invoice";
        System.out.println("QUERY " + query );


        PreparedStatement stm = null;
        ResultSet r = null;
        try {
            stm = conn.prepareStatement(query);
            r = stm.executeQuery();
            while(r.next()) {
                List<String> row = new ArrayList<>();
                for(String col : selectedCols){
                    row.add(r.getString(col));
                }
                results.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error in query during making cases");
            e.printStackTrace();
        }

        return results;
    }
}
