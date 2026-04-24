# Smart Campus Sensor & Room Management API

A RESTful API built with JAX-RS (Jersey) and Apache Tomcat for managing
rooms and sensors across a university smart campus infrastructure.
Developed as coursework for 5COSC022W Client-Server Architectures at the
University of Westminster.

---

## Technology Stack

- Java 8
- JAX-RS 2.1 (Jersey 2.32)
- Apache Tomcat 9
- Maven
- Jackson (JSON serialisation)
- Apache NetBeans IDE

---

## API Design Overview

The API follows REST principles with a versioned base path of `/api/v1`.
All responses are in JSON format. The resource hierarchy reflects the
physical structure of the campus:

- A **Room** is a physical space that contains sensors
- A **Sensor** belongs to exactly one room and records measurements
- A **SensorReading** is a historical record of a sensor measurement

The API uses a Singleton in-memory DataStore using HashMaps to store
all data. No database is used.

### Resource Hierarchy
GET  /api/v1/                              Discovery endpoint
GET  /api/v1/rooms                         List all rooms
POST /api/v1/rooms                         Create a room
GET  /api/v1/rooms/{roomId}               Get a specific room
DEL  /api/v1/rooms/{roomId}               Delete a room
GET  /api/v1/sensors                       List all sensors
GET  /api/v1/sensors?type={type}          Filter sensors by type
POST /api/v1/sensors                       Register a sensor
GET  /api/v1/sensors/{sensorId}           Get a specific sensor
DEL  /api/v1/sensors/{sensorId}           Delete a sensor
GET  /api/v1/sensors/{sensorId}/readings  Get reading history
POST /api/v1/sensors/{sensorId}/readings  Add a new reading

---

## How to Build and Run

### Prerequisites

- Java JDK 8 or higher installed
- Apache NetBeans IDE installed
- Apache Tomcat 9 configured in NetBeans Services tab
- Maven (bundled with NetBeans)

### Steps

1. Clone this repository:
git clone https://github.com/YOUR_USERNAME/smart-campus-api.git

2. Open NetBeans and go to File → Open Project

3. Navigate to the cloned folder and select it, then click Open Project

4. Right-click the project in the Projects panel and select Clean and Build.
   Wait for Maven to download dependencies and build successfully.

5. Right-click the project again and select Run.
   Tomcat will start and deploy the application automatically.

6. The API is now available at:
http://localhost:8080/smart-campus-api/api/v1/

---

## Sample curl Commands

### 1. Discovery — get API metadata and links
curl -X GET http://localhost:8080/smart-campus-api/api/v1/

### 2. Create a room
curl -X POST http://localhost:8080/smart-campus-api/api/v1/rooms 
-H "Content-Type: application/json" 
-d "{"id":"LIB-301","name":"Library Quiet Study","capacity":50}"

### 3. Create a sensor linked to that room
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors 
-H "Content-Type: application/json" 
-d "{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":0.0,"roomId":"LIB-301"}"

### 4. Get all sensors filtered by type
curl -X GET "http://localhost:8080/smart-campus-api/api/v1/sensors?type=Temperature"

### 5. Post a reading to a sensor
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors/TEMP-001/readings 
-H "Content-Type: application/json" 
-d "{"value":23.5}"

### 6. Attempt to delete a room that still has sensors (expect 409 Conflict)
curl -X DELETE http://localhost:8080/smart-campus-api/api/v1/rooms/LIB-301

### 7. Attempt to create a sensor with an invalid roomId (expect 422)
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors 
-H "Content-Type: application/json" 
-d "{"id":"CO2-999","type":"CO2","status":"ACTIVE","currentValue":0.0,"roomId":"FAKE-ROOM"}"

---

## Author

Student Name: Samitha Bandara  
Student ID: w2119852 (20240161)  
Module: 5COSC022W Client-Server Architectures  
University of Westminster