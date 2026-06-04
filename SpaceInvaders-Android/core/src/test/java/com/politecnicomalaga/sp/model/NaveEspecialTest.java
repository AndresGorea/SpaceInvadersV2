package com.politecnicomalaga.sp.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.politecnicomalaga.sp.util.RandomUtils;
import com.politecnicomalaga.sp.model.Ovni.Estado;
import com.politecnicomalaga.sp.model.Ovni.Direccion;

public class NaveEspecialTest {

    @Test
    public void testConstructorGettersYSetters() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            float x = RandomUtils.randomFloat();
            float y = RandomUtils.randomFloat();
            float width = RandomUtils.randomFloatNo0();
            float height = RandomUtils.randomFloatNo0();
            Direccion dir = RandomUtils.randomBoolean() ? Direccion.DERECHA : Direccion.IZQUIERDA;
            float velocidad = RandomUtils.randomFloatNo0();
            String textura = RandomUtils.randomString(5, 15);

            // Act
            NaveEspecial nave = new NaveEspecial(x, y, width, height, dir, velocidad, textura);

            // Assert Getters
            assertNotNull(nave);
            assertEquals(x, nave.getX());
            assertEquals(y, nave.getY());
            assertEquals(width, nave.getWidth());
            assertEquals(height, nave.getHeight());
            assertEquals(dir, nave.getDir());
            assertNotNull(nave.getMisDisparos());
            assertTrue(nave.getMisDisparos().isEmpty());

            // Arrange Setters (inherited)
            float newX = RandomUtils.randomFloat();
            float newY = RandomUtils.randomFloat();

            // Act Setters
            nave.setX(newX);
            nave.setY(newY);

            // Assert Setters
            assertEquals(newX, nave.getX());
            assertEquals(newY, nave.getY());
        }
    }

    @Test
    public void testActualizar() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            float startX = RandomUtils.randomFloat();
            float velocidad = RandomUtils.randomFloatNo0();
            Direccion dir = RandomUtils.randomBoolean() ? Direccion.DERECHA : Direccion.IZQUIERDA;
            NaveEspecial nave = new NaveEspecial(startX, 0, 10, 10, dir, velocidad, "tex");
            float delta = 0.016f;

            // Act
            nave.actualizar(delta);

            // Assert
            if (dir == Direccion.DERECHA) {
                assertEquals(startX + (velocidad * delta), nave.getX(), 0.001f);
            } else {
                assertEquals(startX - (velocidad * delta), nave.getX(), 0.001f);
            }
        }
    }

    @Test
    public void testDisparar() {
        // Arrange
        NaveEspecial nave = new NaveEspecial(0, 0, 20, 20, Direccion.DERECHA, 10f, "tex");

        // Act
        nave.disparar();

        // Assert
        assertEquals(1, nave.getMisDisparos().size());
        DisparoEne d = nave.getMisDisparos().get(0);
        assertEquals(Direccion.ABAJO, d.getDir());
    }

    @Test
    public void testHaSalido() {
        // Arrange
        float anchoMundo = 100f;
        float width = 10f;
        
        // Nave hacia derecha
        NaveEspecial naveDer1 = new NaveEspecial(105f, 0, width, width, Direccion.DERECHA, 10f, "tex"); // fuera
        NaveEspecial naveDer2 = new NaveEspecial(50f, 0, width, width, Direccion.DERECHA, 10f, "tex"); // dentro
        
        // Nave hacia izquierda
        NaveEspecial naveIzq1 = new NaveEspecial(-15f, 0, width, width, Direccion.IZQUIERDA, 10f, "tex"); // fuera
        NaveEspecial naveIzq2 = new NaveEspecial(50f, 0, width, width, Direccion.IZQUIERDA, 10f, "tex"); // dentro

        // Act & Assert
        assertTrue(naveDer1.haSalido(anchoMundo));
        assertFalse(naveDer2.haSalido(anchoMundo));
        
        assertTrue(naveIzq1.haSalido(anchoMundo));
        assertFalse(naveIzq2.haSalido(anchoMundo));
    }
}
