/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

import java.util.HashMap;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author ASUS
 */
// Root discovery endpoint - GET /api/v1/
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    private static final Logger LOGGER = Logger.getLogger(DiscoveryResource.class.getName());

    @GET
    public Response discover() {
        LOGGER.info("Discovery endpoint called.");

        HashMap<String, Object> info = new HashMap<>();
        info.put("name", "Smart Campus Sensor & Room Management API");
        info.put("version", "1.0");
        info.put("contact", "admin@smartcampus.ac.uk");

        // HATEOAS links - tells the client where to find resources
        HashMap<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        info.put("resources", links);

        return Response.ok(info).build();
    }
}