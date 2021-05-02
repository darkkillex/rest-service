package com.example.restservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = CustomException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleAllExceptions(CustomException ex) {
        return errorResponse(ex.getMessage(), ex.getParameter(), ex.getStatusCode());
    }

    private static Map<String, String> createJson(String message, String parameter, HttpStatus httpStatus) {
        HashMap<String, String> body = new HashMap<>();
        body.put("message", message);
        body.put("parameter", parameter);
        body.put("status_code", httpStatus.toString());
        return body;
    }

    private static ResponseEntity<Map<String, String>> errorResponse(String message, String parameter, HttpStatus httpStatus) {
        Map<String, String> json = createJson(message, parameter, httpStatus);
        return new ResponseEntity<Map<String, String> >(json, httpStatus);
    }

}

