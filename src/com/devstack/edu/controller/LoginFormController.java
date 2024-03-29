package com.devstack.edu.controller;

import com.devstack.edu.db.DbConnection;
import com.devstack.edu.util.GlobalVar;
import com.devstack.edu.util.PasswordManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

public class LoginFormController {
    public AnchorPane loginFormContext;
    public TextField txtEmail;
    public PasswordField txtPassword;

    public void signInOnAction(ActionEvent actionEvent) throws IOException {
        try{
            Connection connection = DbConnection.getInstance().getConnection();
            //3 step
            String query = "SELECT email,password FROM user WHERE email=?";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //5 step
            preparedStatement.setString(1,txtEmail.getText());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){

                if (PasswordManager.checkPw(txtPassword.getText(),
                        resultSet.getString(2))){
                    GlobalVar.userEmail= resultSet.getString(1);
                    setUi("DashboardForm");
                    return;
                }else{
                    new Alert(Alert.AlertType.WARNING, "password wrong").show();
                }



            }else{
                new Alert(Alert.AlertType.WARNING, "user not found").show();
            }


        }catch (ClassNotFoundException | SQLException e){
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }


    }

    public void createAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUi("SignupForm");
    }

    private void setUi(String location) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml")));
        Stage stage = (Stage) loginFormContext.getScene().getWindow();
        stage.setTitle("EduSmart");
        stage.setScene(scene);
        stage.centerOnScreen();
    }

}
