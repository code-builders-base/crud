package com.pifrans.crud.services;

import com.pifrans.crud.errors.exceptions.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public abstract class GenericServiceImpl<E> implements GenericService<E> {
    private final JpaRepository<E, UUID> repository;


    protected GenericServiceImpl(JpaRepository<E, UUID> repository) {
        this.repository = repository;
    }

    @Override
    public E findById(Class<E> tClass, UUID id) {
        String message = String.format("Nenhum item (%s) de ID (%s) encontrado!", tClass.getSimpleName(), id);
        return repository.findById(id).orElseThrow(() -> new NotFoundException(message));
    }

    @Override
    public E save(E object) throws DataIntegrityViolationException {
        return repository.save(object);
    }

    @Override
    public List<E> saveAll(List<E> list) throws DataIntegrityViolationException {
        return repository.saveAll(list);
    }

    @Override
    public E update(E object, UUID id) throws DataIntegrityViolationException {
        if (repository.existsById(id)) {
            return repository.save(object);
        }
        String message = String.format("Não foi possível atualizar (%s) de ID (%s), não encontrado!", object.getClass().getSimpleName(), id);
        throw new NotFoundException(message);
    }

    @Override
    public E deleteById(Class<E> tClass, UUID id) {
        String message = String.format("Não foi possível excluir o item, pois não existe (%s) com ID (%s)!", tClass.getSimpleName(), id);
        E object = repository.findById(id).orElseThrow(() -> new NotFoundException(message));

        repository.deleteById(id);
        return object;
    }

    @Override
    public List<E> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<E> findByPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return repository.findAll(pageRequest);
    }
}