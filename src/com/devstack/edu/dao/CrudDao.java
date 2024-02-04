package com.devstack.edu.dao;

import java.sql.SQLException;
import java.util.List;

public interface CrudDao<T, ID> {
    public boolean save(T t) throws SQLException, ClassNotFoundException;
    public boolean update(T t);
    public boolean delete(ID id) throws SQLException, ClassNotFoundException;
    public T find(ID id);
    public List<T> findAll();
}
