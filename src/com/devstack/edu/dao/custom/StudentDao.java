package com.devstack.edu.dao.custom;


import com.devstack.edu.entity.Student;

import java.sql.SQLException;
import java.util.List;

public interface StudentDao {
    public boolean saveStudent(Student student) throws SQLException, ClassNotFoundException;
    public boolean updateStudent(Student student, boolean isActive, int studentId) throws SQLException, ClassNotFoundException;
    public List<Student> findAllStudents(String searchText) throws SQLException, ClassNotFoundException;
    public boolean deleteStudent(int id) throws SQLException, ClassNotFoundException;
}
