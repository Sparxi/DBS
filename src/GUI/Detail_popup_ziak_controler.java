package GUI;

import Application.DB_Connection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Detail_popup_ziak_controler {
    String klik;

    public Detail_popup_ziak_controler(String rozkliknute) {klik = rozkliknute;}

    @FXML
    public TextField typ_text;
    public TextField nazov_text;
    public TextField id_text;
    public TextField max_text;
    public TextField vysledok_text;
    public TextArea znenie_text;
    public Label error_label;
    public Button vymaz_button;

    DB_Connection db_con = new DB_Connection();
    Connection c = db_con.connect("postgres");
    String typ,nazov,znenie;
    short max;

    @FXML
    private void initialize() throws Exception {
        String sql_query;
        PreparedStatement pst = null;
        ResultSet res = null;

        sql_query = "SELECT p.id,pt.name,p.name,p.content,p.max_points FROM problems p " +
                "JOIN problem_types pt ON p.type_id = pt.id WHERE p.name = ?";
        pst = c.prepareStatement(sql_query);
        pst.setString(1, klik);
        res = pst.executeQuery();
        res.next();
        id_text.setText(String.valueOf(res.getInt(1)));
        typ = res.getString(2);
        typ_text.setText(typ);
        nazov = res.getString(3);
        nazov_text.setText(nazov);
        znenie = res.getString(4);
        znenie_text.setText(znenie);
        max = res.getShort(5);
        max_text.setText(String.valueOf(max));
    }
}
