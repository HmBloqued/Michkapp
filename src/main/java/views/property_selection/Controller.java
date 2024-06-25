package views.property_selection;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import datas.jdbcDataAccess;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import models.Property;
import resources.components.property.PropertyController;

public class Controller implements Initializable {
    @FXML
    private VBox propertyTable;

    // At init get properties using jdbcDataAccess and display them in the table:
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        jdbcDataAccess dataAccess = new jdbcDataAccess();
        List<Property> properties = new ArrayList<Property>();
        try {
            properties = dataAccess.getProperties();
        } catch (SQLException e) {
            e.printStackTrace();
            // TODO: Display error message/toast
        }

        for (Property property : properties) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/components/property/property.fxml"));
            try {
                Parent component = loader.load();
                PropertyController controller = loader.getController();
                controller.setData(property);
                component.getStylesheets()
                        .add(getClass().getResource("/resources/components/property/styles.css").toExternalForm());
                        
                if (propertyTable != null) {
                    propertyTable.getChildren().add(component);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
