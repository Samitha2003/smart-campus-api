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
public class RoomNotFoundExceptionMapper implements ExceptionMapper<RoomNotFoundException> {

    private static final Logger LOGGER = Logger.getLogger(RoomNotFoundExceptionMapper.class.getName());

    @Override
    public Response toResponse(RoomNotFoundException exception) {
        LOGGER.warning("RoomNotFoundException: " + exception.getMessage());

        ErrorMessage error = new ErrorMessage(
            "The roomId '" + exception.getRoomId() + "' in your request does not exist. Please provide a valid room ID.",
            422,
            "http://localhost:8080/smart-campus-api/api/v1"
        );

        return Response.status(422).entity(error).build();
    }
}