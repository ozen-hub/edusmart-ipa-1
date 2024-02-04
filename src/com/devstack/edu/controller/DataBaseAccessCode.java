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
import java.util.List;

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
                            rs.getString(5),rs.getBoolean(6))
            );
        }

        return students;
    }
    public boolean deleteStudent(int id) throws SQLException, ClassNotFoundException {
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
}
