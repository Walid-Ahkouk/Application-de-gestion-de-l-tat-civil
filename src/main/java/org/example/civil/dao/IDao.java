package org.example.civil.dao;

import java.util.List;

public interface IDao<T> {
    void create(T o);
    T findById(int id);
    List<T> findAll();
}
