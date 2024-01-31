package com.devstack.edu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.sql.*;

public class ProgramDetailFormController {
    public ListView<String> lstData;

    public void setId(Long id){
        try{
            //1 step
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2 step
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/edusmart",
                    "root","1234");
            //3 step
            String query = "SELECT * FROM program_content WHERE program_program_id=?";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //5 step
            preparedStatement.setLong(1,id);

            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<String> obList = FXCollections.observableArrayList();
            while (resultSet.next()){
                obList.add(resultSet.getString(2));
            }
            lstData.setItems(obList);

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
