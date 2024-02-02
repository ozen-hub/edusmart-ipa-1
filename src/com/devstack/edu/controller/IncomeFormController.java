package com.devstack.edu.controller;

import com.devstack.edu.db.DbConnection;
import com.devstack.edu.view.tm.IncomeTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class IncomeFormController {
    public AnchorPane incomeFormContext;
    public AnchorPane chart;

    public void initialize(){
        loadChart();
    }

    private void loadChart() {
        ObservableList<IncomeTm> obList = FXCollections.observableArrayList();
        try{
            Connection connection = DbConnection.getInstance().getConnection();
            //3 step
            String query = "SELECT date, SUM(amount) AS income FROM payment WHERE is_verified=true GROUP BY date";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //5 step

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                obList.add(new IncomeTm(LocalDate.parse(resultSet.getString(1)),
                        resultSet.getDouble(2)));
            }


        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        LineChart<String,Number> lineChart=new LineChart<>(xAxis,yAxis);
        XYChart.Series<String,Number> series = new XYChart.Series<>();



        obList.forEach(e->{
            series.getData().add(new XYChart.Data<>(e.getDate().toString(),e.getAmount()));
        });



        lineChart.getData().add(series);

        AnchorPane.setTopAnchor(lineChart, 0.0);
        AnchorPane.setRightAnchor(lineChart, 0.0);
        AnchorPane.setLeftAnchor(lineChart, 0.0);
        AnchorPane.setBottomAnchor(lineChart, 0.0);

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
