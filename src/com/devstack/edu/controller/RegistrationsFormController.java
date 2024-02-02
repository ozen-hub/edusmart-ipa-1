package com.devstack.edu.controller;

import com.devstack.edu.db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class RegistrationsFormController {
    public AnchorPane registrationFormContext;
    public TextField txtSearch;
    public TableView tblRegistrations;
    public TableColumn colIntakeId;
    public TableColumn colProgram;
    public TableColumn colStudentName;
    public TableColumn colRegisterDate;
    public TableColumn colOperation;
    public ComboBox<String> cmbProgram;
    public ComboBox<String> cmbIntake;

    public void initialize(){
        loadAllPrograms();


        cmbProgram.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null) loadAllIntakes(Long.parseLong(newValue.split("->")[0]));
        });// 1->IPS
    }

    private void loadAllIntakes(long id) {
        try{
            Connection connection = DbConnection.getInstance().getConnection();
            //3 step
            String query = "SELECT i.intake_id,i.intake_name FROM intake i WHERE i.program_program_id=?";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1,id);

            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<String> obList = FXCollections.observableArrayList();
            while (resultSet.next()){
                obList.add(resultSet.getLong(1)+"->"+resultSet.getString(2));
            }
            cmbIntake.setItems(obList);

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private void loadAllPrograms() {
        try{
            Connection connection = DbConnection.getInstance().getConnection();
            //3 step
            String query = "SELECT program_id,program_name FROM program";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);


            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<String> obList = FXCollections.observableArrayList();
            while (resultSet.next()){
                obList.add(resultSet.getLong(1)+"->"+resultSet.getString(2));
            }
            cmbProgram.setItems(obList);

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }
    private void setUi(String location) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml")));
        Stage stage = (Stage) registrationFormContext.getScene().getWindow();
        stage.setTitle("EduSmart");
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void btnRegisterOnAction(ActionEvent actionEvent) {
    }
}
