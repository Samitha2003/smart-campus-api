/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.models.ErrorMessage;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;
import com.smartcampus.datastore.DataStore;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author ASUS
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private static final Logger LOGGER = Logger.getLogger(SensorReadingResource.class.getName());

    private final String sensorId;
    private final DataStore store = DataStore.getInstance();

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // GET /api/v1/sensors/{sensorId}/readings
    @GET
    public Response getReadings() {
        LOGGER.info("GET /sensors/" + sensorId + "/readings called.");

        if (!store.getSensors().containsKey(sensorId)) {
            ErrorMessage error = new ErrorMessage(
                "Sensor '" + sensorId + "' was not found.",
                404,
                "http://localhost:8080/smart-campus-api/api/v1"
            );
            return Response.status(404).entity(error).build();
        }

        List<SensorReading> list = store.getReadingsForSensor(sensorId);
        return Response.ok(list).build();
    }

    // POST /api/v1/sensors/{sensorId}/readings
    @POST
    public Response addReading(SensorReading reading) {
        LOGGER.info("POST /sensors/" + sensorId + "/readings called.");

        Sensor sensor = store.getSensors().get(sensorId);

        if (sensor == null) {
            ErrorMessage error = new ErrorMessage(
                "Sensor '" + sensorId + "' was not found.",
                404,
                "http://localhost:8080/smart-campus-api/api/v1"
            );
            return Response.status(404).entity(error).build();
        }

        // Business rule: MAINTENANCE sensors cannot accept readings
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            LOGGER.warning("Sensor " + sensorId + " is MAINTENANCE - rejected.");
            throw new SensorUnavailableException(sensorId);
        }

        // Create reading with auto id + timestamp
        SensorReading newReading = new SensorReading(reading.getValue());
        store.getReadingsForSensor(sensorId).add(newReading);

        // Side effect: update sensor's currentValue to the latest reading
        sensor.setCurrentValue(newReading.getValue());
        LOGGER.info("Reading added. Sensor " + sensorId + " currentValue now: " + newReading.getValue());

        return Response.status(201).entity(newReading).build();
    }
}
