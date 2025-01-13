package com.uva.RoomBookingBooking.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class BookingException extends RuntimeException {
    public BookingException(String mensaje) {
        super(mensaje);
    }
}