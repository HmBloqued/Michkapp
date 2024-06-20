package models;

import java.util.Date;

import datas.Persist;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

// CREATE TABLE `inventory` (
//   `id` INT AUTO_INCREMENT NOT NULL,
//   `property_id` INT NOT NULL,
//   `agent_id` INT NOT NULL,
//   `occupant_id` INT NOT NULL,
//   `start_date` DATETIME NOT NULL,
//   `is_occupant_present` TINYINT NOT NULL,
//   `is_owner_present` TINYINT NOT NULL,
//   CONSTRAINT `PRIMARY` PRIMARY KEY (`id`)
// );

@Entity
public class Inventory extends Persist{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "property_id", referencedColumnName = "id")
    private Property property;

    @ManyToOne
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    private User agent;

    @ManyToOne
    @JoinColumn(name = "occupant_id", referencedColumnName = "id")
    private User occupant;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "is_occupant_present")
    private Boolean isOccupantPresent;

    @Column(name = "is_owner_present")
    private Boolean isOwnerPresent;

    public Inventory() {
    }

    public Inventory(Integer id, Property property, User agent, User occupant, Date startDate, Boolean isOccupantPresent,
            Boolean isOwnerPresent) {
        this.id = id;
        this.property = property;
        this.agent = agent;
        this.occupant = occupant;
        this.startDate = startDate;
        this.isOccupantPresent = isOccupantPresent;
        this.isOwnerPresent = isOwnerPresent;

        create(this);
    }

    public Inventory(Property property, User agent, User occupant, Date startDate, Boolean isOccupantPresent,
            Boolean isOwnerPresent) {
        this.property = property;
        this.agent = agent;
        this.occupant = occupant;
        this.startDate = startDate;
        this.isOccupantPresent = isOccupantPresent;
        this.isOwnerPresent = isOwnerPresent;

        create(this);
    }

    public Integer getId() {
        return id;
    }

    public Property getProperty() {
        return property;
    }

    public User getAgent() {
        return agent;
    }

    public User getOccupant() {
        return occupant;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Boolean getIsOccupantPresent() {
        return isOccupantPresent;
    }

    public Boolean getIsOwnerPresent() {
        return isOwnerPresent;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }

    public void setOccupant(User occupant) {
        this.occupant = occupant;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setIsOccupantPresent(Boolean isOccupantPresent) {
        this.isOccupantPresent = isOccupantPresent;
    }

    public void setIsOwnerPresent(Boolean isOwnerPresent) {
        this.isOwnerPresent = isOwnerPresent;
    }

    @Override
    public String toString() {
        return "Inventory [agent=" + agent + ", id=" + id + ", isOccupantPresent=" + isOccupantPresent
                + ", isOwnerPresent=" + isOwnerPresent + ", occupant=" + occupant + ", property=" + property
                + ", startDate=" + startDate + "]";
    }
}
