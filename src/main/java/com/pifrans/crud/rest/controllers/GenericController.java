package com.pifrans.crud.rest.controllers;

import com.pifrans.crud.errors.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

public interface GenericController<E> {

    ResponseEntity<E> findById(UUID id) throws NotFoundException;

    ResponseEntity<List<E>> findAll();

    ResponseEntity<Page<E>> findByPage(Integer page, Integer linesPerPage, String orderBy, String direction);

    ResponseEntity<E> save(E body);

    ResponseEntity<List<E>> saveAll(List<E> body);

    ResponseEntity<E> update(E body) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    ResponseEntity<E> deleteById(UUID id);
}
