package models;

import datas.Persist;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

// CREATE TABLE `room` ( 
//   `id` INT AUTO_INCREMENT NOT NULL,
//   `property_id` INT NOT NULL,
//   `room_type` ENUM NOT NULL,
//   `name` VARCHAR(250) NOT NULL,
//   CONSTRAINT `PRIMARY` PRIMARY KEY (`id`)
// );

@Entity
public class Room extends Persist{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "property_id", referencedColumnName = "id")
    private Property property;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type")
    private RoomType roomType;

    @Column(name = "name")
    private String name;

    public Room() {
    }

    public Room(Integer id, Property property, RoomType roomType, String name) {
        this.id = id;
        this.property = property;
        this.roomType = roomType;
        this.name = name;
    }

    public Room(Property property, RoomType roomType, String name) {
        this.property = property;
        this.roomType = roomType;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
