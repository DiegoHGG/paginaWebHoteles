package com.uva.RoomBookingHotels.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;

@Entity
@Table(name = "Address")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Address {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name="street_Kind")
    private String streetKind;
    @Column(name="street_Name")
    private String streetName;
    @Column
    private Integer number;
    @Column(name="post_Code")
    private String postCode;
    @Column(name="other_Info")
    private String otherInfo;
    @OneToOne(mappedBy = "address")
    private Hotel hotelId;


    public Address() {
    }


    public Address(String streetKind, String streetName, Integer number, String postCode, String otherInfo, Hotel hotelId) {
        this.streetKind = streetKind;
        this.streetName = streetName;
        this.number = number;
        this.postCode = postCode;
        this.otherInfo = otherInfo;
        this.hotelId = hotelId;
    }


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreetKind() {
        return this.streetKind;
    }

    public void setStreetKind(String streetKind) {
        this.streetKind = streetKind;
    }

    public String getStreetName() {
        return this.streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Integer getNumber() {
        return this.number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getPostCode() {
        return this.postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getOtherInfo() {
        return this.otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

}
