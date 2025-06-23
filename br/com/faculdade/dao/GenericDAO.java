package br.com.faculdade.dao;

import java.sql.SQLException;
import java.util.List;


public interface GenericDAO<T> {
    void save(T entity) throws SQLException;
    void update(T entity) throws SQLException;
    void delete(int id) throws SQLException;
    T findById(int id) throws SQLException;
    List<T> findAll() throws SQLException;
}