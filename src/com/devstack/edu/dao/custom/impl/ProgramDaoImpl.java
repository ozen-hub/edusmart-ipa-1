package com.devstack.edu.dao.custom.impl;

import com.devstack.edu.dao.CrudUtil;
import com.devstack.edu.dao.custom.ProgramDao;
import com.devstack.edu.entity.Program;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProgramDaoImpl implements ProgramDao {
    @Override
    public boolean save(Program program) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Program program) {
        return false;
    }

    @Override
    public boolean delete(Long aLong) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public Program find(Long aLong) {
        return null;
    }

    @Override
    public List<Program> findAll() throws SQLException, ClassNotFoundException {
       List<Program> list = new ArrayList<>();
       ResultSet set= CrudUtil.execute("SELECT * FROM program p INNER JOIN program_content pc ON p.program_id=pc.program_program_id");
       while(set.next()){
           System.out.println(set);
           System.out.println(set.getString(1));
           /*list.add(
                   new Program(set.getLong(1),set.getInt(2),
                           set.getString(3),set.getDouble(4),
                           set.getString(5),set.getLong(6),
                           "")
           );*/
       }
       return null;
    }
}
