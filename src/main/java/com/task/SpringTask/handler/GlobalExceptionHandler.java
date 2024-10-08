package com.task.SpringTask.handler;

import com.task.SpringTask.exception.InvalidAmountException;
import com.task.SpringTask.exception.InvalidPinCodeException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<String> handleInvalidAmountException(InvalidAmountException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid amount");
    }

    @ExceptionHandler(InvalidPinCodeException.class)
    public ResponseEntity<String> handleInvalidPinCodeException(InvalidPinCodeException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid pin code");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        if (isControllerException(e)) {
            if (e.getMessage().contains("pinCode"))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad pin code");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
    }

    private boolean isControllerException(ConstraintViolationException e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().toLowerCase().contains("controller")) {
                return true;
            }
        }
        return false;
    }
}
