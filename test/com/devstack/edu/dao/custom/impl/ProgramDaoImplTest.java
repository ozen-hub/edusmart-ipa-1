package com.devstack.edu.dao.custom.impl;


import com.devstack.edu.entity.Program;

import java.sql.SQLException;
import java.util.List;

class ProgramDaoImplTest {

    public static void main(String[] args) {
        new ProgramDaoImplTest().findAll();
    }

    void findAll() {
        try {
           new ProgramDaoImpl().findAll();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
