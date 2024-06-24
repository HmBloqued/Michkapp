package datas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import models.Address;
import models.Furniture;
import models.FurnitureStateInventory;
import models.Inventory;
import models.Property;
import models.Role;
import models.Room;
import models.RoomType;
import models.State;
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
                "a.id as address_id, a.street_name, a.city, a.street_number, a.zip_code, a.floor, a.apartment_number, "
                +
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
                        result.getInt("apartment_number"));

                // Récupérer les données de l'utilisateur (propriétaire)
                User owner = new User(
                        result.getInt("user_id"),
                        result.getString("firstname"),
                        result.getString("lastname"),
                        Role.valueOf(result.getString("role")));

                // Récupérer les données de la propriété
                Property property = new Property(
                        result.getInt("property_id"),
                        address,
                        owner,
                        result.getDate("last_inventory_date"));

                properties.add(property);
            }
        } finally {
            if (result != null)
                result.close();
            if (statement != null)
                statement.close();
        }

        return properties;
    }

    // get property by id with adress and owner
    public Property getPropertyById(int id) throws SQLException {
        Property property = null;
        String query = "SELECT p.id as property_id, p.last_inventory_date as last_inventory_date" +
                "a.id as address_id, a.street_name, a.city, a.street_number, a.zip_code, a.floor, a.apartment_number, "
                +
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
                        result.getInt("apartment_number"));

                // Récupérer les données de l'utilisateur (propriétaire)
                User owner = new User(
                        result.getInt("user_id"),
                        result.getString("firstname"),
                        result.getString("lastname"),
                        Role.valueOf(result.getString("role")));

                // Récupérer les données de la propriété
                property = new Property(
                        result.getInt("property_id"),
                        address,
                        owner,
                        result.getDate("last_inventory_date"));
            }
        } finally {
            if (result != null)
                result.close();
            if (statement != null)
                statement.close();
        }

        return property;
    }

    public int createInventory(int propertyId, boolean ownerPresent, boolean occupantPresent) throws SQLException {
        // SQL query to insert a new record
        String query = "INSERT INTO Inventory (start_date, property_id, agent_id, occupant_id, is_owner_present, is_occupant_present ) VALUES (NOW(), ?, 1, 1, ?, ?)";

        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        int inventoryId = -1;

        try {
            connection = jdbcCreateConnection();
            preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Set the parameters
            preparedStatement.setInt(1, propertyId);
            preparedStatement.setBoolean(2, ownerPresent);
            preparedStatement.setBoolean(3, occupantPresent);

            // Execute the query
            int affectedRows = preparedStatement.executeUpdate();

            // Check if the insert was successful
            if (affectedRows > 0) {
                // Get the generated keys
                generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Retrieve the inventory id
                    inventoryId = generatedKeys.getInt(1);
                }
            }
        } finally {
            // Close resources
            if (generatedKeys != null)
                generatedKeys.close();
            if (preparedStatement != null)
                preparedStatement.close();
            if (connection != null)
                connection.close();
        }

        // Return the generated inventory id
        return inventoryId;
    }

    public boolean createFurnitureStatesInventory(Property property, int inventoryId) throws SQLException {
        String getRoomsQuery = "SELECT id FROM Room WHERE property_id = ?";
        String getFurnituresQuery = "SELECT id FROM Furniture WHERE room_id = ?";
        String insertFurnitureStateInventoryQuery = "INSERT INTO FurnitureStateInventory (inventory_id, furniture_id, datetime, furniture_state) VALUES (?, ?, NOW(), ?)";

        try (Connection connection = jdbcCreateConnection();
                PreparedStatement getRoomsStatement = connection.prepareStatement(getRoomsQuery);
                PreparedStatement getFurnituresStatement = connection.prepareStatement(getFurnituresQuery);
                PreparedStatement insertFurnitureStateInventoryStatement = connection
                        .prepareStatement(insertFurnitureStateInventoryQuery, Statement.RETURN_GENERATED_KEYS)) {

            // Get all the rooms from the property
            getRoomsStatement.setInt(1, property.getId());
            ResultSet roomsResultSet = getRoomsStatement.executeQuery();

            while (roomsResultSet.next()) {
                int roomId = roomsResultSet.getInt("id");

                // Get all the furniture from the room
                getFurnituresStatement.setInt(1, roomId);
                ResultSet furnituresResultSet = getFurnituresStatement.executeQuery();

                while (furnituresResultSet.next()) {
                    long furnitureId = furnituresResultSet.getLong("id");

                    // Set the parameters for inserting a new record
                    insertFurnitureStateInventoryStatement.setInt(1, inventoryId);
                    insertFurnitureStateInventoryStatement.setLong(2, furnitureId);
                    insertFurnitureStateInventoryStatement.setString(3, State.NEW.toString());

                    // Execute the insert statement
                    insertFurnitureStateInventoryStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // Get furniture by id

    // Get furniture state inventory by id

    // Get room by id

    // Get user by id

    // Patch furniture state inventory image (and image date)

    // Patch furniture state inventory comment

    // Patch furniture state inventory state

    public void jdbcDataClose() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
