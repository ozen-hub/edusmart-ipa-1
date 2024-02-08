package com.devstack.edu.dao.custom;

import com.devstack.edu.dao.CrudDao;
import com.devstack.edu.entity.StudentRegisterData;

import java.util.List;

public interface StudentRegisterDataDao extends CrudDao<StudentRegisterData,Long> {
    public List<StudentRegisterData> findAllData();
}
