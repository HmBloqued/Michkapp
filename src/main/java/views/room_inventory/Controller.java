package views.room_inventory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import datas.jdbcDataAccess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.Address;
import models.Inventory;
import models.Property;

public class Controller {
    @FXML
    private Label labelAddress;

    @FXML
    private Label labelAddressSecondPart;

    @FXML
    private VBox inventoryTable;

    @FXML
    public void clickOnSaveButton(ActionEvent event) throws IOException {
        System.out.println("Save");
    }

    @FXML
    public void setData(Property property, Inventory inventory) {
        Address address = property.getAddress();
        this.labelAddress.setText(String.format("%s %s", address.getStreetNumber(), address.getStreetName()));
        this.labelAddressSecondPart.setText(String.format("%s %s", address.getZipCode(), address.getCity()));

        // Add inventory lines
        // jdbcDataAccess dataAccess = new jdbcDataAccess();
        // List<Inventory> inventories = new ArrayList<Inventory>();

        // TEMP: add one inventory line without using jdbcDataAccess
        // Inventory inventory = new Inventory();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../../resources/components/inventory_line/inventory_line.fxml"));

        try {
            Parent component = loader.load();
            resources.components.inventory_line.Controller controller = loader.getController();
            controller.setData();
            component.getStylesheets()
                    .add(getClass().getResource("../../resources/components/inventory_line/styles.css").toExternalForm());

            if (inventoryTable != null) {
                inventoryTable.getChildren().add(component);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // try {
        // Parent component = loader.load();
        // InventoryLineController controller = loader.getController();
        // controller.setData(inventory);
        // component.getStylesheets()
        // .add(getClass().getResource("/resources/components/inventory_line/styles.css").toExternalForm());

        // if (inventoryTable != null) {
        // inventoryTable.getChildren().add(component);
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        // for (Inventory inventory : inventories) {
        // System.out.println(inventory);
        // FXMLLoader loader = new
        // FXMLLoader(getClass().getResource("/resources/components/inventory_line/inventory_line.fxml"));
        // try {
        // Parent component = loader.load();
        // InventoryLineController controller = loader.getController();
        // controller.setData(inventory);
        // component.getStylesheets()
        // .add(getClass().getResource("/resources/components/inventory_line/styles.css").toExternalForm());
        //
        // if (inventoryTable != null) {
        // inventoryTable.getChildren().add(component);
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }

    }

    // handle button action terminateInventory
    @FXML
    public void terminateInventory(ActionEvent event) throws IOException {
        System.out.println("Terminate inventory");

    }
}
