package com.politecnicomalaga.sp.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.politecnicomalaga.sp.util.RandomUtils;
import com.politecnicomalaga.sp.model.Ovni.Estado;
import com.politecnicomalaga.sp.model.Ovni.Direccion;

public class BunkerTest {

    @Test
    public void testConstructorGettersYSetters() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            float x = RandomUtils.randomFloat();
            float y = RandomUtils.randomFloat();
            float width = RandomUtils.randomFloatNo0();
            float height = RandomUtils.randomFloatNo0();
            int vidasMaximas = RandomUtils.randomInt(1, 10);

            // Act
            Bunker bunker = new Bunker(x, y, width, height, vidasMaximas);

            // Assert Getters
            assertNotNull(bunker);
            assertEquals(x, bunker.getX());
            assertEquals(y, bunker.getY());
            assertEquals(width, bunker.getWidth());
            assertEquals(height, bunker.getHeight());
            assertEquals(vidasMaximas, bunker.getVidas());
            assertEquals(Estado.VIVO, bunker.getEstado());
            assertEquals(Direccion.NOMOVER, bunker.getDir());
            assertEquals("bunker_" + vidasMaximas + ".png", bunker.getTextura());

            // Arrange Setters (inherited)
            float newX = RandomUtils.randomFloat();
            float newY = RandomUtils.randomFloat();

            // Act Setters
            bunker.setX(newX);
            bunker.setY(newY);

            // Assert Setters
            assertEquals(newX, bunker.getX());
            assertEquals(newY, bunker.getY());
        }
    }

    @Test
    public void testRecibirDano_ReduceVidaYCambiaTextura() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            int vidasMaximas = RandomUtils.randomInt(2, 10);
            Bunker bunker = new Bunker(0, 0, 10, 10, vidasMaximas);

            // Act
            bunker.recibirDano();

            // Assert
            assertEquals(vidasMaximas - 1, bunker.getVidas());
            assertEquals(Estado.VIVO, bunker.getEstado());
            assertEquals("bunker_" + (vidasMaximas - 1) + ".png", bunker.getTextura());
        }
    }

    @Test
    public void testRecibirDano_MuereAlLlegarA0() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            Bunker bunker = new Bunker(0, 0, 10, 10, 1);

            // Act
            bunker.recibirDano();

            // Assert
            assertEquals(0, bunker.getVidas());
            assertEquals(Estado.MUERTO, bunker.getEstado());
        }
    }

    @Test
    public void testDestruir() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            int vidasMaximas = RandomUtils.randomInt(2, 10);
            Bunker bunker = new Bunker(0, 0, 10, 10, vidasMaximas);

            // Act
            bunker.destruir();

            // Assert
            assertEquals(0, bunker.getVidas());
            assertEquals(Estado.MUERTO, bunker.getEstado());
        }
    }

    @Test
    public void testMover_NoHaceNada() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            float startX = RandomUtils.randomFloat();
            float startY = RandomUtils.randomFloat();
            Bunker bunker = new Bunker(startX, startY, 10, 10, 3);
            
            // Act
            bunker.mover(Direccion.ABAJO, 10f, 1f);
            bunker.mover(Direccion.ARRIBA, 10f, 1f);
            bunker.mover(Direccion.IZQUIERDA, 10f, 1f);
            bunker.mover(Direccion.DERECHA, 10f, 1f);

            // Assert - Position shouldn't change
            assertEquals(startX, bunker.getX());
            assertEquals(startY, bunker.getY());
        }
    }
}
