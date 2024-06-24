package resources.components.inventory_line;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import datas.jdbcDataAccess;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import models.Address;
import models.Furniture;
import models.FurnitureStateInventory;
import models.Inventory;
import models.Property;
import models.State;

public class Controller {
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @FXML
    private Label furnitureNameLabel;

    @FXML
    private Label furniturePositionLabel;

    @FXML
    private ComboBox<State> stateCombo;

    @FXML
    private TextField commentInput;

    @FXML
    private ImageView addPictureIcon;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    FurnitureStateInventory furnitureState;

    // TODO: Temp, should add parameter
    public void setData(Inventory inventory, Furniture furniture) {
        jdbcDataAccess dataAccess = new jdbcDataAccess();
        this.furnitureState = dataAccess.getFurnitureStateInventoryByFurniture(furniture,
                inventory);

        // Set up line component
        furnitureNameLabel.setText(furniture.getName());
        furniturePositionLabel.setText(furniture.getPosition());
        // TODO: Add a label different than value (to prevent language issues)
        for (State state : State.values()) {
            stateCombo.getItems().add(state);
        }


        stateCombo.setValue(furnitureState.getFurnitureState());
        stateCombo.setOnAction(event -> handleStateChange());

        // Prepare text input and add listener
        commentInput.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue,
                String newValue) -> {
            handleCommentChange();
        });

        // Charger l'image pour le bouton
        Image image = new Image(getClass().getResourceAsStream("../../icons/picture.png"));
        addPictureIcon.setImage(image);
    }

    private void handleStateChange() {
        String selected = stateCombo.getSelectionModel().getSelectedItem().toString();

        jdbcDataAccess dataAccess = new jdbcDataAccess();
        dataAccess.patchFurnitureStateInventoryState(this.furnitureState, State.valueOf(selected));
    }

    private void handleCommentChange() {
        String comment = commentInput.getText();
        
        jdbcDataAccess dataAccess = new jdbcDataAccess();
        dataAccess.patchFurnitureStateInventoryComment(this.furnitureState, comment);
    }

    @FXML
    private void choosePicture() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une photgraphie de l'élément");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            byte[] fileBytes = Files.readAllBytes(selectedFile.toPath());

            // Handle this file to save it in database as a tinyblob
            jdbcDataAccess dataAccess = new jdbcDataAccess();
            dataAccess.patchFurnitureStateInventoryImage(this.furnitureState, fileBytes);
        }
    }
}
