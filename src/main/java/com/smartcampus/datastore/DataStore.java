/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.datastore;

import com.smartcampus.models.Room;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */
public class DataStore {
    private static final Logger LOGGER = Logger.getLogger(DataStore.class.getName());

    // Single shared instance - same singleton pattern used in tutorials
    private static final DataStore INSTANCE = new DataStore();

    private final HashMap<String, Room> rooms;
    private final HashMap<String, Sensor> sensors;
    private final HashMap<String, List<SensorReading>> readings;

    private DataStore() {
        rooms    = new HashMap<>();
        sensors  = new HashMap<>();
        readings = new HashMap<>();
        LOGGER.info("DataStore initialised.");
    }

    public static DataStore getInstance() {
        return INSTANCE;
    }

    public synchronized HashMap<String, Room> getRooms() {
        return rooms;
    }

    public synchronized HashMap<String, Sensor> getSensors() {
        return sensors;
    }

    public synchronized List<SensorReading> getReadingsForSensor(String sensorId) {
        if (!readings.containsKey(sensorId)) {
            readings.put(sensorId, new ArrayList<>());
        }
        return readings.get(sensorId);
    }
}
