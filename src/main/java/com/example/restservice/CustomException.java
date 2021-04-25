package com.example.restservice;


import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

  private final String message;
  private final String parameter;
  private final HttpStatus statusCode;

  public CustomException(String message, String parameter, HttpStatus statusCode) {
    this.message = message;
    this.parameter = parameter;
    this.statusCode = statusCode;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public String getParameter() {
    return parameter;
  }

  public HttpStatus getStatusCode() {
    return statusCode;
  }


}
