import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class App extends Application implements Initializable {

    @FXML
    private Label label;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/landing_page/landing_page.fxml"));
        root.getStylesheets().add(getClass().getResource("/views/landing_page/styles.css").toExternalForm());
        Scene scene = new Scene(root);
        stage.setTitle("Michk'App");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public static void main(String[] args) {
        launch(args);
    }
}
