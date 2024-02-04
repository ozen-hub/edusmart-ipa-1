package com.devstack.edu.dao;


import com.devstack.edu.db.DbConnection;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {
    /*public static PreparedStatement execute(String sql, Object... params) throws SQLException, ClassNotFoundException {
        PreparedStatement statement =
                DbConnection.getInstance().getConnection().prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setObject((i + 1), params[i]);
        }
        return statement;
    }

    public static boolean executeUpdate(String sql, Object... params) throws SQLException, ClassNotFoundException {
        return execute(sql, params).executeUpdate() > 0;
    }

    public static ResultSet executeQuery(String sql, Object... params) throws SQLException, ClassNotFoundException {
        return execute(sql, params).executeQuery();
    }*/

    public static <T> T execute(String sql, Object... params) throws SQLException, ClassNotFoundException {
        PreparedStatement statement =
                DbConnection.getInstance().getConnection().prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setObject((i + 1), params[i]);
        }

        if(sql.startsWith("SELECT")){
            return (T) statement.executeQuery();
        }

        return (T)(Boolean)(statement.executeUpdate()>0);
    }

}
