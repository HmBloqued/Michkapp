package datas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import models.Address;
import models.Property;
import models.User;

public class jdbcDataAccess {
    private static Connection connection;
    private static String url = "jdbc:mysql://localhost:3306/michka";
    private static String username = "root";
    private static String password = "Playmobil2004";

    public static Connection getConnection() throws SQLException {
        return connection;
    }

    public static Connection jdbcCreateConnection() throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
        System.out.println("Connection established");
        return connection;
    }

    public static ResultSet createStatement(String request) throws SQLException {
        return connection.createStatement().executeQuery(request);
    }

    public List<Property> getProperties() throws SQLException {
        List<Property> properties = new ArrayList<>();
        String query = "SELECT p.id as property_id, p.last_inventory_date as last_inventory_date, " +
                       "a.id as address_id, a.street_name, a.city, a.street_number, a.zip_code, a.floor, a.apartment_number, " +
                       "u.id as user_id, u.firstname, u.lastname, u.role " +
                       "FROM Property p " +
                       "JOIN Address a ON p.address_id = a.id " +
                       "JOIN User u ON p.owner_id = u.id";

        Statement statement = null;
        ResultSet result = null;

        try {
            connection = jdbcCreateConnection();
            statement = connection.createStatement();
            result = statement.executeQuery(query);

            while (result.next()) {
                // Récupérer les données de l'adresse
                Address address = new Address(
                    result.getInt("address_id"),
                    result.getString("street_name"),
                    result.getString("zip_code"),
                    result.getString("street_number"),
                    result.getString("city"),
                    result.getInt("floor"),
                    result.getInt("apartment_number")
                );

                // Récupérer les données de l'utilisateur (propriétaire)
                User owner = new User(
                    result.getInt("user_id"),
                    result.getString("firstname"),
                    result.getString("lastname"),
                    result.getString("role")
                );

                // Récupérer les données de la propriété
                Property property = new Property(
                    result.getInt("property_id"),
                    address,
                    owner,
                    result.getDate("last_inventory_date")
                );

                properties.add(property);
            }
        } finally {
            if (result != null) result.close();
            if (statement != null) statement.close();
        }

        return properties;
    }

    // get property by id with adress and owner
    public Property getPropertyById(int id) throws SQLException {
        Property property = null;
        String query = "SELECT p.id as property_id, p.last_inventory_date as last_inventory_date" +
                       "a.id as address_id, a.street_name, a.city, a.street_number, a.zip_code, a.floor, a.apartment_number, " +
                       "u.id as user_id, u.firstname, u.lastname, u.role " +
                       "FROM Property p " +
                       "JOIN Address a ON p.address_id = a.id " +
                       "JOIN User u ON p.owner_id = u.id " +
                       "WHERE p.id = " + id;

        Statement statement = null;
        ResultSet result = null;

        try {
            connection = jdbcCreateConnection();
            statement = connection.createStatement();
            result = statement.executeQuery(query);

            if (result.next()) {
                // Récupérer les données de l'adresse
                Address address = new Address(
                    result.getInt("address_id"),
                    result.getString("street_name"),
                    result.getString("zip_code"),
                    result.getString("street_number"),
                    result.getString("city"),
                    result.getInt("floor"),
                    result.getInt("apartment_number")
                );

                // Récupérer les données de l'utilisateur (propriétaire)
                User owner = new User(
                    result.getInt("user_id"),
                    result.getString("firstname"),
                    result.getString("lastname"),
                    result.getString("role")
                );

                // Récupérer les données de la propriété
                property = new Property(
                    result.getInt("property_id"),
                    address,
                    owner,
                    result.getDate("last_inventory_date")
                );
            }
        } finally {
            if (result != null) result.close();
            if (statement != null) statement.close();
        }

        return property;
    }

    public void jdbcDataClose() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
