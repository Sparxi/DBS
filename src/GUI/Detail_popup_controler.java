package GUI;

import Application.DB_Connection;
import Application.Priklady;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Detail_popup_controler {
    String klik;

    public Detail_popup_controler(String rozkliknute) {klik = rozkliknute;}

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
    float vysledok;
    short max;

    @FXML
    private void initialize() throws Exception {
        String sql_query;
        PreparedStatement pst = null;
        ResultSet res = null;

        sql_query = "SELECT p.id,pt.name,p.name,p.content,p.result,p.max_points FROM problems p " +
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
        vysledok = res.getFloat(5);
        vysledok_text.setText(String.valueOf(vysledok));
        max = res.getShort(6);
        max_text.setText(String.valueOf(max));
    }

    @FXML
    void uloz (ActionEvent event) throws Exception {
        String sql_query;
        PreparedStatement pst = null;
        ResultSet res = null;
        Priklady priklad = new Priklady(c);
        short id_nove = priklad.najdi_id(typ_text.getText());

        if (id_nove == 0) {
            error_label.setVisible(true);
            error_label.setTextFill(Color.RED);
            error_label.setText("Nepodarilo sa vykonať zmeny. Zadali ste zlý typ príkladu.");
        } else {
            c.setAutoCommit(false);
            sql_query = "UPDATE problems SET type_id = ?, name = ?, content = ?, result = ?, max_points = ?  WHERE name = ? AND type_id = ? AND result = ? AND max_points = ?";

            pst = c.prepareStatement(sql_query);
            pst.setShort(1, id_nove);
            pst.setString(2, nazov_text.getText());
            pst.setString(3, znenie_text.getText());
            pst.setFloat(4, Float.parseFloat(vysledok_text.getText()));
            pst.setInt(5, Integer.parseInt(max_text.getText()));
            pst.setString(6, nazov);
            pst.setShort(7, priklad.najdi_id(typ));
            pst.setFloat(8, Float.parseFloat(vysledok_text.getText()));    //
            pst.setInt(9, Integer.parseInt(max_text.getText()));    //
            pst.executeUpdate();

            if (priklad.pocet_prikladov(nazov_text.getText(), id_nove) > 1) {
                c.rollback();
                error_label.setVisible(true);
                error_label.setTextFill(Color.RED);
                error_label.setText("Nepodarilo sa vykonať zmeny. Príklad s rovnakým názvom a typom už existuje.");
            } else {
                c.commit();
                error_label.setVisible(true);
                error_label.setTextFill(Color.GREEN);
                error_label.setText("Zmeny boli úspešne vykonané");
            }

//            Hlavna_stranka_Controler hsc = new Hlavna_stranka_Controler();
//            hsc.refresh(typ_text.getText());
        }
    }

    @FXML
    void zrus (ActionEvent event) throws Exception {
        this.initialize();
    }

    @FXML
    void vymaz (ActionEvent event) throws Exception {
        Priklady priklad = new Priklady(c);
        priklad.vymaz_priklad(klik);
        Stage stage = (Stage) vymaz_button.getScene().getWindow();
        stage.close();
    }
}
