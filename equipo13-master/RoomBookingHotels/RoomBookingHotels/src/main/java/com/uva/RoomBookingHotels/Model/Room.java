package com.uva.RoomBookingHotels.Model;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;

@Entity
@Table(name = "Room")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Room {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name="room_Number")
    private String roomNumber;
    @Enumerated(EnumType.STRING)
    @Column(name="room_Type")
    private RoomType roomType;
    @Column
    private boolean available;
    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Hotel hotel;



    public Room() {
    }


    public Room(String roomNumber, RoomType roomType, boolean available, Hotel hotel) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.available = available;
        this.hotel = hotel;
    }


    // Getters and Setters
    public String getRoomNumber() {
        return this.roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return this.roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
