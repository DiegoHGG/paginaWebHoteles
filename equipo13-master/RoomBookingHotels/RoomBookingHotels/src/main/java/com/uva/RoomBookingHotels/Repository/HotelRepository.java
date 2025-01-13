package com.uva.RoomBookingHotels.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uva.RoomBookingHotels.Model.Hotel;


public interface HotelRepository extends JpaRepository<Hotel, Integer>{
}

