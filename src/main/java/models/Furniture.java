package models;

import datas.Persist;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

// CREATE TABLE `furniture` (
//   `id` BIGINT AUTO_INCREMENT NOT NULL,
//   `name` VARCHAR(100) NOT NULL,
//   `room_id` INT NOT NULL,
//   `position` ENUM NOT NULL,
//   CONSTRAINT `PRIMARY` PRIMARY KEY (`id`)
// );

@Entity
public class Furniture extends Persist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    // Enum
    private String position;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;

    public Furniture() {
    }

    public Furniture(Long id, String name, Room room, String position) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
