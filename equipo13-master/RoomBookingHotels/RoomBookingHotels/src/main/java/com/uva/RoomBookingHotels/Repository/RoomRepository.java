package com.uva.RoomBookingHotels.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.uva.RoomBookingHotels.Model.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    
    @Query("SELECT r FROM Room r WHERE r.hotel.id = ?1")
    List<Room> findByHotelId(Integer id);

}
