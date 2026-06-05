package com.politecnicomalaga.sp.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.politecnicomalaga.sp.util.RandomUtils;
import com.politecnicomalaga.sp.model.Ovni.Estado;
import com.politecnicomalaga.sp.model.Ovni.Direccion;

public class DisparoTest {

    // Dummy subclass to test abstract class
    private static class DummyDisparo extends Disparo {
        public DummyDisparo(float x, float y, float width, float height, Estado estado, Direccion dir, String textura) {
            super(x, y, width, height, estado, dir, textura);
        }

        @Override
        public void desaparecer(float limite) {
            // No-op
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

            // Act
            DummyDisparo disparo = new DummyDisparo(x, y, width, height, estado, dir, textura);

            // Assert Getters
            assertNotNull(disparo);
            assertEquals(x, disparo.getX());
            assertEquals(y, disparo.getY());
            assertEquals(width, disparo.getWidth());
            assertEquals(height, disparo.getHeight());
            assertEquals(estado, disparo.getEstado());
            assertEquals(dir, disparo.getDir());
            assertEquals(textura, disparo.getTextura());

            // Arrange Setters (inherited)
            float newX = RandomUtils.randomFloat();
            float newY = RandomUtils.randomFloat();

            // Act Setters
            disparo.setX(newX);
            disparo.setY(newY);

            // Assert Setters
            assertEquals(newX, disparo.getX());
            assertEquals(newY, disparo.getY());
        }
    }
}
