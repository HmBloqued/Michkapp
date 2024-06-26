package models;

import java.util.List;

import datas.Persist;
import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User extends Persist{
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Property> properties;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL)
    private List<Inventory> inventoriesAsAgent;
    
    @OneToMany(mappedBy = "occupant", cascade = CascadeType.ALL)
    private List<Inventory> inventoriesAsOccupant;

    public User() {
    }

    public User(int id, String firstname, String lastname, Role role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;

        create(this);
    }

    public User(String firstname, String lastname, Role role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;

        create(this);
    }

    @Override
    public String toString() {
        return "User [firstname=" + firstname + ", id=" + id + ", lastname=" + lastname + ", properties=" + properties
                + ", role=" + role + "]";
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Integer getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }
}
