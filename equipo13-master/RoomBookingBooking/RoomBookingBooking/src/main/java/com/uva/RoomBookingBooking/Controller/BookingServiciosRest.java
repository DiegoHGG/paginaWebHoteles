package com.uva.RoomBookingBooking.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.uva.RoomBookingBooking.Exception.BookingException;
import com.uva.RoomBookingBooking.Model.Booking;
import com.uva.RoomBookingBooking.Repository.BookingRepository;

@RestController
@RequestMapping("/RoomBooking")
@CrossOrigin(origins = "*")
public class BookingServiciosRest {
    private final BookingRepository repository;

    @Value("${users.url}")
    private String usersUrl;

    @Value("${hotels.url}")
    private String hotelsUrl;

    BookingServiciosRest(BookingRepository repository) {
        this.repository = repository;
    }

    // Booking
    @GetMapping("/bookings/")
    public List<Booking> getBookings(@RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String roomId) {
        List<Booking> bookings = null;
        if ((startDate == null) && (roomId == null) && (endDate == null))
            bookings = repository.findAll();
        else if ((startDate != null) && (roomId != null) && (endDate != null)) {
            bookings = repository.findByDateRangeAndRoom(startDate,endDate, roomId);
        } else if (startDate == null && endDate == null) {
            bookings = repository.findByRoom(roomId);
        } else
            bookings = repository.findByDateRange(startDate, endDate);
        
        return bookings;
    }

    @GetMapping("/bookings/user/{id}/active-bookings/")
    public List<Booking> getBookingsActivesUser(@PathVariable String id) {
        List<Booking> bookings = repository.findByUserIDactives(id);

        return bookings;
    }

    @GetMapping("/bookings/user/{id}")
    public List<Booking> getBookingsUser(@PathVariable String id) {
        List<Booking> bookings = repository.findByUserID(id);

        return bookings;
    }

    @GetMapping(value = { "/bookings/{id}" })
    public Booking getBookingByID(@PathVariable Integer id) {
        Booking booking = repository.findById(id)
                .orElseThrow(() -> new BookingException("No se ha encontrado la reserva con id " + id));
        return booking;
    }

    @PostMapping(value = "/bookings/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String newBooking(@RequestBody Booking newBookingRequest) {
        try {
            
            // Crear la nueva reserva
            Booking newBooking = new Booking();
            newBooking.setStartDate(newBookingRequest.getStartDate());
            newBooking.setEndDate(newBookingRequest.getEndDate());
            newBooking.setUser(newBookingRequest.getUser());
            newBooking.setRoom(newBookingRequest.getRoom());
            newBooking.setNameHotel(newBookingRequest.getNameHotel());
            newBooking.setNameUser(newBookingRequest.getNameUser());
            newBooking.setNumberRoom(newBookingRequest.getNumberRoom());

            repository.save(newBooking);
            return "Nueva reserva creada";
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la reserva: " + e.getMessage());
        }
    }

    @DeleteMapping("bookings/user/{userId}")
    public String deleteBookingsByUserId(@PathVariable String userId) {
        try {
            int reservas = repository.deleteByUserId(userId);
            return "Se eliminaron " + reservas + " reservas para el usuario con ID: " + userId;
        } catch (Exception e) {
            throw new RuntimeException("Error al borrar las reservas para el usuario con ID: " + userId, e);
        }
    }
    @DeleteMapping("bookings/room/{roomId}")
    public String deleteBookingsByRoomId(@PathVariable String roomId) {
        try {
            int reservas = repository.deleteByRoomId(roomId);
            return "Se eliminaron " + reservas + " reservas de la room con ID: " + roomId;
        } catch (Exception e) {
            throw new RuntimeException("Error al borrar las reservas para el usuario con ID: " + roomId, e);
        }
    }
}
