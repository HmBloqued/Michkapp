package models;

import java.util.List;

import datas.Persist;
import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User extends Persist{
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "role")
    private String role; // TODO: Add enum ?

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<Property> properties;

    public User() {
    }

    public User(int id, String firstname, String lastname, String role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }

    @Override
    public String toString() {
        return "User [firstname=" + firstname + ", id=" + id + ", lastname=" + lastname + ", properties=" + properties
                + ", role=" + role + "]";
    }
}
