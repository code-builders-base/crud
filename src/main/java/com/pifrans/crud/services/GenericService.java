package com.pifrans.crud.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface GenericService<E> {

    E findById(Class<E> tClass, UUID id);

    E save(E object) throws DataIntegrityViolationException;

    List<E> saveAll(List<E> list) throws DataIntegrityViolationException;

    E update(E object, UUID id) throws DataIntegrityViolationException;

    E deleteById(Class<E> tClass, UUID id);

    List<E> findAll();

    Page<E> findByPage(Integer page, Integer linesPerPage, String orderBy, String direction);
}
