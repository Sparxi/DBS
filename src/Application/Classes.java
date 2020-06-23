package Application;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Classes {

    Connection c = null;

    public Classes(Connection con) {
        c = con;
    }

    public java.sql.Date strToSqlDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date d;

        try {
            d = format.parse(date);
            return new java.sql.Date(d.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int create_class(String teacher_name, String name) {
        Users user = new Users(c);
        ResultSet res = null;
        String sql_query = "";
        PreparedStatement prep = null;
        sql_query = "INSERT INTO classes(name, teacher_id) VALUES (?, ?)";
        int teacher_id = user.find_user("Ucitel", teacher_name);

        try {
            c.setAutoCommit(false);
            prep = c.prepareStatement(sql_query);
            prep.setString(1, name);
            prep.setInt(2, teacher_id);

            prep.execute();

            if (teacher_id > 0) {
                c.commit();
            } else {
                c.rollback();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (teacher_id == -1) {
            System.out.println("This teacher does not exist.");
            return 0;
        }
        return 1;
    }

    public int find_class(String class_name) {
        ResultSet res = null;
        String sql_query = "";
        PreparedStatement prep = null;

        sql_query = "SELECT * FROM classes WHERE name = ?";
        try {
            prep = c.prepareStatement(sql_query);
            prep.setString(1, class_name);
            res = prep.executeQuery();

            if (res.next())
                return res.getInt("ID");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int find_class_in_time(String class_name, String date) {
        ResultSet res = null;
        String sql_query = "";
        PreparedStatement prep = null;

        sql_query =
                "SELECT cit.ID " +
                        "FROM classes " +
                        "JOIN class_in_time AS cit ON classes.ID = cit.class_id " +
                        "WHERE classes.name = ? AND cit.date = ?";

        try {

            prep = c.prepareStatement(sql_query);
            prep.setString(1, class_name);
            prep.setDate(2, strToSqlDate(date));
            res = prep.executeQuery();

            if (res.next())
                return res.getInt("ID");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public boolean add_class_in_time(String class_name, String teacher_name, String date, String time, String room) {

        Users users = new Users(c);
        ResultSet res = null;
        String sql_query = "";
        PreparedStatement prep = null;

        System.out.println(class_name + " " + teacher_name + " " + date + " " + time +
                " " + room);

        sql_query = "INSERT INTO class_in_time(class_id, teacher_id, date, time, room) VALUES (?, ?, ?, ?, ?)";

        try {

            prep = c.prepareStatement(sql_query);
            prep.setInt(1, find_class(class_name));
            prep.setInt(2, users.find_user("teacher", teacher_name));
            prep.setDate(3, strToSqlDate(date));
            prep.setString(4, time);
            prep.setString(5, room);

            if (prep.execute() != false) return false;
            else return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean add_student_to_class(String student_name, String class_name, String date) {
        Users users = new Users(c);
        ResultSet res = null;
        String sql_query = "";
        PreparedStatement prep = null;

        sql_query = "INSERT INTO attendance(student_id, class_in_time_id) VALUES(?, ?)";

        try {
            prep = c.prepareStatement(sql_query);
            prep.setInt(1, users.find_user("Ziak", student_name));
            prep.setInt(2, find_class_in_time(class_name, date));

            if (prep.execute() != false) return false;
            else return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ObservableList<String> find_classes(int user_id) {        // Method for autosuggestion

        String sql_query;
        PreparedStatement pst = null;
        ResultSet res = null;
        ObservableList<String> as = FXCollections.observableArrayList();

        sql_query = "SELECT c.name " +
                "FROM attendance a " +
                "JOIN class_in_time cit on a.class_in_time_id = cit.ID " +
                "JOIN classes c on cit.class_id = c.ID " +
                "WHERE a.student_id = ? " +
                "GROUP BY c.name;";

        try {
            pst = c.prepareStatement(sql_query);
            pst.setInt(1, user_id);

            res = pst.executeQuery();

            while (res.next()) {
                as.add(res.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return as;

    }

    public ObservableList<String> scheduled_classes(int user_id) {
        String sql_query;
        PreparedStatement pst = null;
        ResultSet res = null;
        String today = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        ObservableList<String> as = FXCollections.observableArrayList();

        sql_query = "SELECT c.name, cit.date, cit.time, cit.room\n" +
                "FROM class_in_time cit\n" +
                "JOIN attendance a on cit.ID = a.class_in_time_id\n" +
                "JOIN classes c on cit.class_id = c.ID\n" +
                "WHERE a.student_id = ? AND cit.date >= ?" +
                "ORDER BY cit.date, cit.time;";

        try {
            pst = c.prepareStatement(sql_query);
            pst.setInt(1, user_id);
            pst.setDate(2, strToSqlDate(today));

            res = pst.executeQuery();

            while (res.next()) {
                as.add(res.getString("name") + "\t " + res.getString("date") + "\t " +
                        res.getString("time") + "\t " + res.getString("room"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return as;
    }

    public int del_class_in_time(String class_name, String teacher_name, String date, String time, String room) {
        Users users = new Users(c);
        String sql_query;
        PreparedStatement pst = null;
        ResultSet res = null;
        int id;
        sql_query = "SELECT id FROM class_in_time WHERE class_id = ? AND teachers_id = ? AND date = ? AND time = ? AND room = ?";
        try {
            pst = c.prepareStatement(sql_query);
            pst.setInt(1, find_class(class_name));
            pst.setInt(2, users.find_user("teacher", teacher_name));
            pst.setDate(3, strToSqlDate(date));
            pst.setString(4, time);
            pst.setString(5, room);

            res = pst.executeQuery();
            res.next();
            id = res.getInt(1);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

        try {
            c.setAutoCommit(false);
            sql_query = "DELETE FROM attendance WHERE class_in_time_id = ?";
            pst = c.prepareStatement(sql_query);
            pst.setInt(1, id);
            pst.execute();
            sql_query = "DELETE FROM class_in_time WHERE id = ?";
            pst = c.prepareStatement(sql_query);
            pst.setInt(1, id);
            pst.execute();
            c.commit();
            return 1;
        }
        catch (SQLException e) {
            e.printStackTrace();
            try {
                c.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return -1;
        }
    }
}
