package com.devstack.edu.controller;

import com.devstack.edu.db.DbConnection;
import com.devstack.edu.util.GlobalVar;
import com.devstack.edu.view.tm.ProgramTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    public TableView<ProgramTm> tblPrograms;
    public TableColumn colTraineriD;
    public TableColumn ColProgramName;
    public TableColumn colHours;
    public TableColumn colAmount;
    public TableColumn colOperation;
    public ComboBox<String> cmbTrainer;
    public TextField txtAmount;
    public ListView<String> lstContent;
    public TextField txtProgramName;

    private String searchText = "";

    ObservableList<String> contents = FXCollections.observableArrayList();

    public void initialize() {
        loadAllTrainers();


        colTraineriD.setCellValueFactory(new PropertyValueFactory<>("trainerId"));
        ColProgramName.setCellValueFactory(new PropertyValueFactory<>("program"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colHours.setCellValueFactory(new PropertyValueFactory<>("hours"));
        colOperation.setCellValueFactory(new PropertyValueFactory<>("operation"));
        loadAllPrograms(searchText);


        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText=newValue;
            if (newValue != null) {
                loadAllPrograms(searchText);
            }

        });


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
                list.add(resultSet.getString(1) + "->" + resultSet.getString(2));
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
       /* Program program = new Program(
                0L, Integer.parseInt(txtHours.getText()),
                txtProgramName.getText(), Double.parseDouble(txtAmount.getText()),
                GlobalVar.userEmail,
                Long.parseLong(cmbTrainer.getValue().split("->")[0])
                , contents
        );*/


        try {
            Connection connection = DbConnection.getInstance().getConnection();
            //3 step
            String query = "INSERT INTO program(hours,program_name,amount,user_email,trainer_trainer_id) VALUES(?,?,?,?,?)";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);
           /* preparedStatement.setObject(1, program.getHours());
            preparedStatement.setObject(2, program.getProgramName());
            preparedStatement.setObject(3, program.getAmount());
            preparedStatement.setObject(4, program.getUserEmail());
            preparedStatement.setObject(5, program.getTrainerId());*/

            boolean isSaved = preparedStatement.executeUpdate() > 0;

            //---------------------------------
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT program_id FROM program ORDER BY program_id DESC LIMIT 1");
            ResultSet resultSet = preparedStatement1.executeQuery();
            if (resultSet.next()) {
                long pId = resultSet.getLong(1);
                if (isSaved) {
                    for (String s : contents
                    ) {
                        PreparedStatement preparedStatement2 =
                                connection.prepareStatement("INSERT INTO program_content(header,program_program_id) VALUES(?,?)");
                        preparedStatement2.setObject(1, s);
                        preparedStatement2.setObject(2, pId);

                        preparedStatement2.executeUpdate();
                    }
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved!").show();
                    loadAllPrograms(searchText);
                }
            }
            //---------------------------------
        } catch (Exception e) {

        }

        //1 step


    }

    private void loadAllPrograms(String searchText) {

        searchText="%"+searchText+"%";
        try{

            Connection connection = DbConnection.getInstance().getConnection();
            //3 step
            String query = "SELECT * FROM program WHERE program_name LIKE ?";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //5 step
            preparedStatement.setString(1,searchText);

            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList<ProgramTm> tms= FXCollections.observableArrayList();

            while (resultSet.next()) {

                Button deleteButton = new Button("Delete");
                Button showMoreButton = new Button("Show");

                ButtonBar bar = new ButtonBar();
                bar.getButtons().addAll(deleteButton,showMoreButton);

                ProgramTm tm = new ProgramTm(
                        resultSet.getLong("program_id"),
                        resultSet.getLong("trainer_trainer_id"),
                        resultSet.getString("program_name"),
                        resultSet.getInt("hours"),
                        resultSet.getDouble("amount"),
                        bar
                );
                tms.add(tm);

                showMoreButton.setOnAction(e->{
                    try{
                       /* Parent parent = FXMLLoader.load(getClass().getResource("../view/ProgramDetailForm.fxml"));
                        Scene scene = new Scene(parent);
                        Stage stage= new Stage();
                        stage.setScene(scene);
                        stage.show();*/
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ProgramDetailForm.fxml"));
                        Parent parent = loader.load();
                        ProgramDetailFormController controller = loader.getController();
                        controller.setId(tm.getProgramId());
                        Scene scene = new Scene(parent);
                        Stage stage= new Stage();
                        stage.setScene(scene);
                        stage.show();

                    }catch (IOException exception){

                    }

                });

                deleteButton.setOnAction(e->{

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "are you sure?",
                            ButtonType.YES,ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get()==ButtonType.YES){
                        try{
                            Connection connection1 = DbConnection.getInstance().getConnection();
                            //3 step
                            String query1 = "DELETE FROM program WHERE program_id=?";
                            //4 step
                            PreparedStatement preparedStatement1 = connection1.prepareStatement(query1);
                            //5 step
                            preparedStatement1.setLong(1,tm.getProgramId());


                            if(preparedStatement1.executeUpdate()>0){
                                new Alert(Alert.AlertType.INFORMATION, "Program was Deleted!").show();
                                loadAllPrograms("");
                            }else{
                                new Alert(Alert.AlertType.WARNING, "Try Again").show();
                            }

                        }catch (SQLException | ClassNotFoundException ex){
                            ex.printStackTrace();
                        }
                    }
                });

            }

            tblPrograms.setItems(tms);

        }catch (ClassNotFoundException | SQLException e){
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }


    }

    public void txtContentOnAction(ActionEvent actionEvent) {
        //contents.clear();
        contents.add(txtContent.getText());
        txtContent.clear();
        lstContent.setItems(contents);
    }
}
