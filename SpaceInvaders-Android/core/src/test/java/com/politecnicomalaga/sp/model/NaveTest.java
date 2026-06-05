package com.politecnicomalaga.sp.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.politecnicomalaga.sp.util.RandomUtils;
import com.politecnicomalaga.sp.model.Ovni.Estado;
import com.politecnicomalaga.sp.model.Ovni.Direccion;

/**
 * The test class NaveTest.
 * Nave is abstract, so we use a dummy subclass for testing its concrete logic.
 */
public class NaveTest {

    // Dummy subclass to test abstract Nave
    private static class DummyNave extends Nave {
        public DummyNave(float x, float y, float width, float height, Estado estado, Direccion dir, String textura, int vidas, float cadencia, float anchoBala, float altoBala, float velocidadBala) {
            super(x, y, width, height, estado, dir, textura, vidas, cadencia, anchoBala, altoBala, velocidadBala);
        }

        @Override
        public void disparar() {
            // No-op for testing
        }

        @Override
        public void gestionarMisDisparos(float limiteMuerte, float delta) {
            // No-op for testing
        }
    }

    @Test
    public void testConstructorGettersYSetters() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            float x = RandomUtils.randomFloat();
            float y = RandomUtils.randomFloat();
            float width = RandomUtils.randomFloatNo0();
            float height = RandomUtils.randomFloatNo0();
            Estado estado = RandomUtils.randomEstado();
            Direccion dir = RandomUtils.randomDireccion();
            String textura = RandomUtils.randomString(5, 15);
            int vidas = RandomUtils.randomInt(1, 10);
            float cadencia = RandomUtils.randomFloatNo0();
            float anchoBala = RandomUtils.randomFloatNo0();
            float altoBala = RandomUtils.randomFloatNo0();
            float velocidadBala = RandomUtils.randomFloatNo0();

            // Act
            DummyNave nave = new DummyNave(x, y, width, height, estado, dir, textura, vidas, cadencia, anchoBala, altoBala, velocidadBala);

            // Assert Getters
            assertNotNull(nave);
            assertEquals(vidas, nave.getVidas());
            assertEquals(cadencia, nave.getCadencia());
            assertEquals(anchoBala, nave.getAnchoBala());
            assertEquals(altoBala, nave.getAltoBala());
            assertEquals(velocidadBala, nave.getVelocidadBala());

            // Arrange Setters
            int newVidas = RandomUtils.randomInt(1, 10);
            float newCadencia = RandomUtils.randomFloatNo0();
            float newAnchoBala = RandomUtils.randomFloatNo0();
            float newAltoBala = RandomUtils.randomFloatNo0();
            float newVelocidadBala = RandomUtils.randomFloatNo0();
            int newPuntos = RandomUtils.randomInt(10, 500);

            // Act Setters
            nave.setVidas(newVidas);
            nave.setCadencia(newCadencia);
            nave.setAnchoBala(newAnchoBala);
            nave.setAltoBala(newAltoBala);
            nave.setVelocidadBala(newVelocidadBala);
            nave.setPuntos(newPuntos);

            // Assert Setters
            assertEquals(newVidas, nave.getVidas());
            assertEquals(newCadencia, nave.getCadencia());
            assertEquals(newAnchoBala, nave.getAnchoBala());
            assertEquals(newAltoBala, nave.getAltoBala());
            assertEquals(newVelocidadBala, nave.getVelocidadBala());
            assertEquals(newPuntos, nave.getPuntos());
        }
    }

    @Test
    public void testRecibirDisparo_PierdeVida() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            int vidasIniciales = RandomUtils.randomInt(2, 10);
            DummyNave nave = new DummyNave(0, 0, 10, 10, Estado.VIVO, Direccion.NOMOVER, "tex", vidasIniciales, 1f, 5, 5, 10);

            // Act
            nave.recibirDisparo();

            // Assert
            assertEquals(vidasIniciales - 1, nave.getVidas());
            assertEquals(Estado.VIVO, nave.getEstado());
        }
    }

    @Test
    public void testRecibirDisparo_MuereAlLlegarA0() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            DummyNave nave = new DummyNave(0, 0, 10, 10, Estado.VIVO, Direccion.NOMOVER, "tex", 1, 1f, 5, 5, 10);

            // Act
            nave.recibirDisparo();

            // Assert
            assertEquals(0, nave.getVidas());
            assertEquals(Estado.MUERTO, nave.getEstado());
        }
    }
    
    @Test
    public void testRecibirDisparo_NoHaceNadaSiEstaMuerto() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            DummyNave nave = new DummyNave(0, 0, 10, 10, Estado.MUERTO, Direccion.NOMOVER, "tex", 0, 1f, 5, 5, 10);

            // Act
            nave.recibirDisparo();

            // Assert
            assertEquals(0, nave.getVidas());
            assertEquals(Estado.MUERTO, nave.getEstado());
        }
    }
}
