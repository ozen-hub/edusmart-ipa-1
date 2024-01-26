package com.devstack.edu.controller;

import com.devstack.edu.model.Student;
import com.devstack.edu.util.GlobalVar;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentFormController {
    public AnchorPane studentFormContext;
    public Button btnSaveUpdate;
    public TextField txtStudentName;
    public TextField txtAddress;
    public TextField txtEmail;
    public DatePicker dob;

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }
    private void setUi(String location) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml")));
        Stage stage = (Stage) studentFormContext.getScene().getWindow();
        stage.setTitle("EduSmart");
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void btnSaveUpdateOnAction(ActionEvent actionEvent) {

        Student student= new Student(0,txtStudentName.getText(),
                txtEmail.getText(),
                dob.getValue(),txtAddress.getText(),true);

        try{
            //1 step
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2 step
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/edusmart",
                    "root","1234");
            //3 step
            String query = "INSERT INTO student(student_name,email,dob,address,status,user_email)" +
                    " VALUES (?,?,?,?,?,?)";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //5 step
            preparedStatement.setString(1,student.getStudentName());
            preparedStatement.setString(2,student.getEmail());
            preparedStatement.setDate(3,student.getDate());
            preparedStatement.setString(4,student.getAddress());
            preparedStatement.setString(5, GlobalVar.userEmail);
            preparedStatement.setBoolean(5, student.isStatus());

            if(preparedStatement.executeUpdate()>0){
                new Alert(Alert.AlertType.INFORMATION, "Student was Saved!").show();
            }else{
                new Alert(Alert.AlertType.WARNING, "Try Again").show();
            }

        }catch (ClassNotFoundException | SQLException e){
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }
}
