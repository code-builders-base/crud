package com.pifrans.crud.rest.controllers;


import com.pifrans.crud.constants.ReflectMethods;
import com.pifrans.crud.rest.responses.SuccessResponse;
import com.pifrans.crud.services.GenericService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

@RestController
public abstract class GenericControllerImpl<E, C> implements GenericController<E> {
    private final GenericService<E> service;
    private final Class<E> classModel;
    private final Class<C> classController;
    private final HttpServletRequest request;


    protected GenericControllerImpl(GenericService<E> service, Class<E> classModel, Class<C> classController, HttpServletRequest request) {
        this.service = service;
        this.classModel = classModel;
        this.classController = classController;
        this.request = request;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<E> findById(@PathVariable UUID id) {
        var entity = service.findById(classModel, id);
        return new SuccessResponse<E>().handle(entity, classController, request, HttpStatus.OK);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<E>> findAll() {
        var entities = service.findAll();
        return new SuccessResponse<List<E>>().handle(entities, classController, request, HttpStatus.OK);
    }

    @Override
    @GetMapping("/page")
    public ResponseEntity<Page<E>> findByPage(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "24") Integer linesPerPage, @RequestParam(defaultValue = "id") String orderBy, @RequestParam(defaultValue = "ASC") String direction) {
        var entityPages = service.findByPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok().body(entityPages);
    }

    @Override
    @PostMapping
    public ResponseEntity<E> save(@RequestBody @Validated E body) {
        var entity = service.save(body);
        return new SuccessResponse<E>().handle(entity, classController, request, HttpStatus.CREATED);
    }

    @Override
    @PostMapping("/saveAll")
    public ResponseEntity<List<E>> saveAll(@RequestBody @Validated List<E> body) {
        var entities = service.saveAll(body);
        return new SuccessResponse<List<E>>().handle(entities, classController, request, HttpStatus.CREATED);
    }

    @Override
    @PutMapping
    public ResponseEntity<E> update(@RequestBody @Validated E body) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var method = body.getClass().getMethod(ReflectMethods.GET_ID.getDescription());
        var id = (UUID) method.invoke(body);
        var entity = service.update(body, id);
        return new SuccessResponse<E>().handle(entity, classController, request, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<E> deleteById(@PathVariable UUID id) {
        var entity = service.deleteById(classModel, id);
        return new SuccessResponse<E>().handle(entity, classController, request, HttpStatus.OK);
    }
}