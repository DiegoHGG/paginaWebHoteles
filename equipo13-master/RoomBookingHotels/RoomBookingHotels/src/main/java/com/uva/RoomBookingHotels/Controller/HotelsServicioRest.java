package com.uva.RoomBookingHotels.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.uva.RoomBookingHotels.Exception.HotelsException;
import com.uva.RoomBookingHotels.Exception.RoomException;
import com.uva.RoomBookingHotels.Model.Hotel;
import com.uva.RoomBookingHotels.Model.Room;
import com.uva.RoomBookingHotels.Repository.HotelRepository;
import com.uva.RoomBookingHotels.Repository.RoomRepository;

@RestController
@RequestMapping("/RoomBooking")
@CrossOrigin(origins = "*")
public class HotelsServicioRest {
    private final HotelRepository repositoryHotel;
    private final RoomRepository repositoryRooms;
    private final RestTemplate restTemplate;

    @Value("${bookings.url}")
    private String bookingsUrl;

    HotelsServicioRest(HotelRepository repositoryHotel, RoomRepository repositoryRooms, RestTemplate restTemplate) {
        this.repositoryHotel = repositoryHotel;
        this.repositoryRooms = repositoryRooms;
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = { "/hotels/" })
    public List<Hotel> getHotels() {
        List<Hotel> hotel = repositoryHotel.findAll();
        return hotel;
    }

    @GetMapping(value = { "/hotels/{id}" })
    public Hotel getHotelByID(@PathVariable Integer id) {
        Hotel hotel = repositoryHotel.findById(id)
                .orElseThrow(() -> new HotelsException("No se ha encontrado el Hotel con id " + id));
        return hotel;
    }

    @PostMapping(value = "/hotels/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String newHotels(@RequestBody Hotel newHotel) {
        try {

            for (Room room : newHotel.getRoomList()) {
                room.setHotel(newHotel);
            }

            repositoryHotel.save(newHotel);
            return "Nuevo Hotel";
        } catch (Exception e) {
            throw new HotelsException("Error al crear el nuevo Hotel.");
        }
    }

    @DeleteMapping("/hotels/{id}")
    public String deleteHotel(@PathVariable Integer id, @RequestHeader("Authorization") String jwtToken) {
        try {
            // Obtener el hotel a eliminar
            Hotel hotel = repositoryHotel.findById(id).orElseThrow(() -> new HotelsException("Hotel no encontrado"));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", jwtToken);
    
            HttpEntity<String> entity = new HttpEntity<>(headers);
            // Eliminar las reservas asociadas a las habitaciones del hotel
            for (Room room : hotel.getRoomList()) {
                String roomId = room.getId().toString();

                try {
                    restTemplate.exchange(bookingsUrl + "bookings/room/" + roomId, HttpMethod.DELETE, entity,
                            String.class);

                    
                } catch (Exception e) {
                    System.out.println("Error al eliminar reservas para la habitación con ID: " + room.getId()
                            + ". Error: " + e.getMessage());
                }
            }
            repositoryHotel.deleteById(id);
            return "Hotel eliminado ";
        } catch (Exception e) {
            throw new HotelsException("Error al borrar el hotel.");
        }
    }

    @PatchMapping(value = "/hotels/{idHotel}/rooms/{idRoom}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String patchDispRoomID(@PathVariable Integer idHotel, @PathVariable Integer idRoom,
            @RequestBody Boolean available,
            @RequestHeader("Authorization") String jwtToken) {

        // Comprobar que existe la habitación
        Room room = repositoryRooms.findById(idRoom).orElseThrow(() -> new RoomException("Habitacion no encontrada"));

        if (!room.getHotel().getId().equals(idHotel))
            throw new RoomException("El hotel no tiene esa habitacion");

        // Si la habitacion tiene una reserva no se puede cambiar a disponible
        // Crear una cabecera personalizada con el token JWT
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        List<Map<String, Object>> reservas = restTemplate.exchange(
                bookingsUrl + "bookings/?startDate=" + LocalDate.now() + "&endDate=" + LocalDate.now() + "&roomId="
                        + idRoom,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                }).getBody();

        // Si hay una reserva y piden cambiar a no disponible, no se puede
        if (!room.isAvailable() && available && reservas != null && !reservas.isEmpty()) {
            throw new RoomException("La habitación tiene una reserva ahora mismo");
        }

        room.setAvailable(available);
        repositoryRooms.save(room);

        return "Habitación actualizado";
    }

    @GetMapping(value = { "/hotels/{id}/rooms/" })
    public List<Room> getRoomslByHotelId(@PathVariable Integer id,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestHeader("Authorization") String jwtToken) {

        // Obtener todas las habitaciones del hotel
        List<Room> roomList = repositoryRooms.findByHotelId(id);

        // Llamar al microservicio de reservas para saber todas las de las fechas
        // Crear una cabecera personalizada con el token JWT
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String bookingServiceUrl = bookingsUrl + "bookings/?startDate=" + startDate +
                "&endDate=" + endDate;
        ResponseEntity<List<Map<String, Object>>> response;
        try {
            response = restTemplate.exchange(
                    bookingServiceUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RoomException("Error al consultar reservas");
        }

        List<Map<String, Object>> bookings = response.getBody();

        // Obtener los IDs de las habitaciones reservadas
        Set<String> reservedRoomIds = bookings.stream()
                .map(booking -> (String) booking.get("room"))
                .collect(Collectors.toSet());

        // Filtrar las habitaciones disponibles
        return roomList.stream()
                .filter(room -> !reservedRoomIds.contains(room.getId().toString()))
                .collect(Collectors.toList());

    }

    @GetMapping(value = { "/hotels/{idHotel}/rooms/{idRoom}" })
    public Room getRoom(@PathVariable Integer idHotel, @PathVariable Integer idRoom) {
        Room room = repositoryRooms.findById(idRoom).orElseThrow(() -> new RoomException("Habitacion no encontrada"));
        if (room.getHotel().getId() != idHotel)
            throw new RoomException("El hotel no tiene esa habitacion");
        return room;
    }

}
