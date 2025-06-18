# HTTP-Server-and-Request-Parser-Project
A lightweight HTTP server implementation in Java, featuring custom request parsing and servlet handling.   Developed as part of the Advanced Software Development course (פת"מ 1), this project simulates basic RESTful API behavior without relying on external HTTP libraries.

It was developed as part of an academic Software Engineering course (פת"מ 1) and received a perfect score (95).

Features
Full implementation of a modular HTTP server from scratch

Supports basic HTTP methods: GET, POST, and DELETE

Custom routing mechanism with longest prefix matching

Multithreaded client handling using a thread pool

Request parsing with support for URI parameters and request body

Easily extendable via the Servlet interface

Structure
MyHTTPServer.java: The core server class implementing HTTPServer

RequestParser.java: Parses raw HTTP requests into structured RequestInfo objects

Servlet.java: Interface for handling HTTP requests

MainTrain.java: Test suite for parsing and server execution

Example Usage
java
Copy
Edit
MyHTTPServer server = new MyHTTPServer(8080, 4);
server.addServlet("GET", "/api/echo", new EchoServlet());
server.start();
Developed by: Rivkah Ayalon
Course: Advanced Software Development (פת"מ 1)
Final Grade: 95
