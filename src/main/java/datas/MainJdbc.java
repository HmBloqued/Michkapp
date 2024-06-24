package datas;

import java.sql.SQLException;
import java.util.List;

import models.Property;
import models.User;
import models.Address;

public class MainJdbc {
    jdbcDataAccess dataAccess;

    public MainJdbc() throws SQLException {
        dataAccess = new jdbcDataAccess();
        // // Get all properties
        // List<Property> properties = dataAccess.getProperties();
        // for (Property property : properties) {
        //     System.out.println(property);
        // }

        // TODO: Define some properties and room etc...

        // // Close connection
        // dataAccess.jdbcDataClose();
        Address address = new Address(1, "rue de la paix", "75000", "1", "Paris", 1, 1);

    }

    public static void main(String[] args) throws SQLException {
        new MainJdbc();
    }
}
