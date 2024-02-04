package com.devstack.edu.dao.custom.impl;

import com.devstack.edu.dao.custom.TrainerDao;
import com.devstack.edu.db.DbConnection;
import com.devstack.edu.entity.Trainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrainerDaoImpl implements TrainerDao {

    @Override
    public boolean updateTrainer(Trainer trainer, long trainerId) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
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
        preparedStatement.setLong(5,trainerId);

        return preparedStatement.executeUpdate()>0;
    }

    @Override
    public List<Trainer> findAllTrainers(String searchText) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        //3 step
        String query = "SELECT * FROM trainer WHERE trainer_name LIKE ? OR trainer_email LIKE ?";
        //4 step
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        //5 step
        preparedStatement.setString(1,searchText);
        preparedStatement.setString(2,searchText);
        ResultSet rs = preparedStatement.executeQuery();
        ArrayList<Trainer> trainers = new ArrayList<>();
        while (rs.next()){
            trainers.add(
                    new Trainer(
                            rs.getLong(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getBoolean(6))
            );
        }

        return trainers;
    }

    @Override
    public boolean save(Trainer trainer) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
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
        return preparedStatement.executeUpdate()>0;
    }

    @Override
    public boolean update(Trainer trainer) {
        return false;
    }

    @Override
    public boolean delete(Long aLong) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public Trainer find(Long aLong) {
        return null;
    }

    @Override
    public List<Trainer> findAll() {
        return null;
    }
}
