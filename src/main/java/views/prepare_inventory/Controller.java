package views.prepare_inventory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.Address;
import models.Property;
import models.User;

public class Controller {
    
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
    public void clickOnSaveButton(ActionEvent event) throws IOException{
        System.out.println("Save");
    }

    public void setData(Property property) {
        Address address = property.getAddress();
        User owner = property.getOwner();


        this.address.setText(String.format("%s %s", address.getStreetNumber(), address.getStreetName()));
        this.addressSecondPart.setText(String.format("%s %s", address.getZipCode(), address.getCity()));

        this.ownerName.setText(String.format("%s %s", owner.getFirstname(), owner.getLastname()));
        this.todayDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
    }

    @FXML
    public void startInventory(ActionEvent event) throws IOException{
        // Create inventory with appropriate values
        

        // Create every inventory state for each furniture

        // Switch to inventory scene
    }
}
