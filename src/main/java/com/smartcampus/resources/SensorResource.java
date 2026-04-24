/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

import com.smartcampus.exception.RoomNotFoundException;
import com.smartcampus.models.ErrorMessage;
import com.smartcampus.models.Room;
import com.smartcampus.models.Sensor;
import com.smartcampus.datastore.DataStore;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author ASUS
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private static final Logger LOGGER = Logger.getLogger(SensorResource.class.getName());

    private final DataStore store = DataStore.getInstance();

    // GET /api/v1/sensors  OR  GET /api/v1/sensors?type=CO2
    @GET
    public Response getSensors(@QueryParam("type") String type) {
        LOGGER.info("GET /sensors called. Type filter: " + type);

        // Plain for loop - same style as tutorial exercises
        List<Sensor> result = new ArrayList<>();
        for (Sensor sensor : store.getSensors().values()) {
            if (type == null || sensor.getType().equalsIgnoreCase(type)) {
                result.add(sensor);
            }
        }

        return Response.ok(result).build();
    }

    // POST /api/v1/sensors
    @POST
    public Response createSensor(Sensor sensor) {
        LOGGER.info("POST /sensors called. Sensor id: " + sensor.getId());

        if (sensor.getId() == null || sensor.getId().isEmpty()) {
            ErrorMessage error = new ErrorMessage(
                "Sensor 'id' field is required.",
                400,
                "http://localhost:8080/smart-campus-api/api/v1"
            );
            return Response.status(400).entity(error).build();
        }

        // Validate the roomId exists - throws 422 via mapper if not
        if (sensor.getRoomId() == null || !store.getRooms().containsKey(sensor.getRoomId())) {
            LOGGER.warning("Sensor references non-existent room: " + sensor.getRoomId());
            throw new RoomNotFoundException(sensor.getRoomId());
        }

        if (store.getSensors().containsKey(sensor.getId())) {
            ErrorMessage error = new ErrorMessage(
                "Sensor '" + sensor.getId() + "' already exists.",
                409,
                "http://localhost:8080/smart-campus-api/api/v1"
            );
            return Response.status(409).entity(error).build();
        }

        store.getSensors().put(sensor.getId(), sensor);

        // Register sensor in the room's sensor list
        Room room = store.getRooms().get(sensor.getRoomId());
        room.getSensorIds().add(sensor.getId());

        LOGGER.info("Sensor created: " + sensor.getId() + " in room: " + sensor.getRoomId());
        return Response.status(201).entity(sensor).build();
    }

    // GET /api/v1/sensors/{sensorId}
    @GET
    @Path("/{sensorId}")
    public Response getSensor(@PathParam("sensorId") String sensorId) {
        LOGGER.info("GET /sensors/" + sensorId + " called.");

        Sensor sensor = store.getSensors().get(sensorId);

        if (sensor == null) {
            ErrorMessage error = new ErrorMessage(
                "Sensor '" + sensorId + "' was not found.",
                404,
                "http://localhost:8080/smart-campus-api/api/v1"
            );
            return Response.status(404).entity(error).build();
        }

        return Response.ok(sensor).build();
    }

    // DELETE /api/v1/sensors/{sensorId}
    @DELETE
    @Path("/{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        LOGGER.info("DELETE /sensors/" + sensorId + " called.");

        Sensor sensor = store.getSensors().remove(sensorId);

        // Also remove this sensor from its room's list
        if (sensor != null) {
            Room room = store.getRooms().get(sensor.getRoomId());
            if (room != null) {
                room.getSensorIds().remove(sensorId);
            }
        }

        return Response.noContent().build();
    }

    // Sub-resource locator - delegates to SensorReadingResource
    // Pattern from Part 4 of the coursework spec
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        LOGGER.info("Sub-resource locator: /sensors/" + sensorId + "/readings");
        return new SensorReadingResource(sensorId);
    }
}
