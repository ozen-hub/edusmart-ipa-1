package com.devstack.edu.controller;

import com.devstack.edu.model.Student;
import com.devstack.edu.util.GlobalVar;
import com.devstack.edu.view.tm.StudentTm;
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

public class StudentFormController {
    public AnchorPane studentFormContext;
    public Button btnSaveUpdate;
    public TextField txtStudentName;
    public TextField txtAddress;
    public TextField txtEmail;
    public DatePicker dob;
    public TableView<StudentTm> tblStudent;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colEmail;
    public TableColumn colDob;
    public TableColumn colAddress;
    public TableColumn colStatus;
    public TableColumn colOptions;
    public TextField txtSearch;
    public RadioButton rBtnActive;
    public Label lblStatus;
    public RadioButton rBtnInActive;

    private String searchText="";
    private int selectedStudentId=0;

    public void initialize(){

        manageStatusVisibility(false);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colOptions.setCellValueFactory(new PropertyValueFactory<>("buttonBar"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadStudents(searchText);


        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText=newValue;
            if (newValue != null) {
                loadStudents(searchText);
            }

        });
    }

    private void manageStatusVisibility(boolean status) {
        lblStatus.setVisible(status);
        rBtnActive.setVisible(status);
        rBtnInActive.setVisible(status);
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }
    private void setUi(String location) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml")));
        Stage stage = (Stage) studentFormContext.getScene().getWindow();
        stage.setTitle("EduSmart");
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void btnSaveUpdateOnAction(ActionEvent actionEvent) {

        Student student= new Student(0,txtStudentName.getText(),
                txtEmail.getText(),
                dob.getValue(),txtAddress.getText(),true);

        if(btnSaveUpdate.getText().equalsIgnoreCase("Save Student")){
            try{
                //1 step
                Class.forName("com.mysql.cj.jdbc.Driver");
                //2 step
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/edusmart",
                        "root","1234");
                //3 step
                String query = "INSERT INTO student(student_name,email,dob,address,status,user_email)" +
                        " VALUES (?,?,?,?,?,?)";
                //4 step
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                //5 step
                preparedStatement.setString(1,student.getStudentName());
                preparedStatement.setString(2,student.getEmail());
                preparedStatement.setDate(3,java.sql.Date.valueOf(student.getDate()));
                preparedStatement.setString(4,student.getAddress());
                preparedStatement.setBoolean(5, student.isStatus());
                preparedStatement.setString(6, GlobalVar.userEmail);


                if(preparedStatement.executeUpdate()>0){
                    new Alert(Alert.AlertType.INFORMATION, "Student was Saved!").show();
                    clearFields();
                    loadStudents(searchText);
                }else{
                    new Alert(Alert.AlertType.WARNING, "Try Again").show();
                }

            }catch (ClassNotFoundException | SQLException e){
                new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
                e.printStackTrace();
            }
        }else{
            if (selectedStudentId==0){
                new Alert(Alert.AlertType.ERROR, "Please verify the student id").show();
                return;
            }
            try{
                //1 step
                Class.forName("com.mysql.cj.jdbc.Driver");
                //2 step
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/edusmart",
                        "root","1234");
                //3 step
                String query = "UPDATE student SET student_name=?, email=?, dob=?,address=?, status=?" +
                        " WHERE student_id=?";
                //4 step
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                //5 step
                preparedStatement.setString(1,student.getStudentName());
                preparedStatement.setString(2,student.getEmail());
                preparedStatement.setDate(3,java.sql.Date.valueOf(student.getDate()));
                preparedStatement.setString(4,student.getAddress());
                preparedStatement.setBoolean(5,rBtnActive.isSelected());
                preparedStatement.setInt(6,selectedStudentId);


                if(preparedStatement.executeUpdate()>0){
                    new Alert(Alert.AlertType.INFORMATION, "Student was Updated!").show();
                    clearFields();
                    loadStudents(searchText);
                    manageStatusVisibility(false);
                    btnSaveUpdate.setText("Save Student");
                }else{
                    new Alert(Alert.AlertType.WARNING, "Try Again").show();
                }

            }catch (ClassNotFoundException | SQLException e){
                new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
                e.printStackTrace();
            }
        }
    }


    private void clearFields(){
       // txtAddress.setText("");
        txtAddress.clear();
        txtStudentName.clear();
        txtEmail.clear();
        dob.setValue(null);
    }

    private void loadStudents(String searchText){

        searchText="%"+searchText+"%";

        try{
            //1 step
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2 step
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/edusmart",
                    "root","1234");
            //3 step
            String query = "SELECT * FROM student WHERE student_name LIKE ? OR email LIKE ?";
            //4 step
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //5 step
            preparedStatement.setString(1,searchText);
            preparedStatement.setString(2,searchText);

            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList<StudentTm> tms= FXCollections.observableArrayList();

            while (resultSet.next()) {

                Button deleteButton = new Button("Delete");
                Button updateButton = new Button("Update");

                ButtonBar bar = new ButtonBar();
                bar.getButtons().addAll(deleteButton,updateButton);


                StudentTm tm = new StudentTm(
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
                    txtStudentName.setText(tm.getName());
                    txtEmail.setText(tm.getEmail());
                    dob.setValue(LocalDate.parse(tm.getDob()));
                    txtAddress.setText(tm.getAddress());
                    selectedStudentId=tm.getId();
                    //==========================
                    if (tm.isStatus().equals("Active")){
                        rBtnActive.setSelected(true);
                    }else{
                        rBtnInActive.setSelected(true);
                    }
                    manageStatusVisibility(true);
                    //==========================
                    btnSaveUpdate.setText("Update Student");
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
                            String query1 = "DELETE FROM student WHERE student_id=?";
                            //4 step
                            PreparedStatement preparedStatement1 = connection1.prepareStatement(query1);
                            //5 step
                            preparedStatement1.setInt(1,tm.getId());


                            if(preparedStatement1.executeUpdate()>0){
                                new Alert(Alert.AlertType.INFORMATION, "Student was Deleted!").show();
                                loadStudents("");
                            }else{
                                new Alert(Alert.AlertType.WARNING, "Try Again").show();
                            }

                        }catch (SQLException | ClassNotFoundException ex){
                            ex.printStackTrace();
                        }
                    }
                });

            }

            tblStudent.setItems(tms);

        }catch (ClassNotFoundException | SQLException e){
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }

    public void btnNewStudentOnAction(ActionEvent actionEvent) {
        clearFields();
        selectedStudentId=0;
        btnSaveUpdate.setText("Save Student");
    }
}

