package com.example.calculator;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;

public class CalculatorServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", new StaticFileHandler("public/index.html"));
        server.createContext("/styles.css", new StaticFileHandler("public/styles.css"));
        server.createContext("/script.js", new StaticFileHandler("public/script.js"));

        server.setExecutor(null);
        server.start();
        System.out.println("Server started on http://localhost:8080");
    }
}

class StaticFileHandler implements HttpHandler {
    private final String resourcePath;

    public StaticFileHandler(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        
        if (inputStream == null) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }
        
        byte[] response = inputStream.readAllBytes();
        exchange.sendResponseHeaders(200, response.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }
}

