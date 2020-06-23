package GUI;
import Application.Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import Application.DB_Connection;


    // Main class po spusteni programu nacita graficke rozhranie prihlasenie a priradi mu controler.

    public class Main extends Application {

        @Override
        public void start(Stage stage) throws Exception {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Prihlasenie.fxml"));
            loader.setController(new Prihlasenie_Controler());
            Parent parent = loader.load();
            Scene scene = new Scene(parent);

            stage.setTitle("Dobrucko");
            stage.setScene(scene);
            stage.show();
            Data.stage = stage;
        }


    }
