/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.models.ErrorMessage;
import com.smartcampus.models.Room;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author ASUS
 */
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private static final Logger LOGGER = Logger.getLogger(RoomResource.class.getName());

    // Get the shared singleton store - same getInstance() pattern as tutorials
    private final DataStore store = DataStore.getInstance();

    // GET /api/v1/rooms
    @GET
    public Response getAllRooms() {
        LOGGER.info("GET /rooms called.");
        List<Room> roomList = new ArrayList<>(store.getRooms().values());
        return Response.ok(roomList).build();
    }

    // POST /api/v1/rooms
    @POST
    public Response createRoom(Room room) {
        LOGGER.info("POST /rooms called. Room id: " + room.getId());

        if (room.getId() == null || room.getId().isEmpty()) {
            ErrorMessage error = new ErrorMessage(
                "Room 'id' field is required.",
                400,
                "http://localhost:8080/smart-campus-api/api/v1"
            );
            return Response.status(400).entity(error).build();
        }

        if (store.getRooms().containsKey(room.getId())) {
            ErrorMessage error = new ErrorMessage(
                "Room with id '" + room.getId() + "' already exists.",
                409,
                "http://localhost:8080/smart-campus-api/api/v1"
            );
            return Response.status(409).entity(error).build();
        }

        store.getRooms().put(room.getId(), room);
        LOGGER.info("Room created: " + room.getId());
        return Response.status(201).entity(room).build();
    }

    // GET /api/v1/rooms/{roomId}
    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        LOGGER.info("GET /rooms/" + roomId + " called.");

        Room room = store.getRooms().get(roomId);

        if (room == null) {
            // Same pattern as Week 9 tutorial - throw custom exception
            // which is caught by DataNotFoundExceptionMapper
            ErrorMessage error = new ErrorMessage(
                "Room with id '" + roomId + "' was not found.",
                404,
                "http://localhost:8080/smart-campus-api/api/v1"
            );
            return Response.status(404).entity(error).build();
        }

        return Response.ok(room).build();
    }

    // DELETE /api/v1/rooms/{roomId}
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        LOGGER.info("DELETE /rooms/" + roomId + " called.");

        Room room = store.getRooms().get(roomId);

        // Idempotent: if already gone, return 204 with no error
        if (room == null) {
            LOGGER.warning("DELETE on non-existent room: " + roomId);
            return Response.noContent().build();
        }

        // Business rule: block deletion if room still has sensors
        if (!room.getSensorIds().isEmpty()) {
            LOGGER.warning("Room " + roomId + " still has sensors - cannot delete.");
            throw new RoomNotEmptyException(roomId);
        }

        store.getRooms().remove(roomId);
        LOGGER.info("Room deleted: " + roomId);
        return Response.noContent().build();
    }
}