/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exception;

import com.smartcampus.models.ErrorMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author ASUS
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        // Log full details server-side only - never expose stack trace to client
        LOGGER.log(Level.SEVERE, "Unhandled exception: " + exception.getMessage(), exception);

        ErrorMessage error = new ErrorMessage(
            "An unexpected error occurred. Please contact support.",
            500,
            "http://localhost:8080/smart-campus-api/api/v1"
        );

        return Response.status(500).entity(error).build();
    }
}
