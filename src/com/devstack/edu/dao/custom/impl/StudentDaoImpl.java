package com.devstack.edu.dao.custom.impl;

import com.devstack.edu.dao.custom.StudentDao;
import com.devstack.edu.db.DbConnection;
import com.devstack.edu.entity.Student;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {


    @Override
    public boolean updateStudent(Student student, boolean isActive, int studentId) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        //3 step
        String query = "UPDATE student SET student_name=?, email=?, dob=?,address=?, status=?" +
                " WHERE student_id=?";
        //4 step
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        //5 step
        preparedStatement.setString(1,student.getStudentName());
        preparedStatement.setString(2,student.getEmail());
        preparedStatement.setDate(3,java.sql.Date.valueOf(student.getDate()));
        preparedStatement.setString(4,student.getAddress());
        preparedStatement.setBoolean(5,isActive);
        preparedStatement.setInt(6,studentId);

        return preparedStatement.executeUpdate()>0;
    }

    @Override
    public List<Student> findAllStudents(String searchText) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        //3 step
        String query = "SELECT * FROM student WHERE student_name LIKE ? OR email LIKE ?";
        //4 step
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        //5 step
        preparedStatement.setString(1,searchText);
        preparedStatement.setString(2,searchText);

        ResultSet rs = preparedStatement.executeQuery();
        ArrayList<Student> students = new ArrayList<>();
        while (rs.next()){
            students.add(
                    new Student(rs.getInt(1),rs.getString(2),
                            rs.getString(3),
                            LocalDate.parse(rs.getString(4)),
                            rs.getString(5),rs.getBoolean(6),
                            rs.getString(7))
            );
        }

        return students;
    }



    @Override
    public boolean save(Student student) throws SQLException, ClassNotFoundException {
        //1 step
        Connection connection = DbConnection.getInstance().getConnection();
        //3 step
        String query = "INSERT INTO student(student_name,email,dob,address,status,user_email)" +
                " VALUES (?,?,?,?,?,?)";
        //4 step
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        //5 step
        preparedStatement.setString(1,student.getStudentName());
        preparedStatement.setString(2,student.getEmail());
        preparedStatement.setDate(3,java.sql.Date.valueOf(student.getDate()));
        preparedStatement.setString(4,student.getAddress());
        preparedStatement.setBoolean(5, student.isStatus());
        preparedStatement.setString(6, student.getUserEmail());

        return preparedStatement.executeUpdate()>0;
    }

    @Override
    public boolean update(Student student) {
        return false;
    }

    @Override
    public boolean delete(Integer id) throws SQLException, ClassNotFoundException {
        //1 step
        Connection connection1 = DbConnection.getInstance().getConnection();
        //3 step
        String query1 = "DELETE FROM student WHERE student_id=?";
        //4 step
        PreparedStatement preparedStatement1 = connection1.prepareStatement(query1);
        //5 step
        preparedStatement1.setInt(1,id);
        return preparedStatement1.executeUpdate()>0;
    }

    @Override
    public Student find(Integer integer) {
        return null;
    }

    @Override
    public List<Student> findAll() {
        return null;
    }
}
