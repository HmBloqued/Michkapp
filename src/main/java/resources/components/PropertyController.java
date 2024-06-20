package resources.components;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import models.Address;
import models.Property;

public class PropertyController {
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @FXML
    private Label addressLabel;

    @FXML
    private Label lastDateLabel;

    @FXML
    private Button chooseProperty;

    public void setData(Property property) {
        Address address = property.getAddress();
        addressLabel.setText(String.format("%s %s, %s - %s", address.getStreetNumber(), address.getStreetName(), address.getZipCode(), address.getCity()));
    
        String lastInventoryDateStr = property.getLastInventoryDate() == null
                ? "Aucun antécédant"
                : dateFormat.format(property.getLastInventoryDate());
        lastDateLabel.setText(lastInventoryDateStr);

        chooseProperty.setOnAction(e -> { 
            // Pass Property to the next view
            System.out.println("Choose property: " + property);
        });
    }
}
