package com.example.cryptocurrency.controller;

import com.example.cryptocurrency.model.exception.InputInvalidException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExpcetionHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<String> handleMethodArgumentNotValid(
            ConstraintViolationException ex) {
        String errorMessage = "constraint validate failed: " + ex.toString();
        return new ResponseEntity<>(errorMessage,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler({InputInvalidException.class})
    protected ResponseEntity<String> handle(
            InputInvalidException ex) {
        String errorMessage ="Input is invalid " + ex.toString();
        return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
    }
}

