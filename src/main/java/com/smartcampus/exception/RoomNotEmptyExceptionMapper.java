/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exception;

import com.smartcampus.models.ErrorMessage;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author ASUS
 */
@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    private static final Logger LOGGER = Logger.getLogger(RoomNotEmptyExceptionMapper.class.getName());

    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        LOGGER.warning("RoomNotEmpty: " + exception.getMessage());

        ErrorMessage error = new ErrorMessage(
            "Room '" + exception.getRoomId() + "' cannot be deleted because it still has sensors assigned to it. Remove all sensors first.",
            409,
            "http://localhost:8080/smart-campus-api/api/v1"
        );

        return Response.status(409).entity(error).build();
    }
}
