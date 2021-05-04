package com.example.restservice;


import org.springframework.http.HttpStatus;

public class UtilityHandler {

    public static void checkValueParameter(String parameter, String labelParameter) {
        if (parameter == null || parameter.isEmpty()) {
            throw new CustomException("field_not_found_or_empty", labelParameter, HttpStatus.BAD_REQUEST);
        }
    }

    public static void checkObjectIsNotNull(Car car) {
        if (car == null) {
            throw new CustomException("record_not_found", "id", HttpStatus.NOT_FOUND);
        }
    }

}
