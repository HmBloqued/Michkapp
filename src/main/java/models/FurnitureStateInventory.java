package models;

import java.time.LocalDateTime;

import datas.Persist;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

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
    private LocalDateTime datetime;

    // TODO: Enum
    @Column(name = "furniture_state", nullable = false)
    private String furnitureState;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_date")
    private LocalDateTime pictureDate;

    @Column(name = "comment", length = 250)
    private String comment;

    public FurnitureStateInventory() {
    }

    public FurnitureStateInventory(Inventory inventory, Furniture furniture, LocalDateTime datetime,
            String furnitureState,
            byte[] picture, LocalDateTime pictureDate, String comment) {
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

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public String getFurnitureState() {
        return furnitureState;
    }

    public byte[] getPicture() {
        return picture;
    }

    public LocalDateTime getPictureDate() {
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
