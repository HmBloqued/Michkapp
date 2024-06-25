package resources.components.inventory_line;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import datas.jdbcDataAccess;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import models.Furniture;
import models.FurnitureStateInventory;
import models.Inventory;
import models.State;

public class Controller {
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @FXML
    private Label furnitureNameLabel;

    @FXML
    private Label furniturePositionLabel;

    @FXML
    private Label pictureName;

    @FXML
    private ComboBox<State> stateCombo;

    @FXML
    private TextField commentInput;

    @FXML
    private ImageView addPictureIcon;

    @FXML
    private ImageView changePictureIcon;

    @FXML
    private Button unchoosedPicture;

    @FXML
    private HBox choosedPicture;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    FurnitureStateInventory furnitureState;

    // TODO: Temp, should add parameter
    public void setData(Inventory inventory, Furniture furniture) {
        jdbcDataAccess dataAccess = new jdbcDataAccess();
        this.furnitureState = dataAccess.getFurnitureStateInventoryFromInventoryFurniture(inventory, furniture);

        System.out.println("furnitureState : " + furnitureState);

        // Set up line component
        this.choosedPicture.setManaged(false);
        this.unchoosedPicture.setManaged(true);
        this.choosedPicture.setVisible(false);
        this.unchoosedPicture.setVisible(true);
        furnitureNameLabel.setText(furniture.getName());
        furniturePositionLabel.setText(furniture.getPosition());
        // TODO: Add a label different than value (to prevent language issues)
        for (State state : State.values()) {
            stateCombo.getItems().add(state);
        }

        stateCombo.setValue(furnitureState.getFurnitureState());
        stateCombo.setOnAction(event -> handleStateChange());

        if (furnitureState.getPicture() != null) {
            this.pictureName.setText(furnitureState.getPictureName());
            this.choosedPicture.setManaged(true);
            this.unchoosedPicture.setManaged(false);
            this.choosedPicture.setVisible(true);
            this.unchoosedPicture.setVisible(false);
        } else {
            System.out.println("picture not existing !");
        }

        // Prepare text input and add listener
        commentInput.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue,
                String newValue) -> {
            handleCommentChange();
        });

        // Charger l'image pour le bouton
        Image image = new Image(getClass().getResourceAsStream("../../icons/picture.png"));
        addPictureIcon.setImage(image);
        changePictureIcon.setImage(image);
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
        fileChooser.setTitle("Sélectionner une photgraphie");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png"));
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            this.furnitureState.setPicture(Files.readAllBytes(selectedFile.toPath()), selectedFile.getName());
            System.out.println(this.furnitureState.getPicture());
            System.out.println(this.furnitureState.getPictureName());
            System.out.println(this.furnitureState);
            this.pictureName.setText(selectedFile.getName());
            this.choosedPicture.setManaged(true);
            this.unchoosedPicture.setManaged(false);
            this.choosedPicture.setVisible(true);
            this.unchoosedPicture.setVisible(false);
            byte[] fileBytes = Files.readAllBytes(selectedFile.toPath());

            jdbcDataAccess dataAccess = new jdbcDataAccess();
            dataAccess.patchFurnitureStateInventoryImage(this.furnitureState, fileBytes, selectedFile.getName());
        }
    }
}
