package com.task.SpringTask.exception;

/**
 * Класс-исключение для неправильной суммы
 */
public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException(String message){
        super(message);
    }
}
