package com.politecnicomalaga.sp.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.politecnicomalaga.sp.util.RandomUtils;
import com.politecnicomalaga.sp.model.Ovni.Estado;
import com.politecnicomalaga.sp.model.Ovni.Direccion;

/**
 * The test class OvniTest.
 */
public class OvniTest {

    public OvniTest() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
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
            String textura = RandomUtils.randomString(5, 20);

            // Act
            Ovni ovni = new Ovni(x, y, width, height, estado, dir, textura);

            // Assert Getters
            assertNotNull(ovni);
            assertEquals(x, ovni.getX());
            assertEquals(y, ovni.getY());
            assertEquals(width, ovni.getWidth());
            assertEquals(height, ovni.getHeight());
            assertEquals(estado, ovni.getEstado());
            assertEquals(dir, ovni.getDir());
            assertEquals(textura, ovni.getTextura());
            assertEquals(width / 2, ovni.getMitadWidth());
            assertEquals(height / 2, ovni.getMitadHeight());

            // Arrange Setters
            float newX = RandomUtils.randomFloat();
            float newY = RandomUtils.randomFloat();
            float newWidth = RandomUtils.randomFloatNo0();
            float newHeight = RandomUtils.randomFloatNo0();
            Estado newEstado = RandomUtils.randomEstado();
            Direccion newDir = RandomUtils.randomDireccion();
            String newTextura = RandomUtils.randomString(5, 20);

            // Act Setters
            ovni.setX(newX);
            ovni.setY(newY);
            ovni.setWidth(newWidth);
            ovni.setHeight(newHeight);
            ovni.setEstado(newEstado);
            ovni.setDir(newDir);
            ovni.setTextura(newTextura);

            // Assert Setters
            assertEquals(newX, ovni.getX());
            assertEquals(newY, ovni.getY());
            assertEquals(newWidth, ovni.getWidth());
            assertEquals(newHeight, ovni.getHeight());
            assertEquals(newEstado, ovni.getEstado());
            assertEquals(newDir, ovni.getDir());
            assertEquals(newTextura, ovni.getTextura());
        }
    }

    @Test
    public void testColision() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            float x1 = RandomUtils.randomFloat();
            float y1 = RandomUtils.randomFloat();
            float w1 = 10f;
            float h1 = 10f;

            Ovni ovni1 = new Ovni(x1, y1, w1, h1, Estado.VIVO, Direccion.NOMOVER, "1.png");

            // Solapados (Colisionan)
            float x2Colision = x1 + 5f;
            float y2Colision = y1 + 5f;
            Ovni ovni2Colision = new Ovni(x2Colision, y2Colision, 10f, 10f, Estado.VIVO, Direccion.NOMOVER, "2.png");

            // No solapados (No colisionan)
            float x3NoColision = x1 + 20f;
            float y3NoColision = y1 + 20f;
            Ovni ovni3NoColision = new Ovni(x3NoColision, y3NoColision, 10f, 10f, Estado.VIVO, Direccion.NOMOVER, "3.png");

            // Act & Assert
            assertTrue(ovni1.colision(ovni2Colision));
            assertFalse(ovni1.colision(ovni3NoColision));
        }
    }

    @Test
    public void testMover() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            float startX = RandomUtils.randomFloat();
            float startY = RandomUtils.randomFloat();
            float velocidad = RandomUtils.randomFloatNo0();
            float delta = 0.016f; // Simulation of ~60fps step
            
            Ovni ovniArriba = new Ovni(startX, startY, 10, 10, Estado.VIVO, Direccion.ARRIBA, "tex");
            Ovni ovniAbajo = new Ovni(startX, startY, 10, 10, Estado.VIVO, Direccion.ABAJO, "tex");
            Ovni ovniDer = new Ovni(startX, startY, 10, 10, Estado.VIVO, Direccion.DERECHA, "tex");
            Ovni ovniIzq = new Ovni(startX, startY, 10, 10, Estado.VIVO, Direccion.IZQUIERDA, "tex");

            // Act
            ovniArriba.mover(Direccion.ARRIBA, velocidad, delta);
            ovniAbajo.mover(Direccion.ABAJO, velocidad, delta);
            ovniDer.mover(Direccion.DERECHA, velocidad, delta);
            ovniIzq.mover(Direccion.IZQUIERDA, velocidad, delta);

            // Assert
            assertEquals(startY + (velocidad * delta), ovniArriba.getY(), 0.001f);
            assertEquals(startY - (velocidad * delta), ovniAbajo.getY(), 0.001f);
            assertEquals(startX + (velocidad * delta), ovniDer.getX(), 0.001f);
            assertEquals(startX - (velocidad * delta), ovniIzq.getX(), 0.001f);
        }
    }

    @Test
    public void testEstaVivo() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            Ovni ovniVivo = new Ovni(0, 0, 10, 10, Estado.VIVO, Direccion.NOMOVER, "tex");
            Ovni ovniMuerto = new Ovni(0, 0, 10, 10, Estado.MUERTO, Direccion.NOMOVER, "tex");

            // Act & Assert
            assertTrue(ovniVivo.estaVivo());
            assertFalse(ovniMuerto.estaVivo());
        }
    }
}
