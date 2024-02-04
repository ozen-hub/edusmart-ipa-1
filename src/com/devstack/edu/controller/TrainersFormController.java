package com.devstack.edu.controller;

import com.devstack.edu.dao.custom.impl.TrainerDaoImpl;
import com.devstack.edu.db.DbConnection;

import com.devstack.edu.entity.Trainer;
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


            ObservableList<TrainerTm> tms= FXCollections.observableArrayList();

            for (Trainer trainer:new TrainerDaoImpl().findAllTrainers(searchText)) {

                Button deleteButton = new Button("Delete");
                Button updateButton = new Button("Update");

                ButtonBar bar = new ButtonBar();
                bar.getButtons().addAll(deleteButton,updateButton);

                TrainerTm tm = new TrainerTm(
                        trainer.getTrainerId(),
                        trainer.getTrainerName(),
                        trainer.getTrainerEmail(),
                        trainer.getNic(),
                        trainer.getAddress(),
                        trainer.isTrainerStatus()?"Active":"InActive",
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
                            Connection connection1 = DbConnection.getInstance().getConnection();
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
                if(new TrainerDaoImpl().save(trainer)){
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
                if(new TrainerDaoImpl().updateTrainer(trainer, selectedTrainerId)){
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
