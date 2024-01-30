package com.devstack.edu.controller;

import com.devstack.edu.model.Student;
import com.devstack.edu.model.Trainer;
import com.devstack.edu.util.GlobalVar;
import com.devstack.edu.view.tm.StudentTm;
import com.devstack.edu.view.tm.TrainerTm;
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
import java.util.Optional;

public class TrainersFormController {
    public AnchorPane trainersFormOnAction;
    public Button btnSaveUpdate;
    public TextField txtTrainerName;
    public TextField txtSearch;
    public TextField txtTrainerAddress;
    public TextField txtTrainerEmail;
    public TableView<TrainerTm> tblTrainers;
    public TableColumn colTrainerId;
    public TableColumn colTrainerName;
    public TableColumn colEmail;
    public TableColumn colNic;
    public TableColumn colAddress;
    public TableColumn colOperation;
    public TextField txtTrainerNic;
    public TableColumn colTrainerState;

    private String searchText="";
    private long selectedTrainerId=0;

    public void initialize(){

        colTrainerId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTrainerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colOperation.setCellValueFactory(new PropertyValueFactory<>("buttonBar"));
        colTrainerState.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadTrainers(searchText);


        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText=newValue;
            if (newValue != null) {
                loadTrainers(searchText);
            }

        });
    }

    private void loadTrainers(String searchText) {
        searchText="%"+searchText+"%";

        try{
            //1 step
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2 step
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/edusmart",
                    "root","1234");
            //3 step
            String query = "SELECT * FROM trainer WHERE trainer_name LIKE ? OR trainer_email LIKE ?";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //5 step
            preparedStatement.setString(1,searchText);
            preparedStatement.setString(2,searchText);

            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList<TrainerTm> tms= FXCollections.observableArrayList();

            while (resultSet.next()) {

                Button deleteButton = new Button("Delete");
                Button updateButton = new Button("Update");

                ButtonBar bar = new ButtonBar();
                bar.getButtons().addAll(deleteButton,updateButton);


                TrainerTm tm = new TrainerTm(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getBoolean(6)?"Active":"InActive",
                        bar
                );
                tms.add(tm);

                updateButton.setOnAction(e->{
                    txtTrainerName.setText(tm.getName());
                    txtTrainerEmail.setText(tm.getEmail());
                    txtTrainerNic.setText(tm.getNic());
                    txtTrainerAddress.setText(tm.getAddress());
                    selectedTrainerId=tm.getId();
                    btnSaveUpdate.setText("Update Trainer");
                });
                deleteButton.setOnAction(e->{

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "are you sure?",
                            ButtonType.YES,ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get()==ButtonType.YES){
                        try{
                            //1 step
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            //2 step
                            Connection connection1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/edusmart",
                                    "root","1234");
                            //3 step
                            String query1 = "DELETE FROM trainer WHERE trainer_id=?";
                            //4 step
                            PreparedStatement preparedStatement1 = connection1.prepareStatement(query1);
                            //5 step
                            preparedStatement1.setLong(1,tm.getId());


                            if(preparedStatement1.executeUpdate()>0){
                                new Alert(Alert.AlertType.INFORMATION, "Trainer was Deleted!").show();
                                loadTrainers("");
                            }else{
                                new Alert(Alert.AlertType.WARNING, "Try Again").show();
                            }

                        }catch (SQLException | ClassNotFoundException ex){
                            ex.printStackTrace();
                        }
                    }
                });

            }

            tblTrainers.setItems(tms);

        }catch (ClassNotFoundException | SQLException e){
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }
    private void setUi(String location) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml")));
        Stage stage = (Stage) trainersFormOnAction.getScene().getWindow();
        stage.setTitle("EduSmart");
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void saveTrainerOnAction(ActionEvent actionEvent) {
        Trainer trainer= new Trainer(0L,txtTrainerName.getText(),
                txtTrainerEmail.getText(),
                txtTrainerNic.getText(),txtTrainerAddress.getText(),true);

        if(btnSaveUpdate.getText().equalsIgnoreCase("Save Trainer")){
            try{
                //1 step
                Class.forName("com.mysql.cj.jdbc.Driver");
                //2 step
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/edusmart",
                        "root","1234");
                //3 step
                String query = "INSERT INTO trainer(trainer_name,trainer_email,nic,address,trainer_status)" +
                        " VALUES (?,?,?,?,?)";
                //4 step
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                //5 step
                preparedStatement.setString(1,trainer.getTrainerName());
                preparedStatement.setString(2,trainer.getTrainerEmail());
                preparedStatement.setString(3,trainer.getNic());
                preparedStatement.setString(4,trainer.getAddress());
                preparedStatement.setBoolean(5, trainer.isTrainerStatus());


                if(preparedStatement.executeUpdate()>0){
                    new Alert(Alert.AlertType.INFORMATION, "Trainer was Saved!").show();
                    clearFields();
                    loadTrainers(searchText);
                }else{
                    new Alert(Alert.AlertType.WARNING, "Try Again").show();
                }

            }catch (ClassNotFoundException | SQLException e){
                new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
                e.printStackTrace();
            }
        }else{
            if (selectedTrainerId==0){
                new Alert(Alert.AlertType.ERROR, "Please verify the Trainer id").show();
                return;
            }
            try{
                //1 step
                Class.forName("com.mysql.cj.jdbc.Driver");
                //2 step
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/edusmart",
                        "root","1234");
                //3 step
                String query = "UPDATE trainer SET trainer_name=?, trainer_email=?, nic=?,address=?" +
                        " WHERE trainer_id=?";
                //4 step
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                //5 step
                preparedStatement.setString(1,trainer.getTrainerName());
                preparedStatement.setString(2,trainer.getTrainerEmail());
                preparedStatement.setString(3,trainer.getTrainerEmail());
                preparedStatement.setString(4,trainer.getAddress());
                preparedStatement.setLong(5,selectedTrainerId);


                if(preparedStatement.executeUpdate()>0){
                    new Alert(Alert.AlertType.INFORMATION, "Student was Updated!").show();
                    clearFields();
                    loadTrainers(searchText);
                    btnSaveUpdate.setText("Save Trainer");
                }else{
                    new Alert(Alert.AlertType.WARNING, "Try Again").show();
                }

            }catch (ClassNotFoundException | SQLException e){
                new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
                e.printStackTrace();
            }
        }
    }

    public void btnNewTrainerOnAction(ActionEvent actionEvent) {
        clearFields();
        selectedTrainerId=0;
        btnSaveUpdate.setText("Save Trainer");
    }

    private void clearFields(){
        // txtAddress.setText("");
        txtTrainerAddress.clear();
        txtTrainerNic.clear();
        txtTrainerName.clear();
        txtTrainerEmail.clear();
    }


}
