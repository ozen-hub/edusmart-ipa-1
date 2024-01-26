package com.devstack.edu.controller;

import com.devstack.edu.model.User;
import com.devstack.edu.util.GlobalVar;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignupFormController {

    public void initialize(){
        txtFirstName.requestFocus();
    }

    public AnchorPane registerFormContext;
    public TextField txtEmail;
    public PasswordField txtPassword;
    public TextField txtFirstName;
    public TextField txtLastname;
    public Button btnSignup;

    public void signupOnAction(ActionEvent actionEvent) throws IOException {

        User user = new User(
                txtFirstName.getText(),
                txtLastname.getText(),
                txtEmail.getText(),
                txtPassword.getText()
        );

        try{
            //1 step
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2 step
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/edusmart",
                    "root","1234");
            //3 step
            String query = "INSERT INTO user VALUES (?,?,?,?,?)";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //5 step
            preparedStatement.setString(1,user.getRootEmail());
            preparedStatement.setString(2,user.getFirstName());
            preparedStatement.setString(3,user.getLastname());
            preparedStatement.setString(4,user.getPassword());
            preparedStatement.setBoolean(5,true);

            if(preparedStatement.executeUpdate()>0){
                new Alert(Alert.AlertType.INFORMATION, "User was Saved!").show();
            }else{
                new Alert(Alert.AlertType.WARNING, "Try Again").show();
            }

        }catch (ClassNotFoundException | SQLException e){
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }

        GlobalVar.userEmail= user.getRootEmail();
        setUi("DashboardForm");
    }

    public void alreadyHaveAnAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUi("loginForm");
    }
    //=========================
    private void setUi(String location) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml")));
        Stage stage = (Stage) registerFormContext.getScene().getWindow();
        stage.setTitle("EduSmart");
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void emailNextOnAction(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }

    public void passwordNextOnAction(ActionEvent actionEvent) {
        btnSignup.requestFocus();
    }

    public void fNameNextOnAction(ActionEvent actionEvent) {
        txtLastname.requestFocus();
    }

    public void lNameNextOnAction(ActionEvent actionEvent) {
        txtEmail.requestFocus();
    }
}
