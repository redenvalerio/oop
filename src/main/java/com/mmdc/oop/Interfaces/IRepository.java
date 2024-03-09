package com.mmdc.oop.Interfaces;

import java.util.List;

public interface IRepository<T, ID> {
    T findById(ID id);
    List<T> findAll();
    T save(T entity);
    T update(T entity);
    void delete(T entity);
}
