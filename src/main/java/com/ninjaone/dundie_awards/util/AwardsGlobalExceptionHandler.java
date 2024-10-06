package com.ninjaone.dundie_awards.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AwardsGlobalExceptionHandler {
    @ExceptionHandler(EmployeeNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFound(EmployeeNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}
