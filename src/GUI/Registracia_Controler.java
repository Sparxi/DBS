package GUI;

import Application.DB_Connection;
import Application.Data;
import Application.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.sql.Connection;

public class Registracia_Controler {
    @FXML
    public ChoiceBox<String> ziak_ucitel;
    public TextField meno;
    public TextField heslo;
    public TextField email;
    public TextField telefon;

    String name, passwd, mail;
    int phone, return_value;

    DB_Connection db_con = new DB_Connection();
    Connection c = db_con.connect("postgres");
    Users user = new Users(c);
    Alerts al = new Alerts();


    @FXML
    private void initialize(){
        ziak_ucitel.getItems().addAll("Ziak", "Ucitel");
        ziak_ucitel.setValue("Ziak");
    }

    @FXML
    void registruj(ActionEvent event) throws Exception {

        name = meno.getText();
        passwd = heslo.getText();
        mail = email.getText();
        phone = Integer.parseInt(telefon.getText());

        if (ziak_ucitel.getValue() == "Ziak")
            return_value = user.new_user("Ziak", name, passwd, mail, phone);
        else
            return_value = user.new_user("teachers", name, passwd, mail, phone);

        if (return_value == 0)
            al.display_alert("Informacia", "Pouzivatel, ktoreho chcete pridat uz existuje.");
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Prihlasenie.fxml"));
            loader.setController(new Prihlasenie_Controler());
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            Data.stage.setScene(scene);
        }
    }
}
