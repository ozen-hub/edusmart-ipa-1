package com.devstack.edu.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardFormController {

    public AnchorPane dashboardFormContext;
    public Label lblTime;
    public Label lblDate;

    public void initialize() {
        loadDateAndTime();
    }

    private void loadDateAndTime() {
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1), event -> {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    String currentTime = simpleDateFormat.format(new Date());
                    lblTime.setText(currentTime);
                }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        //-------------------------
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(simpleDateFormat.format(date));

    }


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
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml")));
        Stage stage = (Stage) dashboardFormContext.getScene().getWindow();
        //stage.setTitle("EduSmart");
        stage.setScene(scene);
        stage.centerOnScreen();
    }


}
