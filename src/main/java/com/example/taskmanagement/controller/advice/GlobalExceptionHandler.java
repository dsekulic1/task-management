package com.example.taskmanagement.controller.advice;

import com.example.task.management.model.ErrorDto;
import com.example.taskmanagement.controller.exception.InvalidDateFormatException;
import com.example.taskmanagement.controller.exception.NotFoundException;
import com.example.taskmanagement.controller.exception.TaskProcessingException;
import com.example.taskmanagement.controller.exception.UnauthorizedException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<ErrorDto> handleInvalidDateFormat(InvalidDateFormatException ex) {
        ErrorDto errorDto = createErrorDto(ex.getMessage(), "BAD_REQUEST", HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(NotFoundException ex) {
        ErrorDto errorDto = createErrorDto(ex.getMessage(), "NOT_FOUND", HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDto> handleBadRequestException(BadRequestException ex) {
        ErrorDto errorDto = createErrorDto(ex.getMessage(), "BAD_REQUEST", HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDto> handleUnauthorizedException(UnauthorizedException ex) {
        ErrorDto errorDto = createErrorDto(ex.getMessage(), "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> handleRuntimeException(RuntimeException ex) {
        ErrorDto errorDto = createErrorDto(ex.getMessage(), "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorDto errorDto = createErrorDto(ex.getMessage(), "ILLEGAL_ARGUMENT", HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(TaskProcessingException.class)
    public ResponseEntity<ErrorDto> handleTaskProcessingException(TaskProcessingException ex) {
        ErrorDto errorDto = createErrorDto(ex.getMessage(), "TASK_PROCESSING_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }

    private ErrorDto createErrorDto(String message, String code, HttpStatus status) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(message);
        errorDto.setCode(code);
        errorDto.setStatus(String.valueOf(status));
        errorDto.setTimestamp(String.valueOf(LocalDateTime.now()));
        return errorDto;
    }
}
