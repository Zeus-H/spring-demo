package com.example.springdemo.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseLogOutput implements TimeLog2Util.LogOutput {
    private final Connection connection;

    public DatabaseLogOutput(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void output(String log) {
        String sql = "INSERT INTO logs (log) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, log);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理或记录数据库操作异常
        }
    }
}
