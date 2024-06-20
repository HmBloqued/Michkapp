package models;

import java.text.DateFormat;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    private Date lastInventoryDate;

    public Property() {
    }

    public Property(Integer id, Address address, User owner, Date lastInventoryDate) {
        this.id = id;
        this.address = address;
        this.owner = owner;
        this.lastInventoryDate = lastInventoryDate;
    }

    @Override
    public String toString() {
        return "Property [address=" + address + ", id=" + id + ", lastInventoryDate=" + lastInventoryDate + ", owner="
                + owner + "]";
    }

    public Address getAddress() {
        return address;
    }

    // TODO: Delete ?
    public User getOwner() {
        return owner;
    }

    public Date getLastInventoryDate() {
        return lastInventoryDate;
    }

    
    
}