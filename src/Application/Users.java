package Application;

import java.sql.*;

public class Users {
    Connection c;

    public Users(Connection input_con) {
        c = input_con;
    }

    public int new_user(String table_name, String name, String passwd, String email_addr, int phone_num) {

        PreparedStatement pst;
        String sql_query = "";
        Password_hash password_hash = new Password_hash();

        if (table_name == "Ziak")
            sql_query = "INSERT INTO students (name, password, email_address, phone_number) VALUES (?, ?, ?, ?)";
        else
            sql_query = "INSERT INTO teachers (name, password, email_address, phone_number) VALUES (?, ?, ?, ?)";

        try {

            pst = c.prepareStatement(sql_query);
            pst.setString(1, name);
            pst.setString(2, password_hash.getSecurePassword(passwd));
            pst.setString(3, email_addr);
            pst.setInt(4, phone_num);

            if (pst.execute() != false) System.out.println("Failed to add new user.");
            else {
                System.out.println("New user added.");
                return 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int check_user_passwd(String name, String passw) throws SQLException {
        String sql_query;
        PreparedStatement pst = null;
        ResultSet res = null;
        Password_hash password_hash = new Password_hash();
        sql_query = "SELECT * FROM students WHERE students.name = ?";

        String sec_pass = password_hash.getSecurePassword(passw);

        pst = c.prepareStatement(sql_query);
        pst.setString(1, name);
        res = pst.executeQuery();

        if (res.next()) {

            if (sec_pass.equals(res.getString("password"))) {

                Data.logintype = "students";
                Data.meno = res.getString(1);
                Data.heslo = res.getString(2);
                Data.id = res.getInt("id");
                return 1;
            }
            else
                return -1;
        }
        else{
            sql_query = "SELECT * FROM teachers WHERE teachers.name = ?";
            pst = c.prepareStatement(sql_query);
            pst.setString(1, name);
            res = pst.executeQuery();
            if (res.next()) {
                if (sec_pass.equals(res.getString("password"))) {
                    Data.logintype = "teachers";
                    Data.meno = res.getString(1);
                    Data.heslo = res.getString(2);
                    return 1;
                }
                else
                    return -1;
            }
        }
        return 0;
    }

    public int find_user(String user_type, String user_name) {
        String sql_query;
        PreparedStatement pst = null;
        ResultSet res = null;

        try {
            if (user_type.equals("Ziak")) {
                sql_query = "SELECT id FROM students WHERE name = ?";
                pst = c.prepareStatement(sql_query);
                pst.setString(1, user_name);
            }
            else {
                sql_query = "SELECT id FROM teachers WHERE name = ?";
                pst = c.prepareStatement(sql_query);
                pst.setString(1, user_name);
            }

            res = pst.executeQuery();


            if (res.next())
                return res.getInt("ID");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}

