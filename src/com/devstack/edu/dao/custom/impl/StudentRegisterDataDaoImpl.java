package com.devstack.edu.dao.custom.impl;

import com.devstack.edu.dao.custom.StudentRegisterDataDao;
import com.devstack.edu.entity.StudentRegisterData;

import java.sql.SQLException;
import java.util.List;

public class StudentRegisterDataDaoImpl implements StudentRegisterDataDao {
    @Override
    public boolean save(StudentRegisterData studentRegisterData) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(StudentRegisterData studentRegisterData) {
        return false;
    }

    @Override
    public boolean delete(Long aLong) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public StudentRegisterData find(Long aLong) {
        return null;
    }

    @Override
    public List<StudentRegisterData> findAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<StudentRegisterData> findAllData() {
        return null;
    }
}
