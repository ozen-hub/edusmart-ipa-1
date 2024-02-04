package com.devstack.edu.controller;

import com.devstack.edu.db.DbConnection;
import com.devstack.edu.model.Student;
import com.devstack.edu.util.GlobalVar;
import com.devstack.edu.view.tm.IncomeTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class DataBaseAccessCode {
    public boolean saveStudent(Student student) throws SQLException, ClassNotFoundException {
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
        preparedStatement.setString(6, GlobalVar.userEmail);

        return preparedStatement.executeUpdate()>0;
    }
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
}
