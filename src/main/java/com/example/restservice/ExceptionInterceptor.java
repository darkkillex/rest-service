package com.example.restservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class ExceptionInterceptor extends RuntimeException {

    @ExceptionHandler(value = CustomException.class)
    @ResponseBody
    public ResponseEntity<String> handleAllExceptions(CustomException ex) {
        return errorResponse(ex.getMessage(), ex.getParameter(), ex.getStatusCode());
    }

    private static String createJson(String message, String parameter, HttpStatus httpStatus) {
        return "{\"message\" : \"" + message + "\"," +
                "\"parameter\" : \"" + parameter + "\"," +
                "\"status code\" : \"" + httpStatus  + "\"}";
    }

    private static ResponseEntity<String> errorResponse(String message, String parameter, HttpStatus httpStatus) {
        String json = createJson(message, parameter, httpStatus);
        return new ResponseEntity<>(json, httpStatus);
    }

}

