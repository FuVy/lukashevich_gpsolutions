package com.gpsolutions.lukashevich.exceptions.handlers;

import com.gpsolutions.lukashevich.domain.search.FailedSearchItem;
import com.gpsolutions.lukashevich.exceptions.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<?> handleJPAViolations(TransactionSystemException e) {
        ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();
        if (!(e.getCause().getCause() instanceof ConstraintViolationException ve)) {
            return responseEntity.build();
        }
        var errors = ve.getConstraintViolations().stream()
                .map(violation -> {
                    Map<String, String> errMap = new HashMap<>();
                    errMap.put(violation.getPropertyPath().toString(),
                            violation.getMessage());
                    return errMap;
                }).toList();
        return responseEntity.body(errors);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    ResponseEntity<?> handleMethodValidationErrors(HandlerMethodValidationException e) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (ParameterValidationResult paramResult : e.getParameterValidationResults()) {
            if (paramResult instanceof ParameterErrors parameterErrors) {
                for (FieldError fieldError : parameterErrors.getFieldErrors()) {
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage() == null ? "" : fieldError.getDefaultMessage());
                }
            } else {
                String parameterName = paramResult.getMethodParameter().getParameterName();
                if (parameterName == null) {
                    parameterName = "arg" + paramResult.getMethodParameter().getParameterIndex();
                }
                for (MessageSourceResolvable error : paramResult.getResolvableErrors()) {
                    errors.put(parameterName, error.getDefaultMessage() == null ? "" : error.getDefaultMessage());
                }
            }
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<?> handleNotReadableErrors(HttpMessageNotReadableException e) {
        return  ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<?> handleTypeMismatchErrors(MethodArgumentTypeMismatchException e) {
        return  ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> handleBindErrors(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(e.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        y -> y.getDefaultMessage() == null ? "" : y.getDefaultMessage()
                )));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnknownErrors(Exception e) {
        log.error("An unexpected error occurred.", e);
        return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<List<FailedSearchItem>> handleNotFoundErrors(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getFailedItems());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleNotFoundErrors(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
