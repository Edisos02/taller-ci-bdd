package cl.empresa.demo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prueba de integración: levanta el servidor HTTP real (LoginHttpServer)
 * y verifica que, trabajando junto con LoginService, responde
 * correctamente a peticiones reales sobre el endpoint /login.
 * A diferencia de una prueba unitaria, aquí NO se aísla una sola clase:
 * se prueban varias capas integradas (servidor HTTP + lógica de negocio).
 */
class LoginIntegrationTest {

    private static final String URL = "http://localhost:8080/login";
    private static final HttpClient client = HttpClient.newHttpClient();

    @BeforeAll
    static void iniciarServidor() throws Exception {
        // Arranca el mismo servidor real que usas en producción/local
        LoginHttpServer.main(new String[0]);
        // Pequeña espera para asegurar que el servidor ya está escuchando
        Thread.sleep(300);
    }

    @Test
    void loginConCredencialesValidas_debeResponderOk() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .POST(HttpRequest.BodyPublishers.ofString("{\"usuario\":\"admin\",\"clave\":\"1234\"}"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("ok"));
    }

    @Test
    void loginConCredencialesInvalidas_debeResponderDenegado() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .POST(HttpRequest.BodyPublishers.ofString("{\"usuario\":\"admin\",\"clave\":\"incorrecta\"}"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(401, response.statusCode());
        assertTrue(response.body().contains("denied"));
    }
}
