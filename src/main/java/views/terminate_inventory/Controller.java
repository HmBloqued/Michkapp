package views.terminate_inventory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import datas.jdbcDataAccess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
        // Prepare this view
        System.out.println("Set data for terminate inventory");
    }

    @FXML
    public void goBackToInventory(ActionEvent event) {
        System.out.println("Go back to inventory");
    }

    @FXML
    public void terminateInventory(ActionEvent event) {
        boolean isPdfGenerated = PdfWriterService.createInventoryPdf(inventory, property);
        if (isPdfGenerated) {
            // JDBC call to update inventory date
            jdbcDataAccess dataAccess = new jdbcDataAccess();
            dataAccess.patchPropertyLastInventoryDate(property);

            try {
                dataAccess.jdbcDataClose();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Send to pdf_generated view
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../pdf_generated/pdf_generated.fxml"));

            try {
                AnchorPane root;
                root = loader.load();

                views.pdf_generated.Controller controller = loader.getController();
                controller.setData();

                Scene nextScene = new Scene(root);
                Stage mainWindow = (Stage) terminateButton.getScene().getWindow();

                mainWindow.setScene(nextScene);
                mainWindow.show();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
