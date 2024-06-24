package models;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FurnitureStateInventoryId implements Serializable {
    private Integer inventoryId;
    private Integer furnitureId;

    public FurnitureStateInventoryId() {
    }

    public FurnitureStateInventoryId(Integer inventoryId, Integer furnitureId) {
        this.inventoryId = inventoryId;
        this.furnitureId = furnitureId;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getFurnitureId() {
        return furnitureId;
    }

    public void setFurnitureId(Integer furnitureId) {
        this.furnitureId = furnitureId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FurnitureStateInventoryId)) return false;
        FurnitureStateInventoryId that = (FurnitureStateInventoryId) o;
        return getInventoryId().equals(that.getInventoryId()) && getFurnitureId().equals(that.getFurnitureId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInventoryId(), getFurnitureId());
    }

    @Override
    public String toString() {
        return "FurnitureStateInventoryId{" +
                "inventoryId=" + inventoryId +
                ", furnitureId=" + furnitureId +
                '}';
    }
}