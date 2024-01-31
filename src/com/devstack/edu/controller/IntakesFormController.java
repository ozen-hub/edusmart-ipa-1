package com.devstack.edu.controller;

import com.devstack.edu.model.Intake;
import com.devstack.edu.view.tm.IntakeTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class IntakesFormController {
    public AnchorPane intakeFormContext;
    public Button btnSaveIntake;
    public TextField txtIntakeName;
    public TextField txtSearchHere;
    public TableView<IntakeTm> tblIntakes;
    public TableColumn colIntakeId;
    public TableColumn colProgram;
    public TableColumn colIntakeName;
    public TableColumn colStartDate;
    public TableColumn colOperation;
    public ComboBox<String> cmbProgram;
    public DatePicker dpStartDate;

    public void initialize(){


        colIntakeId.setCellValueFactory(new PropertyValueFactory<>("intakeId"));
        colIntakeName.setCellValueFactory(new PropertyValueFactory<>("intakeName"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colOperation.setCellValueFactory(new PropertyValueFactory<>("button"));

        loadAllPrograms();
        loadAllIntakes();
    }

    private void loadAllPrograms() {
        try {
            //1 step
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2 step
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/edusmart",
                    "root", "1234");
            //3 step
            String query = "SELECT program_id,program_name FROM program";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);


            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList<String> list = FXCollections.observableArrayList();

            while (resultSet.next()) {
                list.add(resultSet.getString(1) + "->" + resultSet.getString(2));
            }

            cmbProgram.setItems(list);

        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }
    private void setUi(String location) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml")));
        Stage stage = (Stage) intakeFormContext.getScene().getWindow();
        stage.setTitle("EduSmart");
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void btnNewIntakeOnAction(ActionEvent actionEvent) {
    }

    public void btnSaveIntakeOnAction(ActionEvent actionEvent) {
        // ID->Program Name
        Intake intake = new Intake(0,
                txtIntakeName.getText(),dpStartDate.getValue(),
                Long.parseLong(cmbProgram.getValue().split("->")[0]));

        try {
            //1 step
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2 step
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/edusmart",
                    "root", "1234");
            //3 step
            String query = "INSERT INTO intake(intake_name,start_date,program_program_id) VALUES(?,?,?)";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setObject(1,intake.getIntakeName());
            preparedStatement.setObject(2,intake.getStartDate());
            preparedStatement.setObject(3,intake.getProgramId());

            boolean isSaved = preparedStatement.executeUpdate()>0;
            if(isSaved){
                new Alert(Alert.AlertType.INFORMATION,"Intake Saved!").show();
                clearFields();
                loadAllIntakes();
            }

        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }

    }

    private void clearFields() {
        txtIntakeName.clear();
        dpStartDate.setValue(null);
        cmbProgram.setValue(null);
    }

    private void loadAllIntakes() {
        try {
            //1 step
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2 step
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/edusmart",
                    "root", "1234");
            //3 step
            String query = "SELECT p.program_name,i.intake_id,i.intake_name,i.start_date FROM intake i INNER JOIN program p ON i.program_program_id=p.program_id";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<IntakeTm> obList = FXCollections.observableArrayList();
            while(resultSet.next()){

                Button btn = new Button("Delete");

                IntakeTm tm = new IntakeTm(resultSet.getLong(2),
                        resultSet.getString(3),resultSet.getString(1),
                        LocalDate.parse(resultSet.getString(4)),btn);
                obList.add(tm);
            }
            tblIntakes.setItems(obList);

        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }

    }
}
