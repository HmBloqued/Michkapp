package views.room_inventory;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    public void clickOnSaveButton(ActionEvent event) throws IOException{
        System.out.println("Save");
    }

    @FXML
    public void setData(Property property, Inventory inventory) {
        Address address = property.getAddress();
        this.labelAddress.setText(String.format("%s %s", address.getStreetNumber(), address.getStreetName()));
        this.labelAddressSecondPart.setText(String.format("%s %s", address.getZipCode(), address.getCity()));
    }

    // handle button action terminateInventory
    @FXML
    public void terminateInventory(ActionEvent event) throws IOException {
        System.out.println("Terminate inventory");
    }
}
