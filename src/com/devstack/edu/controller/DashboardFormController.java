package com.devstack.edu.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardFormController {

    public AnchorPane dashboardFormContext;

    public void studentClickOnAction(MouseEvent mouseEvent) throws IOException {
        setUi("StudentForm");
    }

    public void programClickOnAction(MouseEvent mouseEvent) throws IOException {
        setUi("ProgramsForm");
    }

    public void trainerClickOnAction(MouseEvent mouseEvent) throws IOException {
        setUi("TrainersForm");
    }

    public void intakeClickOnAction(MouseEvent mouseEvent) throws IOException {
        setUi("IntakesForm");
    }

    public void incomeClickOnAction(MouseEvent mouseEvent) throws IOException {
        setUi("incomeForm");
    }

    public void registrationClickOnAction(MouseEvent mouseEvent) throws IOException {
        setUi("RegistrationsForm");
    }

    public void reportClickOnAction(MouseEvent mouseEvent) throws IOException {
        setUi("ReportsForm");
    }


    private void setUi(String location) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml")));
        Stage stage = (Stage) dashboardFormContext.getScene().getWindow();
        //stage.setTitle("EduSmart");
        stage.setScene(scene);
        stage.centerOnScreen();
    }



}
