package views.prepare_inventory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import datas.jdbcDataAccess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Address;
import models.Inventory;
import models.Property;
import models.User;

public class Controller {
    private Property property;

    @FXML
    private Label address;

    @FXML
    private Label addressSecondPart;

    @FXML
    private Label ownerName;

    @FXML
    private Label occupantName;

    @FXML
    private Label todayDate;

    @FXML
    private CheckBox ownerPresent;

    @FXML
    private CheckBox occupantPresent;

    @FXML
    public void clickOnSaveButton(ActionEvent event) throws IOException {
        System.out.println("Save");
    }

    public void setData(Property property) {
        this.property = property;
        Address address = property.getAddress();
        User owner = property.getOwner();

        this.address.setText(String.format("%s %s", address.getStreetNumber(), address.getStreetName()));
        this.addressSecondPart.setText(String.format("%s %s", address.getZipCode(), address.getCity()));

        this.ownerName.setText(String.format("%s %s", owner.getFirstname(), owner.getLastname()));
        this.todayDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
    }

    @FXML
    public void goBackToMenu(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../property_selection/property_selection.fxml"));

        try {
            AnchorPane root;
            root = loader.load();
            Scene nextScene = new Scene(root);
            Stage mainWindow = (Stage) occupantPresent.getScene().getWindow();

            mainWindow.setScene(nextScene);
            mainWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void startInventory(ActionEvent event) throws IOException {
        try {
            // Create inventory with appropriate values
            jdbcDataAccess dataAccess = new jdbcDataAccess();
            int inventoryId = dataAccess.createInventory(this.property.getId(), this.ownerPresent.isSelected(),
                    this.occupantPresent.isSelected());
            // Create every inventory state for each furniture
            boolean result = dataAccess.createFurnitureStatesInventory(property, inventoryId);
            if (!result) {
                System.out.println("Error while creating furniture states inventory");
                return;
            }

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../room_inventory/room_inventory.fxml"));
            AnchorPane root = loader.load();

            // Get inventory from inventory id
            Inventory inventory = dataAccess.getInventoryById(inventoryId, property);

            views.room_inventory.Controller controller = loader.getController();
            controller.setData(property, inventory);

            Scene nextScene = new Scene(root);
            Stage mainWindow = (Stage) ownerPresent.getScene().getWindow();

            mainWindow.setScene(nextScene);
            mainWindow.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
