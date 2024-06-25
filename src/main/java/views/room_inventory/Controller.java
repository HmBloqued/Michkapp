package views.room_inventory;

import java.io.IOException;
import java.util.List;

import datas.jdbcDataAccess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Address;
import models.Furniture;
import models.Inventory;
import models.Property;
import models.Room;

public class Controller {
    @FXML
    private ListView<String> listViewRoom;

    @FXML
    private Label labelAddress;

    @FXML
    private Label labelAddressSecondPart;

    @FXML
    private Label roomNameLabel;

    @FXML
    private VBox inventoryTable;

    private Room selectedRoom;

    private List<Room> rooms;

    private Inventory inventory;

    private Property property;

    @FXML
    public void setData(Property property, Inventory inventory) {
        // Prepare this view
        Address address = property.getAddress();
        this.labelAddress.setText(String.format("%s %s", address.getStreetNumber(), address.getStreetName()));
        this.labelAddressSecondPart.setText(String.format("%s %s", address.getZipCode(), address.getCity()));

        // Get all rooms
        jdbcDataAccess dataAccess = new jdbcDataAccess();
        this.rooms = dataAccess.getRoomsByProperty(property);
        this.inventory = inventory;
        this.property = property;
        if(rooms.size() != 0){
            listViewRoom.getItems().addAll(rooms.stream().map(Room::getName).toArray(String[]::new));
            listViewRoom.getSelectionModel().select(0);
            this.selectedRoom = rooms.get(0);
    
            this.displayRoom();
        }
    }

    @FXML
    private void selectRoom(MouseEvent event) {
        int index = listViewRoom.getSelectionModel().getSelectedIndex();
        this.selectedRoom = rooms.get(index);
        System.out.println("Selected room: " + this.selectedRoom.getName());
        this.displayRoom();
    }

    private void displayRoom(){
        jdbcDataAccess dataAccess = new jdbcDataAccess();
        List<Furniture> furnitures = dataAccess.getFurnituresByRoom(this.selectedRoom);

        // Clear inventory table
        inventoryTable.getChildren().clear();
        this.roomNameLabel.setText(this.selectedRoom.getName());
        
        // For each furniture in each room
        for (Furniture furniture : furnitures) {
            // Add inventory lines
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../../resources/components/inventory_line/inventory_line.fxml"));

            try {
                Parent component = loader.load();
                resources.components.inventory_line.Controller controller = loader.getController();
                controller.setData(inventory, furniture);
                component.getStylesheets()
                        .add(getClass().getResource("../../resources/components/inventory_line/styles.css")
                                .toExternalForm());

                if (inventoryTable != null) {
                    inventoryTable.getChildren().add(component);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // handle button action terminateInventory
    @FXML
    public void terminateInventory(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../terminate_inventory/terminate_inventory.fxml"));
            AnchorPane root = loader.load();

            views.terminate_inventory.Controller controller = loader.getController();
            controller.setData(property, inventory);

            Scene nextScene = new Scene(root);
            Stage mainWindow = (Stage) inventoryTable.getScene().getWindow();

            mainWindow.setScene(nextScene);
            mainWindow.show();
    }

    @FXML
    public void cancelInventory(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../cancel_inventory/cancel_inventory.fxml"));
            AnchorPane root = loader.load();

            views.cancel_inventory.Controller controller = loader.getController();
            controller.setData(property, inventory);

            Scene nextScene = new Scene(root);
            Stage mainWindow = (Stage) inventoryTable.getScene().getWindow();

            mainWindow.setScene(nextScene);
            mainWindow.show();
    }
}
