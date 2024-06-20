package views.room_inventory;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Controller {
    
    @FXML
    public void clickOnSaveButton(ActionEvent event) throws IOException{
        System.out.println("Save");
    }
}
