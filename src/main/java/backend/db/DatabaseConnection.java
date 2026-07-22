package backend.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL =
            "jdbc:oracle:thin:@localhost:1522:stu";
    
    // Replace with your Oracle credentials
    // ssh -l cwl -L localhost:1522:dbhost.students.cs.ubc.ca:1522 remote.students.cs.ubc.ca - for SSH Tunneling
    // add your cwl password when promoted
    // sqlplus ora_<cwl> / a<StudentNum>
    // if you run into any issues try doing,
    // sqlplus ora_<cwl> 
    // enter a<StudentNum> when prompted for password
    
    // run @setup.sql in ssh
    // run SELECT COUNT(*) FROM Court_Cases;
    // if it is not equal to 10, try troubleshooting steps below. 

    // mvn clean javafx:run in another terminal window.

    // Troubleshooting 
    // if you do not see the count of total cases to be 10, please try copy pasting the entire setup sql file in the ssh 
    // then run COMMIT; 
    // then mvn clean javafx:run in another terminal window. 
    private static final String USERNAME = "ora_kanaad";
    private static final String PASSWORD = "a57135345";

    private static Connection connection = null;

    public static Connection getConnection() {

    if (connection != null) {
        return connection;
    }

    try {
        connection = DriverManager.getConnection(
            URL,
            USERNAME,
            PASSWORD
        );

    } catch (SQLException e) {
        System.out.println("Database connection ERROR.");
    }

    return connection;  
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException ex) {
            System.out.println("Database connection ERROR.");
            ex.printStackTrace();
        }
        }
}
