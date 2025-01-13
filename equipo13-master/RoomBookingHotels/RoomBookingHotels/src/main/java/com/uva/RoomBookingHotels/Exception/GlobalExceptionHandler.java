package com.uva.RoomBookingHotels.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {


    
    @ExceptionHandler(HotelsException.class)
    public ResponseEntity<String> handleHotelException(HotelsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }


    @ExceptionHandler(RoomException.class)
    public ResponseEntity<String> handleUserException(RoomException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}