package models;

import java.util.Date;

import datas.Persist;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Transient;

@Entity
public class FurnitureStateInventory extends Persist {
    @EmbeddedId
    private FurnitureStateInventoryId id;

    @ManyToOne
    @MapsId("inventoryId")
    @JoinColumn(name = "inventory_id", nullable = false, referencedColumnName = "id")
    private Inventory inventory;

    @ManyToOne
    @MapsId("furnitureId")
    @JoinColumn(name = "furniture_id", nullable = false, referencedColumnName = "id")
    private Furniture furniture;

    @Column(name = "datetime", nullable = false)
    private Date datetime;

    @Enumerated(EnumType.STRING)
    @Column(name = "furniture_state", nullable = false)
    private State furnitureState;

    @Lob
    @Column(name = "picture", columnDefinition = "LONGBLOB")
    private byte[] picture;

    @Column(name = "picture_date")
    private Date pictureDate;

    @Column(name = "comment", length = 250)
    private String comment;

    @Column(name = "picture_name")
    private String pictureName;

    public FurnitureStateInventory() {
    }

    public FurnitureStateInventory(Inventory inventory, Furniture furniture, Date datetime,
            State furnitureState,
            byte[] picture, Date pictureDate, String comment, String pictureName) {
        this.inventory = inventory;
        this.furniture = furniture;
        this.datetime = datetime;
        this.furnitureState = furnitureState;
        this.picture = picture;
        this.pictureDate = pictureDate;
        this.comment = comment;
        this.pictureName = pictureName;

        create(this);
    }

    public FurnitureStateInventory(Inventory inventory, Furniture furniture, Date datetime,
            State furnitureState) {
        this.inventory = inventory;
        this.furniture = furniture;
        this.datetime = datetime;
        this.furnitureState = furnitureState;

        create(this);
    }

    public FurnitureStateInventoryId getId() {
        return id;
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

    public String getPictureName() {
        return pictureName;
    }

    public Date getPictureDate() {
        return pictureDate;
    }

    public String getComment() {
        return comment;
    }

    public void setPicture(byte[] picture, String pictureName) {
        this.picture = picture;
        this.pictureName = pictureName;
    }

    @Override
    public String toString() {
        return "FurnitureStateInventory{" +
                "id=" + id +
                ", inventory=" + inventory +
                ", furniture=" + furniture +
                ", datetime=" + datetime +
                ", furnitureState=" + furnitureState +
                ", picture=" + picture +
                ", pictureDate=" + pictureDate +
                ", comment='" + comment + '\'' +
                ", pictureName='" + pictureName + '\'' +
                '}';
    }
}
