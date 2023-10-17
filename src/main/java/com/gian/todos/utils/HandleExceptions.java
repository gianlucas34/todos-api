package com.gian.todos.utils;

import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class HandleExceptions {
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
    var errors = e.getConstraintViolations()
        .stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.get(0));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getFieldError().getDefaultMessage());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMostSpecificCause().getMessage());
  }
}
