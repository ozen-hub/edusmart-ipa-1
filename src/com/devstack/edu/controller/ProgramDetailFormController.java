package com.devstack.edu.controller;

import com.devstack.edu.db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.sql.*;

public class ProgramDetailFormController {
    public ListView<String> lstData;

    // SELECT p.*,pc.* from program p INNER JOIN program_content pc ON p.program_id=pc.program_program_id;
    // SELECT pc.* from program_content pc WHERE pc.program_program_id=?;

    public void setId(Long id){
        try{
            Connection connection = DbConnection.getInstance().getConnection();
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
