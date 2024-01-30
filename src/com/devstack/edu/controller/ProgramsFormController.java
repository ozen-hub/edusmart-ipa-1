package com.devstack.edu.controller;

import com.devstack.edu.view.tm.TrainerTm;
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
import java.util.Optional;

public class ProgramsFormController {
    public AnchorPane programFormContext;
    public TextField txtSearch;
    public TextField txtContent;
    public TextField txtHours;
    public TableView tblPrograms;
    public TableColumn colTraineriD;
    public TableColumn ColProgramName;
    public TableColumn colHours;
    public TableColumn colAmount;
    public TableColumn colOperation;
    public ComboBox<String> cmbTrainer;
    public TextField txtAmount;
    public ListView<String> lstContent;

    public void initialize() {
        loadAllTrainers();
    }

    private void loadAllTrainers() {
        try {
            //1 step
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2 step
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/edusmart",
                    "root", "1234");
            //3 step
            String query = "SELECT trainer_id,trainer_name FROM trainer";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);


            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList<String> list = FXCollections.observableArrayList();

            while (resultSet.next()) {
                list.add(resultSet.getString(1)+"->"+resultSet.getString(2));
            }

            cmbTrainer.setItems(list);

        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    private void setUi(String location) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml")));
        Stage stage = (Stage) programFormContext.getScene().getWindow();
        stage.setTitle("EduSmart");
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void btnSaveProgramOnAction(ActionEvent actionEvent) {

    }
}
