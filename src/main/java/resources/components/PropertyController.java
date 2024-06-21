package resources.components;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
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
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../../views/prepare_inventory/prepare_inventory.fxml"));
                Parent root = loader.load();
                
                views.prepare_inventory.Controller controller = loader.getController();
                controller.setData(property);

                Scene nextScene = new Scene(root);

                Stage mainWindow = (Stage) chooseProperty.getScene().getWindow();

                mainWindow.setScene(nextScene);
                mainWindow.show();
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
