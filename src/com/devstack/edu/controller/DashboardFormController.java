package com.devstack.edu.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        setUi("StudentForm","student");
    }

    public void programClickOnAction(MouseEvent mouseEvent) throws IOException {
        setUi("ProgramsForm","program");
    }

    public void trainerClickOnAction(MouseEvent mouseEvent) throws IOException {
        setUi("TrainersForm","trainer");
    }

    public void intakeClickOnAction(MouseEvent mouseEvent) throws IOException {
        setUi("IntakesForm","intake");
    }

    public void incomeClickOnAction(MouseEvent mouseEvent) throws IOException {
        setUi("incomeForm","income");
    }

    public void registrationClickOnAction(MouseEvent mouseEvent) throws IOException {
        setUi("RegistrationsForm","registration");
    }

    public void reportClickOnAction(MouseEvent mouseEvent) throws IOException {
        setUi("ReportsForm","report");
    }


    private void setUi(String location, String styleSheet) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml")));
        scene.getStylesheets().add(getClass().getResource("../view/styles/"+styleSheet+".css").toExternalForm());
        Stage stage = (Stage) dashboardFormContext.getScene().getWindow();
        //stage.setTitle("EduSmart");
        stage.setScene(scene);
        stage.centerOnScreen();
    }


    public void makeBackupOnAction(ActionEvent actionEvent) {
        String userName="root";
        String password="1234";
        String database="edusmart";

        try{
            SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyyMMdd");
            String timeStamp = simpleDateFormat.format(new Date());
            String fileName = "backup_"+timeStamp+".sql";// backup_3234.sql

            String sqlCommand = "mysqldump --user="+userName+" --password="+password+" --host=localhost "+database+" --result-file="+fileName;

            Process process= Runtime.getRuntime().exec(sqlCommand);
            int exitCode = process.waitFor();

            if(exitCode==0){
                File backup = new File(fileName);

                if (backup.exists()){
                    FileInputStream fileInputStream = new FileInputStream(backup);
                    FileOutputStream fileOutputStream = new FileOutputStream("src/"+fileName);

                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    while ((bytesRead=fileInputStream.read(buffer))!=-1){
                        fileOutputStream.write(buffer,0,bytesRead);
                    }

                    new Alert(Alert.AlertType.INFORMATION,"Backup File was Created!").show();

                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
