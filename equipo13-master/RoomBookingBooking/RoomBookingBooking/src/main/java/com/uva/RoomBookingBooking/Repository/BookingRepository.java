package com.uva.RoomBookingBooking.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.uva.RoomBookingBooking.Model.Booking;

import jakarta.transaction.Transactional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT b FROM Booking b WHERE b.roomID = ?1")
    List<Booking> findByRoom(String roomId);

    @Query("SELECT b FROM Booking b WHERE b.roomID = ?3 AND b.startDate <= ?2 AND b.endDate >= ?1")
    List<Booking> findByDateRangeAndRoom(LocalDate startDate, LocalDate endDate, String roomId);

    @Query("SELECT b FROM Booking b WHERE b.startDate <= ?2 AND b.endDate >= ?1 ORDER BY b.startDate DESC")
    List<Booking> findByDateRange(LocalDate startDate, LocalDate endDate);

    @Query("SELECT b FROM Booking b WHERE b.userID = ?1")
    List<Booking> findByUserID(String userId);

    @Query("SELECT b FROM Booking b WHERE b.userID = ?1 AND b.endDate > CURRENT_DATE ")
    List<Booking> findByUserIDactives(String userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Booking b WHERE b.userID = ?1")
    int deleteByUserId(String userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Booking b WHERE b.roomID = ?1")
    int deleteByRoomId(String roomId);
}
