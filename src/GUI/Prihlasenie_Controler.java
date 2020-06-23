package GUI;

import Application.DB_Connection;
import Application.Data;
import Application.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.sql.Connection;


public class Prihlasenie_Controler {
        @FXML
        public TextField meno;
        public TextField heslo;
        public Label chyba;

        DB_Connection db_con = new DB_Connection();
        Connection c = db_con.connect("postgres");
        Users user = new Users(c);

    @FXML
    void prihlasit(ActionEvent event) throws Exception {

        String entered_name = meno.getText(), entered_passwd = heslo.getText();
        int password_check = user.check_user_passwd(entered_name, entered_passwd);

        if (password_check == 1) {
            System.out.println("Welcome");
            if (Data.logintype.equals("teachers")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Hlavna_stranka.fxml"));
                loader.setController(new Hlavna_stranka_Controler());
                Parent parent = loader.load();
                Scene scene = new Scene(parent);
                Data.stage.setScene(scene);
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Hlavna_stranka_ziak.fxml"));
                loader.setController(new Hlavna_stranka_ziak_Controler());
                Parent parent = loader.load();
                Scene scene = new Scene(parent);
                Data.stage.setScene(scene);
            }
        } else {
            if (password_check == 0) {
                chyba.setVisible(true);
                chyba.setTextFill(Color.RED);
                chyba.setText("Zadaný používateľ neexistuje");
            } else {
                chyba.setVisible(true);
                chyba.setTextFill(Color.RED);
                chyba.setText("Zadali ste zlé heslo");
            }
        }
    }
    @FXML
    void registrovat(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Registracia.fxml"));
        loader.setController(new Registracia_Controler());
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Data.stage.setScene(scene);
    }
}