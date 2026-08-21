package cl.empresa.bdd.steps;

import cl.empresa.demo.LoginService;
import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginSteps {
    private final LoginService loginService = new LoginService();
    private String usuario;
    private String clave;
    private boolean resultado;

    @Given("que existe un usuario {string} con clave {string}")
    public void existeUsuario(String usuario, String clave) {
        this.usuario = usuario;
        this.clave = clave;
    }

    @When("el usuario intenta iniciar sesión")
    public void intentaIniciarSesion() {
        resultado = loginService.autenticar(usuario, clave);
    }

    @When("el usuario intenta iniciar sesión con usuario {string} y clave {string}")
    public void intentaIniciarSesionConCredenciales(String usuario, String clave) {
        resultado = loginService.autenticar(usuario, clave);
    }

    @Then("el acceso debe ser concedido")
    public void accesoConcedido() {
        assertTrue(resultado);
    }

    @Then("el acceso debe ser rechazado")
    public void accesoRechazado() {
        assertFalse(resultado);
    }
}