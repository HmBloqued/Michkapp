package models;

import java.util.Date;

import datas.Persist;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

// CREATE TABLE `furniture_state_inventory` (
//   `inventory_id` INT NOT NULL,
//   `furniture_id` BIGINT NOT NULL,
//   `datetime` DATETIME NOT NULL,
//   `furniture_state` ENUM NOT NULL,
//   `picture` BLOB NULL,
//   `picture_date` DATETIME NULL,
//   `comment` VARCHAR(250) NULL,
//   CONSTRAINT `PRIMARY` PRIMARY KEY (`inventory_id`, `furniture_id`)
// );

@Entity
public class FurnitureStateInventory extends Persist {
    @Id
    @Column(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @Id
    @Column(name = "furniture_id", nullable = false)
    private Furniture furniture;

    @Column(name = "datetime", nullable = false)
    private Date datetime;

    @Enumerated(EnumType.STRING)
    @Column(name = "furniture_state", nullable = false)
    private State furnitureState;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_date")
    private Date pictureDate;

    @Column(name = "comment", length = 250)
    private String comment;

    public FurnitureStateInventory() {
    }

    public FurnitureStateInventory(Inventory inventory, Furniture furniture, Date datetime,
            State furnitureState,
            byte[] picture, Date pictureDate, String comment) {
        this.inventory = inventory;
        this.furniture = furniture;
        this.datetime = datetime;
        this.furnitureState = furnitureState;
        this.picture = picture;
        this.pictureDate = pictureDate;
        this.comment = comment;

        create(this);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Furniture getFurniture() {
        return furniture;
    }

    public Date getDatetime() {
        return datetime;
    }

    public State getFurnitureState() {
        return furnitureState;
    }

    public byte[] getPicture() {
        return picture;
    }

    public Date getPictureDate() {
        return pictureDate;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "FurnitureStateInventory [comment=" + comment + ", datetime=" + datetime + ", furniture=" + furniture
                + ", furnitureState=" + furnitureState + ", inventory=" + inventory + ", picture=" + picture
                + ", pictureDate=" + pictureDate + "]";
    }
}
