package com.uva.RoomBookingHotels.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class HotelsException extends RuntimeException {
    public HotelsException(String mensaje) {
        super(mensaje);
    }
}