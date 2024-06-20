package datas;

import java.sql.SQLException;
import java.util.List;

import models.Property;
import models.User;

public class MainJdbc {
    jdbcDataAccess dataAccess;

    public MainJdbc() throws SQLException {
        dataAccess = new jdbcDataAccess();
        // Get all properties
        List<Property> properties = dataAccess.getProperties();
        for (Property property : properties) {
            System.out.println(property);
        }

        // Close connection
        dataAccess.jdbcDataClose();

    }

    public static void main(String[] args) throws SQLException {
        new MainJdbc();
    }
}
