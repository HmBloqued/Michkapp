package views.cancel_inventory;

import java.io.IOException;
import java.sql.SQLException;

import datas.jdbcDataAccess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Inventory;
import models.Property;

import services.PdfWriterService;

public class Controller {
    @FXML
    private Button terminateButton;

    private Property property;

    private Inventory inventory;

    @FXML
    public void setData(Property property, Inventory inventory) {
        this.property = property;
        this.inventory = inventory;
    }

    @FXML
    public void goBackToInventory(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../room_inventory/room_inventory.fxml"));
            AnchorPane root;
            root = loader.load();

            views.room_inventory.Controller controller = loader.getController();
            controller.setData(property, inventory);

            Scene nextScene = new Scene(root);
            Stage mainWindow = (Stage) terminateButton.getScene().getWindow();

            mainWindow.setScene(nextScene);
            mainWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void terminateInventory(ActionEvent event) {
        // JDBC call to delete inventory
        jdbcDataAccess dataAccess = new jdbcDataAccess();
        dataAccess.deleteInventory(inventory);

        try {
            dataAccess.jdbcDataClose();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Send to landing_page
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../landing_page/landing_page.fxml"));

        try {
            AnchorPane root;
            root = loader.load();
            Scene nextScene = new Scene(root);
            Stage mainWindow = (Stage) terminateButton.getScene().getWindow();

            mainWindow.setScene(nextScene);
            mainWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
