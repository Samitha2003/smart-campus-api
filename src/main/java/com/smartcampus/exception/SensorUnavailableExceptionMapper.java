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
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {

    private static final Logger LOGGER = Logger.getLogger(SensorUnavailableExceptionMapper.class.getName());

    @Override
    public Response toResponse(SensorUnavailableException exception) {
        LOGGER.warning("SensorUnavailable: " + exception.getMessage());

        ErrorMessage error = new ErrorMessage(
            "Sensor '" + exception.getSensorId() + "' is currently under MAINTENANCE and cannot accept new readings.",
            403,
            "http://localhost:8080/smart-campus-api/api/v1"
        );

        return Response.status(403).entity(error).build();
    }
}
