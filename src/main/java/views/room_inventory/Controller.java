package views.room_inventory;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import models.Inventory;
import models.Property;

public class Controller {
    
    @FXML
    public void clickOnSaveButton(ActionEvent event) throws IOException{
        System.out.println("Save");
    }

    @FXML
    public void setData(Property property, Inventory inventory) {
        System.out.println("Set datas");
    }
}
