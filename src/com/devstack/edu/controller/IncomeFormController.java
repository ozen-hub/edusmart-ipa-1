package com.devstack.edu.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class IncomeFormController {
    public AnchorPane incomeFormContext;
    public AnchorPane chart;

    public void initialize(){
        loadChart();
    }

    private void loadChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        LineChart<Number,Number> lineChart=new LineChart<>(xAxis,yAxis);
        XYChart.Series<Number,Number> series = new XYChart.Series<>();

        series.getData().add(new XYChart.Data<>(1,500));
        series.getData().add(new XYChart.Data<>(2,600));
        series.getData().add(new XYChart.Data<>(3,300));
        series.getData().add(new XYChart.Data<>(4,800));
        series.getData().add(new XYChart.Data<>(5,200));
        series.getData().add(new XYChart.Data<>(6,500));
        series.getData().add(new XYChart.Data<>(7,600));
        series.getData().add(new XYChart.Data<>(8,300));
        series.getData().add(new XYChart.Data<>(9,800));
        series.getData().add(new XYChart.Data<>(10,200));

        lineChart.getData().add(series);

        chart.getChildren().add(lineChart);
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }
    private void setUi(String location) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml")));
        Stage stage = (Stage) incomeFormContext.getScene().getWindow();
        stage.setTitle("EduSmart");
        stage.setScene(scene);
        stage.centerOnScreen();
    }
}
