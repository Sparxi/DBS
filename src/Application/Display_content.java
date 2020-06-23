package Application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Display_content {

    Connection c;
    public Display_content(Connection connection) {
        c = connection;
    }

    public void display() {
        Statement st = null;
        try {
            st = c.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM students");

            while (res.next()) {
                System.out.println("Meno: " + res.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
