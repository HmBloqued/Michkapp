package views.pdf_generated;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controller {
    @FXML
    private Button menuButton;

    @FXML
    public void setData() {
    }

    @FXML
    public void goBackToMainMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../landing_page/landing_page.fxml"));
            AnchorPane root;
            root = loader.load();

            Scene nextScene = new Scene(root);
            Stage mainWindow = (Stage) menuButton.getScene().getWindow();

            mainWindow.setScene(nextScene);
            mainWindow.show();
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
