package resources.components.inventory_line;

import java.io.IOException;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import models.Address;
import models.Property;
import models.State;

public class Controller {
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @FXML
    private ComboBox<State> stateCombo;

    @FXML
    private TextField commentInput;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // TODO: Temp, should add parameter
    public void setData() {
        System.out.println("Setting data");

        // Prepare ComboBox with values TODO: Add a label different than value (to
        // prevent language issues)
        for (State state : State.values()) {
            stateCombo.getItems().add(state);
        }
        stateCombo.getSelectionModel().selectFirst();
        stateCombo.setOnAction(event -> handleStateChange());

        // Prepare comment input and add listener
        commentInput.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue,
                String newValue) -> {
            handleCommentChange();
        });
    }

    private void handleStateChange() {
        String selected = stateCombo.getSelectionModel().getSelectedItem().toString();
        System.out.println("Selected item: " + selected);
    }

    private void handleCommentChange() {
        String comment = commentInput.getText();
        System.out.println("Comment: " + comment);
    }

    @FXML
    private void choosePicture() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une photgraphie de l'élément");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            System.out.println("Fichier sélectionné : " + selectedFile.getAbsolutePath());

            // Handle this file to save it in database as a tinyblob
            
        }
    }
}
