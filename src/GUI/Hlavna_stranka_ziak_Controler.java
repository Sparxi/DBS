package GUI;

import Application.Classes;
import Application.DB_Connection;
import Application.Data;
import Application.Priklady;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;

public class Hlavna_stranka_ziak_Controler {
    public ListView list_priklady;
    public ListView list_uspesnost;
    public TextField vyhladaj_text;
    public ChoiceBox typ_prikladu2;
    public ChoiceBox zoradit_podla;
    public TextField slajder_hodnota;
    public Slider slajder;
    public Label od_do;
    public ListView list_schedule;

    DB_Connection db_con = new DB_Connection();
    Connection c = db_con.connect("postgres");
    String rozkliknute;
    int odkial = 0;

    @FXML
    private void initialize() throws Exception {
        String sql_query, s;
        PreparedStatement pst = null;
        ResultSet res = null;
        typ_prikladu2.getItems().add("Všetky");
        zoradit_podla.getItems().add("Názvu príkladu - zostupne");
        zoradit_podla.getItems().add("Názvu príkladu - vzostupne");
        zoradit_podla.getItems().add("Úspešnosti - zostupne");
        zoradit_podla.getItems().add("Úspešnosti - vzostupne");
        sql_query = "SELECT name FROM problem_types";
        pst = c.prepareStatement(sql_query);
        res = pst.executeQuery();
        while(res.next()) {
            s = res.getString("name");
            typ_prikladu2.getItems().add(s);
        }
        zoradit_podla.getSelectionModel().selectFirst();
        typ_prikladu2.getSelectionModel().selectFirst();
        slajder_hodnota.textProperty().bindBidirectional(slajder.valueProperty(), NumberFormat.getNumberInstance());
    }

    @FXML
    void vyhladaj_priklad (ActionEvent event) throws SQLException {
        Double d = slajder.getValue();
        odkial = 0;
        this.search(odkial);
    }

    @FXML
    private void otvor_zaznam() throws IOException {
        rozkliknute = (String) list_priklady.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Detail_popup_ziak.fxml"));
        loader.setController(new Detail_popup_ziak_controler(rozkliknute));
        Parent root1 = loader.load();
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(new Scene(root1));
        popup.show();
    }

    @FXML
    void doprava (ActionEvent event) throws SQLException {
        Double d = slajder.getValue();
        odkial = odkial + d.intValue();
        this.search(odkial);
    }

    @FXML
    void dolava (ActionEvent event) throws SQLException {
        Double d = slajder.getValue();
        if (odkial - d.intValue() > 0)
            odkial = odkial - d.intValue();
        else
            odkial = 0;
        this.search(odkial);
    }
    @FXML
    void search(int odkial) throws SQLException {
        String s1 = (String) typ_prikladu2.getValue();
        String s2 = (String)zoradit_podla.getValue();
        Double d = slajder.getValue();
        Priklady priklad = new Priklady(c);
        priklad.vyhladaj_priklady(vyhladaj_text.getText(),s1, s2,d.intValue(),odkial);
        ObservableList<String> nazvy = FXCollections.observableArrayList(priklad.getZoznam1());
        list_priklady.setItems(nazvy);
        ObservableList<String> uspesnosti = FXCollections.observableArrayList(priklad.getZoznam2());
        list_uspesnost.setItems(uspesnosti);
        vyhladaj_text.clear();
        String s  = String.valueOf(odkial);
        Integer i = d.intValue() + odkial;
        od_do.setText(s + "-" + i.toString());
    }

    @FXML
    void show_schedule() {
        ObservableList<String> list = null;
        Classes classes = new Classes(c);
        list = classes.scheduled_classes(Data.id);

        list_schedule.setItems(list);
    }
}
