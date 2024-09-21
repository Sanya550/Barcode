package com.example.barcode.service;

import com.example.barcode.database.Database;
import com.example.barcode.entity.Device;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeviceService {
    private static final String SELECT_ALL_DEVICES_QUERY = "SELECT * FROM device";
    private static final String SAVE_DEVICE_QUERY = "INSERT INTO device (name, type, brand, code) VALUES (?,?,?,?)";
    private static final String SELECT_DEVICE_BY_NAME_QUERY = "SELECT * FROM device WHERE name = ?";

    public List<Device> getDevices() {
        List<Device> devices = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_DEVICES_QUERY);
            while (resultSet.next()) {
                Device device = new Device();
                device.setId(resultSet.getLong("id"));
                device.setName(resultSet.getString("name"));
                device.setType(resultSet.getString("type"));
                device.setBrand(resultSet.getString("brand"));
                device.setCode(resultSet.getString("code"));
                devices.add(device);
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return devices;
    }

    public void create(Device device) {
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.
                     prepareStatement(SAVE_DEVICE_QUERY)) {
            preparedStatement.setString(1, device.getName());
            preparedStatement.setString(2, device.getType());
            preparedStatement.setString(3, device.getBrand());
            preparedStatement.setString(4, device.getCode());
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Device getDeviceByName(String name) {
        Device device = null;
        try (Connection connection = Database.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_DEVICE_BY_NAME_QUERY)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                device = new Device();
                device.setId(resultSet.getInt("id"));
                device.setName(resultSet.getString("name"));
                device.setType(resultSet.getString("type"));
                device.setBrand(resultSet.getString("brand"));
                device.setCode(resultSet.getString("code"));
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return device;
    }
}
