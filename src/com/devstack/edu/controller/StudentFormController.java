package com.devstack.edu.controller;

import com.devstack.edu.bo.BoFactory;
import com.devstack.edu.bo.custom.StudentBo;
import com.devstack.edu.dao.DaoFactory;
import com.devstack.edu.dao.custom.StudentDao;
import com.devstack.edu.dao.custom.impl.StudentDaoImpl;
import com.devstack.edu.db.DbConnection;
import com.devstack.edu.dto.StudentDto;
import com.devstack.edu.entity.Student;
import com.devstack.edu.util.GlobalVar;
import com.devstack.edu.validators.SimpleTextValidator;
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
    public Button btnNewStudent;

    private String searchText = "";
    private int selectedStudentId = 0;

    private StudentBo studentbo = BoFactory.getBo(BoFactory.BoType.STUDENT);

    public void initialize() {

        btnNewStudent.getStyleClass().add("button");

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
            searchText = newValue;
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
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml")));
        Stage stage = (Stage) studentFormContext.getScene().getWindow();
        stage.setTitle("EduSmart");
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void btnSaveUpdateOnAction(ActionEvent actionEvent) {
        txtStudentName.getStyleClass().removeAll("error");

        if (!SimpleTextValidator.validateName(txtStudentName.getText())){
            txtStudentName.getStyleClass().add("error");
            new Alert(Alert.AlertType.WARNING, "Wrong Student name").show();
            return;
        }


        if (!SimpleTextValidator.validateDob(String.valueOf(dob.getValue()))){
            new Alert(Alert.AlertType.WARNING, "Wrong DOB").show();
            return;
        }

        if (!SimpleTextValidator.validateAddress(txtAddress.getText())){
            new Alert(Alert.AlertType.WARNING, "Wrong Address").show();
            return;
        }


        StudentDto student = new StudentDto(0, txtStudentName.getText(),
                txtEmail.getText(),
                dob.getValue(), txtAddress.getText(), true);

        if (btnSaveUpdate.getText().equalsIgnoreCase("Save Student")) {
            try {
                if (studentbo.saveStudent(student)) {
                    new Alert(Alert.AlertType.INFORMATION, "Student was Saved!").show();
                    clearFields();
                    loadStudents(searchText);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again").show();
                }

            } catch (ClassNotFoundException | SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
                e.printStackTrace();
            }
        } else {
            if (selectedStudentId == 0) {
                new Alert(Alert.AlertType.ERROR, "Please verify the student id").show();
                return;
            }
            try {
                if (studentbo.updateStudent(student, rBtnActive.isSelected(), selectedStudentId)) {
                    new Alert(Alert.AlertType.INFORMATION, "Student was Updated!").show();
                    clearFields();
                    loadStudents(searchText);
                    manageStatusVisibility(false);
                    btnSaveUpdate.setText("Save Student");
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again").show();
                }

            } catch (ClassNotFoundException | SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
                e.printStackTrace();
            }
        }
    }


    private void clearFields() {
        // txtAddress.setText("");
        txtAddress.clear();
        txtStudentName.clear();
        txtEmail.clear();
        dob.setValue(null);
    }

    private void loadStudents(String searchText) {

        searchText = "%" + searchText + "%";

        try {
            ObservableList<StudentTm> tms = FXCollections.observableArrayList();

            for (StudentDto student : studentbo.findAllStudents(searchText)
            ) {
                Button deleteButton = new Button("Delete");
                Button updateButton = new Button("Update");

                deleteButton.getStyleClass().add("delete-button");
                updateButton.getStyleClass().add("update-button");


                ButtonBar bar = new ButtonBar();
                bar.getButtons().addAll(deleteButton, updateButton);

                StudentTm tm = new StudentTm(
                        student.getStudentId(),
                        student.getStudentName(),
                        student.getEmail(),
                        student.getDate().toString(),
                        student.getAddress(),
                        student.isStatus() ? "Active" : "InActive",
                        bar
                );
                tms.add(tm);

                updateButton.setOnAction(e -> {
                    txtStudentName.setText(tm.getName());
                    txtEmail.setText(tm.getEmail());
                    dob.setValue(LocalDate.parse(tm.getDob()));
                    txtAddress.setText(tm.getAddress());
                    selectedStudentId = tm.getId();
                    //==========================
                    if (tm.isStatus().equals("Active")) {
                        rBtnActive.setSelected(true);
                    } else {
                        rBtnInActive.setSelected(true);
                    }
                    manageStatusVisibility(true);
                    //==========================
                    btnSaveUpdate.setText("Update Student");
                });

                deleteButton.setOnAction(e -> {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "are you sure?",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get() == ButtonType.YES) {
                        try {
                            if (studentbo.deleteStudent(tm.getId())) {
                                new Alert(Alert.AlertType.INFORMATION, "Student was Deleted!").show();
                                loadStudents("");
                            } else {
                                new Alert(Alert.AlertType.WARNING, "Try Again").show();
                            }

                        } catch (SQLException | ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }


            tblStudent.setItems(tms);

        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }

    public void btnNewStudentOnAction(ActionEvent actionEvent) {
        clearFields();
        selectedStudentId = 0;
        btnSaveUpdate.setText("Save Student");
    }
}

