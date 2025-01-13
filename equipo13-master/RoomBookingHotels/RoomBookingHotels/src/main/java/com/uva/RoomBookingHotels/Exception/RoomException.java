package com.uva.RoomBookingHotels.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class RoomException extends RuntimeException {
    public RoomException(String mensaje) {
        super(mensaje);
    }
}