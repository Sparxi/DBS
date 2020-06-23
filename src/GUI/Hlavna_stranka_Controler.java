package GUI;

import Application.Classes;
import Application.Data;
import Application.Priklady;
import Application.Users;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;

public class Hlavna_stranka_Controler extends Hlavna_stranka_ziak_Controler {
    @FXML
    public TextField nazov;
    public TextField ucitel;
    public TextField garant;
    public TextField kurz;
    public TextField miestnost;
    public TextField kurz_v_case;
    public TextField ziak;
    public TextField cas;
    public Tab kurzy_tab;
    public ListView list_priklady;
    public TextField vysledok_text;
    public TextField max_bodov_text;
    public TextField nazov_text;
    public TextArea znenie_text;
    public ChoiceBox typ_prikladu;
    public ChoiceBox typ_prikladu2;
    public ChoiceBox zoradit_podla;
    public Label pridaj_label;
    public TextField slajder_hodnota;
    public TextField datum;
    public Slider slajder;
    public ListView list_schedule;
    public TextField class_in_time_date;

    Alerts al = new Alerts();
    int return_value = -1;

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
        while (res.next()) {
            s = res.getString("name");
            typ_prikladu.getItems().add(s);
            typ_prikladu2.getItems().add(s);
        }
        zoradit_podla.getSelectionModel().selectFirst();
        typ_prikladu.getSelectionModel().selectFirst();
        typ_prikladu2.getSelectionModel().selectFirst();
        slajder_hodnota.textProperty().bindBidirectional(slajder.valueProperty(), NumberFormat.getNumberInstance());

        //Jedis jedis = new Jedis("localhost");
        //System.out.println("REDIS funguje!");
    }

    @FXML
    void pridaj_priklad(ActionEvent event) throws Exception {
        // System.out.println(vysledok_text.getText());
        float f = Float.parseFloat(vysledok_text.getText());
        int i = Integer.parseInt(max_bodov_text.getText());
        String s = (String) typ_prikladu.getValue();
        // System.out.println(i + ", "+ s + ", " + f);
        Priklady priklad = new Priklady(c);
        if (priklad.pridaj_priklad(nazov_text.getText(), f, i, znenie_text.getText(), s) == 1) {
            pridaj_label.setVisible(true);
            pridaj_label.setText("Príklad s rovnakým názvom a typom už existuje.");
            pridaj_label.setTextFill(Color.RED);
        } else {
            pridaj_label.setVisible(true);
            pridaj_label.setText("Príklad úspešne pridaný");
            pridaj_label.setTextFill(Color.GREEN);
            vysledok_text.clear();
            max_bodov_text.clear();
            znenie_text.clear();
            nazov_text.clear();
        }
    }

    @FXML
    private void otvor_zaznam() throws IOException {
        rozkliknute = (String) list_priklady.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Detail_popup.fxml"));
        loader.setController(new Detail_popup_controler(rozkliknute));
        Parent root1 = loader.load();
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(new Scene(root1));
        popup.show();
    }

    @FXML
    void show_schedule() {
        ObservableList<String> list = null;
        Classes classes = new Classes(c);
        list = classes.scheduled_classes(Data.id);

        list_schedule.setItems(list);
    }

    @FXML
    void pridaj_kurz (ActionEvent event) throws Exception {
        Classes cls = new Classes(c);
        return_value = cls.create_class(garant.getText(), nazov.getText());
        if (return_value == 1)
            al.display_alert("Informacia", "Kurz " + nazov.getText() + " pridany s ucitelom " + garant.getText() + ".");
        else
            al.display_alert("Informacia", "Zadany ucitel neexistuje.");
    }

    @FXML
    void pridaj_kurz_v_case (ActionEvent event) throws Exception {
        Classes classes = new Classes(c);
        Boolean b = classes.add_class_in_time(kurz.getText(), ucitel.getText(), datum.getText(), cas.getText(), miestnost.getText());
        if (b) {
            al.display_alert("Pridanie kurzu v case", "Kurz v case uspesne pridany!");
            kurz.clear(); ucitel.clear(); datum.clear(); cas.clear(); miestnost.clear();
        }
        else al.display_alert("Pridanie kurzu v case", "Kurz v case nebol pridany, skontrolujte pole kurz a ucitel, mozu byt nespravne zadane.");

    }

    @FXML
    void check(ActionEvent event) throws Exception {
        Classes classes = new Classes(c);
        Users u = new Users(c);
        ObservableList<String> suggestions = null;
        int id = u.find_user("Ziak", ziak.getText());
        if (id != 0) {
            ziak.setStyle("-fx-border-color: #2ecc71; -fx-border-radius: 4px;");
            suggestions = classes.find_classes(id);
            TextFields.bindAutoCompletion(kurz_v_case, suggestions);
        }
    }

    @FXML
    void pridaj_ziaka_na_kurz (ActionEvent event) throws Exception {
        Classes classes = new Classes(c);
        if (classes.add_student_to_class(ziak.getText(), kurz_v_case.getText(), class_in_time_date.getText())){
            al.display_alert("Pridanie ziaka na kurz", "Ziak " + ziak.getText() + " uspesne pridany na kurz.");
            ziak.clear(); kurz_v_case.clear(); class_in_time_date.clear(); ziak.setStyle("-fx-border-color: lightgrey; -fx-border-radius: 4px;");
        }
    }

    @FXML
    void vymaz_kurz_v_case (ActionEvent event) throws Exception {
        Classes classes = new Classes(c);
        int i = classes.del_class_in_time(kurz.getText(), ucitel.getText(), datum.getText(), cas.getText(), miestnost.getText());
        if (i == 1) {
            al.display_alert("Pridanie kurzu v case", "Kurz v case uspesne odstraneny");
            kurz.clear();
            ucitel.clear();
            datum.clear();
            cas.clear();
            miestnost.clear();
        }
        else {
            if (i == 0)
                al.display_alert("Pridanie kurzu v case", "Kurz v case nebol odstraneny, kurz so zadanymi parametrami neexistuje.");
            else
                al.display_alert("Pridanie kurzu v case", "Kurz v case nebol odstraneny, vyskytla sa chyba");
        }
    }
}
