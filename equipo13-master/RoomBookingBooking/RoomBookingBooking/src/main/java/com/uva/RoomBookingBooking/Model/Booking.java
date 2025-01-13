package com.uva.RoomBookingBooking.Model;

import java.time.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;

@Entity
@Table(name = "Booking")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Booking {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name="start_Date")
    private LocalDate startDate;
    @Column(name="end_Date")
    private LocalDate endDate;
    @Column(name="user_ID")
    private String userID;
    @Column(name="name_user")
    private String nameUser;
    @Column(name="room_ID")
    private String roomID;
    @Column(name="number_room")
    private String numberRoom;
    @Column(name="name_hotel")
    private String nameHotel;

    
    public Booking(LocalDate startDate, LocalDate endDate, String userID, String nameUser, String roomID, String numberRoom, String nameHotel) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.userID = userID;
        this.nameUser = nameUser;
        this.roomID = roomID;
        this.numberRoom = numberRoom;
        this.nameHotel = nameHotel;
    }

    public Booking() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getUser() {
        return this.userID;
    }

    public void setUser(String user) {
        this.userID = user;
    }

    public String getRoom() {
        return this.roomID;
    }

    public void setRoom(String room) {
        this.roomID = room;
    }

    public String getNameUser() {
        return this.nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getNumberRoom() {
        return this.numberRoom;
    }

    public void setNumberRoom(String numberRoom) {
        this.numberRoom = numberRoom;
    }

    public String getNameHotel() {
        return this.nameHotel;
    }

    public void setNameHotel(String nameHotel) {
        this.nameHotel = nameHotel;
    }

}
