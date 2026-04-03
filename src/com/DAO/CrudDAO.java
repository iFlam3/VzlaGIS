package com.DAO;

import java.util.List;

public interface CrudDAO<T, ID> {

    boolean create(T entity);

    T findById(ID id);

    List<T> findAll();

    boolean update(T entity);

    boolean delete(ID id);
}