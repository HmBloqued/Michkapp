package views.landing_page;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {
    @FXML
    // TODO: Rename to something like "select property"
    private void startInventory(ActionEvent event) throws IOException {
        // Load
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/property_selection/property_selection.fxml"));
        // FXMLLoader loader = new FXMLLoader(getClass().getResource("java/views/property_selection/property_selection.fxml"));

        Parent otherSceneRoot = loader.load();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Scene otherScene = new Scene(otherSceneRoot);
        otherScene.getStylesheets().add(getClass().getResource("/views/property_selection/styles.css").toExternalForm());
        // otherScene.getStylesheets().add(getClass().getResource("java/views/property_selection/styles.css").toExternalForm());
        stage.setScene(otherScene);
        stage.setResizable(false);
        stage.show();
    }

    // TODO: Add a scene (and so method) to review already finished inventories
}
