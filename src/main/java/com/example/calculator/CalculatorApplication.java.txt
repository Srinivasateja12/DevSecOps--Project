package com.example.calculator;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.InetSocketAddress;

public class CalculatorServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        server.createContext("/", new StaticFileHandler("resources/public/index.html"));
        server.createContext("/styles.css", new StaticFileHandler("resources/public/styles.css"));
        server.createContext("/script.js", new StaticFileHandler("resources/public/script.js"));
        
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on http://localhost:8080");
    }
}

class StaticFileHandler implements HttpHandler {
    private final String filePath;
    
    public StaticFileHandler(String filePath) {
        this.filePath = filePath;
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        byte[] response = Files.readAllBytes(Paths.get(filePath));
        exchange.sendResponseHeaders(200, response.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }
}
