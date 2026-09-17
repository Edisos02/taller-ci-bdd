package cl.empresa.demo;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class LoginHttpServer {

    public static void main(String[] args) throws IOException {
        LoginService loginService = new LoginService();
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/login", (HttpExchange exchange) -> {
            if (!"POST".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            boolean ok = body.contains("\"admin\"") && body.contains("\"1234\"");

            boolean autenticado = loginService.autenticar("admin", "1234") && ok;

            String respuesta = autenticado ? "{\"status\":\"ok\"}" : "{\"status\":\"denied\"}";
            int codigo = autenticado ? 200 : 401;

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(codigo, respuesta.getBytes(StandardCharsets.UTF_8).length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(respuesta.getBytes(StandardCharsets.UTF_8));
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Servidor escuchando en puerto 8080 (v2)");
    }
}