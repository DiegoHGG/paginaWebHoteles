package com.uva.RoomBookingUsers.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.uva.RoomBookingUsers.Exception.UserException;
import com.uva.RoomBookingUsers.Model.User;
import com.uva.RoomBookingUsers.Model.UserStatus;
import com.uva.RoomBookingUsers.Repository.UserRepository;

@RestController
@RequestMapping("/RoomBooking")
@CrossOrigin(origins = "*")
public class UsersServiciosRest {
    private final UserRepository repository;
    private final RestTemplate restTemplate;

    @Value("${bookings.url}")
    private String bookingsUrl;

    UsersServiciosRest(UserRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    // Users
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<User> getUsers(@RequestParam String email) {
        return repository.findByEmail(email);
    }

    @GetMapping(value = "/users/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers() {
        List<User> users= repository.findAll();
        users.forEach(user -> user.setPassword(""));
        return users;
    }

    @GetMapping(value = { "/users/{id}" })
    public User getUserPorID(@PathVariable Integer id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserException("No se ha encontrado el user con id " + id));
        return user;
    }

    @PostMapping(value = "/users/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String newUser(@RequestBody User newUser) {
        try {
            newUser.setStatus(UserStatus.NOBOOKINGS);
            repository.save(newUser);
            return "Nuevo registro creado";
        } catch (Exception e) {
            throw new UserException("Error al crear el nuevo registro.");
        }
    }

    @PutMapping("/users/{id}")
    public String putUser(@PathVariable Integer id, @RequestBody User userActualizado) {
        User usuario = repository.findById(id).orElseThrow(() -> new UserException("Usuario no encontrado"));

        // Actualizar solo nombre y email
        usuario.setName(userActualizado.getName());
        usuario.setEmail(userActualizado.getEmail());
        usuario.setPassword(userActualizado.getPassword());

        repository.save(usuario);
        return "Usuario actualizado";
    }

    @PatchMapping("/users/{id}")
    public String putUserReservar(@PathVariable Integer id, @RequestBody User userActualizado,
            @RequestHeader("Authorization") String jwtToken) {
        User usuario = repository.findById(id).orElseThrow(() -> new UserException("Usuario no encontrado"));

        usuario.setStatus(userActualizado.getStatus());

        // Comprobar las reservas que tiene
        // Crear una cabecera personalizada con el token JWT
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        List<Map<String, Object>> reservasActivas = restTemplate.exchange(
                bookingsUrl + "bookings/user/" + id + "/active-bookings/",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                }).getBody();

        List<Map<String, Object>> reservas = restTemplate.exchange(
                bookingsUrl + "bookings/user/" + id,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                }).getBody();

        if (userActualizado.getStatus().compareTo(UserStatus.NOBOOKINGS) == 0 && reservas != null
                && reservas.isEmpty()) {
            repository.save(usuario);
        } else if (userActualizado.getStatus().compareTo(UserStatus.WITHACTIVEBOOKINGS) == 0 && reservas != null
                && !reservas.isEmpty() && reservasActivas != null && !reservasActivas.isEmpty()) {
            repository.save(usuario);
        } else if (userActualizado.getStatus().compareTo(UserStatus.WITHINACTIVEBOOKINGS) == 0
                && reservasActivas != null && reservasActivas.isEmpty() && reservas != null && !reservas.isEmpty()) {
            repository.save(usuario);
        } else {
            throw new UserException("Estado inválido: el estado no es consistente con las reservas del usuario");
        }

        return "Usuario actualizado";
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Integer id, @RequestHeader("Authorization") String jwtToken) {
        try {
            repository.findById(id).orElseThrow(() -> new UserException("Usuario no encontrado"));

            // Crear una cabecera personalizada con el token JWT
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", jwtToken);
    
            HttpEntity<String> entity = new HttpEntity<>(headers);
    
            // Llamar al servicio de reservas para eliminar todas las reservas del usuario
            restTemplate.exchange(
                    bookingsUrl + "bookings/user/" + id,
                    HttpMethod.DELETE,
                    entity,
                    Void.class
            );
            repository.deleteById(id);
            return "Nuevo registro eliminado";
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new UserException("Error al borrar el usuario.");
        }
    }

}
