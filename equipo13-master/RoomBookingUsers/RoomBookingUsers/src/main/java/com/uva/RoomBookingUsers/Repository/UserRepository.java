package com.uva.RoomBookingUsers.Repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uva.RoomBookingUsers.Model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    Optional<User> findByEmail(String email);
}

