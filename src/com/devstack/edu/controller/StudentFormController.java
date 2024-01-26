package com.devstack.edu.controller;

import com.devstack.edu.model.Student;
import com.devstack.edu.util.GlobalVar;
import com.devstack.edu.view.tm.StudentTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class StudentFormController {
    public AnchorPane studentFormContext;
    public Button btnSaveUpdate;
    public TextField txtStudentName;
    public TextField txtAddress;
    public TextField txtEmail;
    public DatePicker dob;
    public TableView<StudentTm> tblStudent;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colEmail;
    public TableColumn colDob;
    public TableColumn colAddress;
    public TableColumn colStatus;
    public TableColumn colOptions;

    private String searchText="";

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
            preparedStatement.setDate(3,java.sql.Date.valueOf(student.getDate()));
            preparedStatement.setString(4,student.getAddress());
            preparedStatement.setBoolean(5, student.isStatus());
            preparedStatement.setString(6, GlobalVar.userEmail);


            if(preparedStatement.executeUpdate()>0){
                new Alert(Alert.AlertType.INFORMATION, "Student was Saved!").show();
                clearFields();
                loadStudents(searchText);
            }else{
                new Alert(Alert.AlertType.WARNING, "Try Again").show();
            }

        }catch (ClassNotFoundException | SQLException e){
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }


    private void clearFields(){
       // txtAddress.setText("");
        txtAddress.clear();
        txtStudentName.clear();
        txtEmail.clear();
        dob.setValue(null);
    }

    private void loadStudents(String searchText){

        searchText="%"+searchText+"%";

        try{
            //1 step
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2 step
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/edusmart",
                    "root","1234");
            //3 step
            String query = "SELECT * FROM student WHERE student_name LIKE ? OR email LIKE ?";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //5 step
            preparedStatement.setString(1,searchText);
            preparedStatement.setString(2,searchText);

            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList<StudentTm> tms= FXCollections.observableArrayList();

            while (resultSet.next()) {

                Button deleteButton = new Button("Delete");
                Button updateButton = new Button("Update");

                ButtonBar bar = new ButtonBar();


                StudentTm tm = new StudentTm(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getBoolean(6),
                        deleteButton,
                        updateButton
                );
                tms.add(tm);
            }

            tblStudent.setItems(tms);

            if(>0){
                new Alert(Alert.AlertType.INFORMATION, "Student was Saved!").show();
                clearFields();
                loadStudents(searchText);
            }else{
                new Alert(Alert.AlertType.WARNING, "Try Again").show();
            }

        }catch (ClassNotFoundException | SQLException e){
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }
    }

}
