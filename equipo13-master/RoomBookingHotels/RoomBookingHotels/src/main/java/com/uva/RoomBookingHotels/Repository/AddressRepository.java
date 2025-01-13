package com.uva.RoomBookingHotels.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uva.RoomBookingHotels.Model.Address;


public interface AddressRepository extends JpaRepository<Address, Integer>{
}
