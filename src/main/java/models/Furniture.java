package models;

import java.io.Serializable;
import java.util.List;

import datas.Persist;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

// CREATE TABLE `furniture` (
//   `id` BIGINT AUTO_INCREMENT NOT NULL,
//   `name` VARCHAR(100) NOT NULL,
//   `room_id` INT NOT NULL,
//   `position` ENUM NOT NULL,
//   CONSTRAINT `PRIMARY` PRIMARY KEY (`id`)
// );

@Entity
public class Furniture extends Persist implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "position")
    private String position;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;

    @OneToMany(mappedBy = "furniture", cascade = CascadeType.ALL)
    private List<FurnitureStateInventory> furnitureStateInventories;

    public Furniture() {
    }

    public Furniture(Integer id, String name, Room room, String position) {
        this.id = id;
        this.name = name;
        this.room = room;
        this.position = position;

        create(this);
    }

    public Furniture(String name, Room room, String position) {
        this.name = name;
        this.room = room;
        this.position = position;

        create(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Furniture{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", room=" + room +
                ", position='" + position + '\'' +
                '}';
    }
}
