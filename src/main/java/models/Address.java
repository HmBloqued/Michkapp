package models;

import java.util.List;

import datas.Persist;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "address")
public class Address extends Persist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "street_name")
    private String streetName;
    @Column(name = "zip_code")
    private String zipCode;
    @Column(name = "street_number")
    private String streetNumber;
    private String city;
    private Integer floor;

    @Column(name = "apartment_number")
    private Integer apartmentNumber;

    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL)
    private List<Property> properties;

    public Address() {
    }

    public Address(Integer id, String streetName, String zipCode, String streetNumber, String city, Integer floor,
            Integer apartmentNumber) {
        this.id = id;
        this.streetName = streetName;
        this.zipCode = zipCode;
        this.streetNumber = streetNumber;
        this.city = city;
        this.floor = floor;
        this.apartmentNumber = apartmentNumber;

        create(this);
    }

    @Override
    public String toString() {
        return "Address [apartmentNumber=" + apartmentNumber + ", city=" + city + ", floor=" + floor + ", id=" + id
                + ", properties=" + properties + ", streetName=" + streetName + ", streetNumber=" + streetNumber
                + ", zipCode=" + zipCode + "]";
    }

    public Integer getId() {
        return id;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getCity() {
        return city;
    }

    public Integer getFloor() {
        return floor;
    }

    public Integer getApartmentNumber() {
        return apartmentNumber;
    }

    public List<Property> getProperties() {
        return properties;
    }
}