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
        // TODO: Temp, replace forced agent and occupant with true datas
        String query = "INSERT INTO Inventory (start_date, property_id, agent_id, occupant_id, is_owner_present, is_occupant_present ) VALUES (NOW(), ?, 1, 2, ?, ?)";

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
        // Get all rooms
        jdbcDataAccess dataAccess = new jdbcDataAccess();
        List<Room> rooms = dataAccess.getRoomsByProperty(property);

        // For each room get furnitures
        for (Room room : rooms) {
            List<Furniture> furnitures = dataAccess.getFurnituresByRoom(room);

            // Then get all furniture for each room
            for (Furniture furniture : furnitures) {

                // Then create a furniture state inventory for each furniture
                String query = "INSERT INTO FurnitureStateInventory (datetime, furniture_id, inventory_id, furniture_state) VALUES (NOW(), ?, ?, ?)";
                PreparedStatement preparedStatement = null;

                try {
                    connection = jdbcCreateConnection();
                    preparedStatement = connection.prepareStatement(query);

                    // Set the parameters
                    preparedStatement.setInt(1, furniture.getId());
                    preparedStatement.setInt(2, inventoryId);
                    preparedStatement.setString(3, State.NEW.toString());

                    // Execute the query
                    int affectedRows = preparedStatement.executeUpdate();

                    // Check if the insert was successful
                    if (affectedRows <= 0) {
                        return false;
                    }
                } finally {
                    // Close resources
                    if (preparedStatement != null)
                        preparedStatement.close();
                    if (connection != null)
                        connection.close();
                }
            }
        }
        return true;
    }

    public Inventory getInventoryById(int inventoryId, Property property) {

        Inventory inventory = null;
        String query = "SELECT i.id as inventory_id, i.start_date as inventory_start_date, i.agent_id as inventory_agent_id, i.occupant_id as inventory_occupant_id, i.is_owner_present as inventory_is_owner_present, i.is_occupant_present as inventory_is_occupant_present "
                + "FROM Inventory i "
                + "WHERE i.id = " + inventoryId;

        Statement statement = null;
        ResultSet result = null;

        try {
            connection = jdbcCreateConnection();
            statement = connection.createStatement();
            result = statement.executeQuery(query);

            if (result.next()) {
                inventory = new Inventory(
                        result.getInt("inventory_id"),
                        property,
                        // TODO: Temp because we are not handling agent and occupant yet
                        new User(),
                        new User(),
                        result.getDate("inventory_start_date"),
                        result.getBoolean("inventory_is_owner_present"),
                        result.getBoolean("inventory_is_occupant_present"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventory;
    }

    public List<Room> getRoomsByProperty(Property property) {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT r.id as room_id, r.name as room_name, r.room_type as room_type " +
                "FROM Room r " +
                "WHERE r.property_id = " + property.getId();

        Statement statement = null;
        ResultSet result = null;

        try {
            connection = jdbcCreateConnection();
            statement = connection.createStatement();
            result = statement.executeQuery(query);

            while (result.next()) {
                Room room = new Room(
                        result.getInt("room_id"),
                        property,
                        RoomType.valueOf(result.getString("room_type")),
                        result.getString("room_name"));

                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    public List<Furniture> getFurnituresByRoom(Room room) {
        List<Furniture> furnitures = new ArrayList<>();
        String query = "SELECT f.id as furniture_id, f.name as furniture_name, f.position as furniture_position " +
                "FROM Furniture f " +
                "WHERE f.room_id = " + room.getId();

        Statement statement = null;
        ResultSet result = null;

        try {
            connection = jdbcCreateConnection();
            statement = connection.createStatement();
            result = statement.executeQuery(query);

            while (result.next()) {
                Furniture furniture = new Furniture(
                        result.getInt("furniture_id"),
                        result.getString("furniture_name"),
                        room,
                        result.getString("furniture_position"));

                furnitures.add(furniture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return furnitures;
    }

    public void patchFurnitureStateInventoryImage(FurnitureStateInventory furnitureStateInventory, byte[] image, String imageName) {
        String query = "UPDATE FurnitureStateInventory SET picture = ?, picture_date = NOW(), datetime = NOW(), picture_name = ? WHERE furniture_id = ? AND inventory_id = ?";

        PreparedStatement preparedStatement = null;

        try {
            connection = jdbcCreateConnection();
            preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setBytes(1, image);
            preparedStatement.setString(2, furnitureStateInventory.getPictureName());
            preparedStatement.setInt(3, furnitureStateInventory.getFurniture().getId());
            preparedStatement.setInt(4, furnitureStateInventory.getInventory().getId());

            // Execute the query
            int affectedRows = preparedStatement.executeUpdate();

            // Check if the update was successful
            if (affectedRows <= 0) {
                System.out.println("Error updating the image");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void patchFurnitureStateInventoryComment(FurnitureStateInventory furnitureStateInventory, String comment) {
        String query = "UPDATE FurnitureStateInventory SET comment = ?, datetime = NOW() WHERE furniture_id = ? AND inventory_id = ?";

        PreparedStatement preparedStatement = null;

        try {
            connection = jdbcCreateConnection();
            preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setString(1, comment);
            preparedStatement.setInt(2, furnitureStateInventory.getFurniture().getId());
            preparedStatement.setInt(3, furnitureStateInventory.getInventory().getId());

            // Execute the query
            int affectedRows = preparedStatement.executeUpdate();

            // Check if the update was successful
            if (affectedRows <= 0) {
                System.out.println("Error updating the comment");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Patch furniture state inventory state
    public void patchFurnitureStateInventoryState(FurnitureStateInventory furnitureStateInventory, State state) {
        String query = "UPDATE FurnitureStateInventory SET furniture_state = ?, datetime = NOW() WHERE furniture_id = ? AND inventory_id = ?";

        PreparedStatement preparedStatement = null;

        try {
            connection = jdbcCreateConnection();
            preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setString(1, state.toString());
            preparedStatement.setInt(2, furnitureStateInventory.getFurniture().getId());
            preparedStatement.setInt(3, furnitureStateInventory.getInventory().getId());

            // Execute the query
            int affectedRows = preparedStatement.executeUpdate();

            // Check if the update was successful
            if (affectedRows <= 0) {
                System.out.println("Error updating the state");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public FurnitureStateInventory getFurnitureStateInventoryFromInventoryFurniture(Inventory inventory,
            Furniture furniture) {
        FurnitureStateInventory furnitureStateInventory = null;
        String query = "SELECT fsi.datetime as fsi_datetime, fsi.furniture_state as fsi_furniture_state, fsi.picture as fsi_picture, fsi.picture_date as fsi_picture_date, fsi.comment as fsi_comment, fsi.picture_name as fsi_picture_name "
                +
                "FROM FurnitureStateInventory fsi " +
                "WHERE fsi.furniture_id = " + furniture.getId() + " AND fsi.inventory_id = " + inventory.getId();

        Statement statement = null;
        ResultSet result = null;

        try {
            connection = jdbcCreateConnection();
            statement = connection.createStatement();
            result = statement.executeQuery(query);

            if (result.next()) {
                furnitureStateInventory = new FurnitureStateInventory(
                        inventory,
                        furniture,
                        result.getDate("fsi_datetime"),
                        State.valueOf(result.getString("fsi_furniture_state")),
                        result.getBytes("fsi_picture"),
                        result.getDate("fsi_picture_date"),
                        result.getString("fsi_comment"),
                        result.getString("fsi_picture_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return furnitureStateInventory;
    }

    public boolean deleteInventory(Inventory inventory) {
        try {
            // Delete furnitureStateInventory
            String query = "DELETE * FROM furnitureStateInventory WHERE inventory_id = ?";
            PreparedStatement preparedStatement = null;

            connection = jdbcCreateConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, inventory.getId());


            // Delete inventory
            query = "DELETE * FROM inventory WHERE id = ?";
            preparedStatement = null;
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, inventory.getId());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Delete inventory

        return true;
    }

    public void patchPropertyLastInventoryDate(Property property) {
        String query = "UPDATE Property SET last_inventory_date = NOW() WHERE id = ?";

        PreparedStatement preparedStatement = null;

        try {
            connection = jdbcCreateConnection();
            preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setInt(1, property.getId());

            // Execute the query
            int affectedRows = preparedStatement.executeUpdate();

            // Check if the update was successful
            if (affectedRows <= 0) {
                System.out.println("Error updating the date");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(User user){
        String query = "INSERT INTO User (id, firstname, lastname, role) VALUES (?, ?, ?, ?)";

        PreparedStatement preparedStatement = null;

        try {
            connection = jdbcCreateConnection();
            preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getFirstname());
            preparedStatement.setString(3, user.getLastname());
            preparedStatement.setString(4, user.getRole().toString());

            // Execute the query
            int affectedRows = preparedStatement.executeUpdate();

            // Check if the insert was successful
            if (affectedRows <= 0) {
                System.out.println("Error inserting the user");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addAddress(Address address){
        String query = "INSERT INTO Address (id, street_name, zip_code, street_number, city, floor, apartment_number) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = null;

        try {
            connection = jdbcCreateConnection();
            preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setInt(1, address.getId());
            preparedStatement.setString(2, address.getStreetName());
            preparedStatement.setString(3, address.getZipCode());
            preparedStatement.setString(4, address.getStreetNumber());
            preparedStatement.setString(5, address.getCity());

            // Execute the query
            int affectedRows = preparedStatement.executeUpdate();

            // Check if the insert was successful
            if (affectedRows <= 0) {
                System.out.println("Error inserting the address");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addProperty(Property property){
        String query = "INSERT INTO Property (id, address_id, owner_id) VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = null;

        try {
            connection = jdbcCreateConnection();
            preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setInt(1, property.getId());
            preparedStatement.setInt(2, property.getAddress().getId());
            preparedStatement.setInt(3, property.getOwner().getId());

            // Execute the query
            int affectedRows = preparedStatement.executeUpdate();

            // Check if the insert was successful
            if (affectedRows <= 0) {
                System.out.println("Error inserting the property");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addRoom(Room room){
        String query = "INSERT INTO Room (property_id, room_type, name) VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = null;

        try {
            connection = jdbcCreateConnection();
            preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setInt(1, room.getProperty().getId());
            preparedStatement.setString(2, room.getRoomType().toString());
            preparedStatement.setString(3, room.getName());

            // Execute the query
            int affectedRows = preparedStatement.executeUpdate();

            // Check if the insert was successful
            if (affectedRows <= 0) {
                System.out.println("Error inserting the room");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addFurniture(Furniture furniture){
        String query = "INSERT INTO Furniture (name, room_id, position) VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = null;

        try {
            connection = jdbcCreateConnection();
            preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setString(1, furniture.getName());
            preparedStatement.setInt(2, furniture.getRoom().getId());
            preparedStatement.setString(3, furniture.getPosition());

            // Execute the query
            int affectedRows = preparedStatement.executeUpdate();

            // Check if the insert was successful
            if (affectedRows <= 0) {
                System.out.println("Error inserting the furniture");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserById(int userId){
        String query = "SELECT * FROM User WHERE id = ?";

        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        User user = null;

        try {
            connection = jdbcCreateConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);

            result = preparedStatement.executeQuery();

            if (result.next()) {
                user = new User(
                        result.getInt("id"),
                        result.getString("firstname"),
                        result.getString("lastname"),
                        Role.valueOf(result.getString("role")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public void jdbcDataClose() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
