package com.devstack.edu.controller;

import com.devstack.edu.db.DbConnection;
import com.devstack.edu.model.Registration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

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
    public TextField txtStudent;

    public void initialize() {
        loadAllPrograms();


        cmbProgram.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) loadAllIntakes(Long.parseLong(newValue.split("->")[0]));
        });// 1->IPS
        manageAutoComplete();
    }

    private void manageAutoComplete() {
        ArrayList<String> data = new ArrayList<>();


        try {
            Connection connection = DbConnection.getInstance().getConnection();
            //3 step
            String query = "SELECT email FROM student";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.add(resultSet.getString(1));
            }

            TextFields.bindAutoCompletion(txtStudent, data);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllIntakes(long id) {
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            //3 step
            String query = "SELECT i.intake_id,i.intake_name FROM intake i WHERE i.program_program_id=?";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<String> obList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                obList.add(resultSet.getLong(1) + "->" + resultSet.getString(2));
            }
            cmbIntake.setItems(obList);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllPrograms() {
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            //3 step
            String query = "SELECT program_id,program_name FROM program";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);


            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<String> obList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                obList.add(resultSet.getLong(1) + "->" + resultSet.getString(2));
            }
            cmbProgram.setItems(obList);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    private void setUi(String location) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml")));
        Stage stage = (Stage) registrationFormContext.getScene().getWindow();
        stage.setTitle("EduSmart");
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void btnRegisterOnAction(ActionEvent actionEvent) {

        double amount= getProgramAmount(Long.parseLong(cmbProgram.getValue().split("->")[0]));
        if (amount==0){
            new Alert(Alert.AlertType.WARNING,"Program not found!").show();
            return;
        }

        Long studentId= getSelectedStudent(txtStudent.getText());
        if (studentId==0){
            new Alert(Alert.AlertType.WARNING,"Student not found!").show();
            return;
        }

        Registration registration = new Registration(
                0, LocalDate.now(),amount , Long.parseLong(cmbIntake.getValue().split("->")[0]),
                studentId
        );

        boolean isSaved = saveRegistration(registration);
        if(isSaved){
            savePayment();
        }

    }

    private boolean saveRegistration(Registration reg){
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            //3 step
            String query = "INSERT INTO  registration(register_date,program_amount,intake_intake_id,student_student_id) VALUES(?,?,?,?)";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setObject(1, reg.getDate());
            preparedStatement.setObject(2, reg.getAmount());
            preparedStatement.setObject(3, reg.getIntake());
            preparedStatement.setObject(4, reg.getStudent());

            return preparedStatement.executeUpdate()>0;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private double getProgramAmount(long programId) {
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            //3 step
            String query = "SELECT amount FROM program WHERE program_id=?";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setObject(1, programId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
    private Long getSelectedStudent(String email) {
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            //3 step
            String query = "SELECT student_id FROM student WHERE email=?";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setObject(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong(1);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0L;
    }
}
