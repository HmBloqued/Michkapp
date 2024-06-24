package views.terminate_inventory;

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
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import models.Address;
import models.Furniture;
import models.FurnitureStateInventory;
import models.Inventory;
import models.Property;
import models.Room;

public class Controller {
    @FXML
    private Label labelAddress;

    @FXML
    public void setData(Property property, Inventory inventory) {
        // Prepare this view
        System.out.println("Set data for terminate inventory");
    }

    @FXML
    public void goBackToInventory(ActionEvent event) {
        System.out.println("Go back to inventory");
    }

    @FXML
    public void terminateInventory(ActionEvent event) {
        System.out.println("Terminate inventory");
    }
}
