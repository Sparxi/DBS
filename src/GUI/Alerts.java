package GUI;

import javafx.scene.control.Alert;

public class Alerts {
    public Alerts() {
    }

    public void display_alert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }
}
