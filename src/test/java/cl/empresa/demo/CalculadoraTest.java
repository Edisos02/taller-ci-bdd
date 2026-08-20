package cl.empresa.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraTest {

    @Test
    void deberiaSumarDosNumeros() {
        Calculadora calculadora = new Calculadora();
        assertEquals(8, calculadora.sumar(5, 3));
    }

    @Test
    void deberiaRestarDosNumeros() {
        Calculadora calculadora = new Calculadora();
        assertEquals(2, calculadora.restar(5, 3));
    }
}