package com.uva.roomBookingAuth.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.uva.roomBookingAuth.Exception.UserException;
import com.uva.roomBookingAuth.Model.JwtUtil;
import com.uva.roomBookingAuth.Model.User;

@RestController
@RequestMapping("/RoomBooking")
@CrossOrigin(origins = "*")
public class AuthRest {
    private final JwtUtil jwt;
    private final RestTemplate restTemplate;

    @Value("${users.url}")
    private String usersUrl;

    AuthRest(JwtUtil jwt, RestTemplate restTemplate) {
        this.jwt = jwt;
        this.restTemplate = restTemplate;
    }

    @PostMapping(value = "/register/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String newUser(@RequestBody User newUser) {
        try {
            // Crear los encabezados
            long expirationTime = 1000 * 60 * 60; // 1 hora de expiración

            String jwtprov = this.jwt.generateToken("", "", expirationTime);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + jwtprov); // Agregar el token JWT

            // Crear la entidad HTTP con encabezados y cuerpo
            HttpEntity<User> requestEntity = new HttpEntity<>(newUser,headers);


            restTemplate.postForEntity(usersUrl + "/users/",
            requestEntity,Void.class);

            // Generar el token
            String jwt = this.jwt.generateToken(newUser.getEmail(), newUser.getName(), expirationTime);

            return jwt;
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new UserException("Error.");
        }
    }

    @PostMapping(value = "/login/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String loginUser(@RequestBody User user) {
        try {
            // Crear los encabezados
            long expirationTime = 1000 * 60 * 60; // 1 hora de expiración
            String jwtprov = this.jwt.generateToken("juan", "alberto", expirationTime);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtprov); // Agregar el token JWT al encabezado

            // Crear la entidad con los encabezados
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Realizar la solicitud GET con encabezados
            ResponseEntity<User> userResponse = restTemplate.exchange(
                    usersUrl + "/users?email=" + user.getEmail(),
                    HttpMethod.GET,
                    entity,
                    User.class);
            User userDB = userResponse.getBody();
            if (userDB!=null) {
                
                // Comparar la contraseña cifrada almacenada con la nueva
                if (!userDB.getPassword().equals(user.getPassword())) {
                    return "Contraseña incorrecta";
                }

                // Generar el token
                String jwt = this.jwt.generateToken(user.getEmail(), user.getName(), expirationTime);

                return jwt;
            } else {
                return "Usuario no encontrado";
            }
        } catch (Exception e) {
            throw new UserException("Error al iniciar sesión en el usuario");
        }
    }

}
