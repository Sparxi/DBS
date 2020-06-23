package Application;

//import java.sql.Connection;
//import java.sql.DriverManager;

import java.sql.*;


public class DB_Connection {

    Connection c = null;

    public DB_Connection() {
    }

    public Connection connect(String passwd) {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "p");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Connected to a DB");

        return c;
    }

    public void disconnect() {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Disconnected from a DB");
    }

}
