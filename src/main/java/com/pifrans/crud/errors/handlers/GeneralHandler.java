package com.pifrans.crud.errors.handlers;

import com.pifrans.crud.domains.dtos.CommonErroDto;
import com.pifrans.crud.errors.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class GeneralHandler {
    private final HttpServletRequest request;


    @Autowired
    public GeneralHandler(HttpServletRequest request) {
        this.request = request;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CommonErroDto> handler(NotFoundException exception) {
        var commonErroDto = new CommonErroDto(exception.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(commonErroDto, HttpStatus.NOT_FOUND);
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<CommonErroDto>> handler(MethodArgumentNotValidException exception) {
        var commonErroDtos = new ArrayList<CommonErroDto>();
        var errors = exception.getBindingResult().getFieldErrors();

        errors.forEach(error -> {
            var errorMessage = error.getDefaultMessage();
            var field = error.getField();
            var finalMessage = String.format("Campo (%s), mensagem (%s)!", field, errorMessage);
            var problemDetail = new CommonErroDto(finalMessage, request.getRequestURI());

            commonErroDtos.add(problemDetail);
        });
        return new ResponseEntity<>(commonErroDtos, HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CommonErroDto> handler(DataIntegrityViolationException exception) {
        var commonErroDto = new CommonErroDto(Objects.requireNonNull(exception.getRootCause()).getMessage(), request.getRequestURI());
        return new ResponseEntity<>(commonErroDto, HttpStatus.CONFLICT);
    }
}
