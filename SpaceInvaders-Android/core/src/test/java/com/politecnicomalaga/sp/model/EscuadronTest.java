package com.politecnicomalaga.sp.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.politecnicomalaga.sp.util.RandomUtils;
import com.politecnicomalaga.sp.model.Ovni.Estado;
import com.politecnicomalaga.sp.model.Ovni.Direccion;

public class EscuadronTest {

    @Test
    public void testConstructorGettersYSetters() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            float x = RandomUtils.randomFloat();
            float y = RandomUtils.randomFloat();
            float width = 10f;
            float height = 10f;
            float espacioEntreNaves = 5f;
            int probabilidad = RandomUtils.randomInt(0, 100);

            // Act
            Escuadron escuadron = new Escuadron(x, y, width, height, Estado.VIVO, Direccion.NOMOVER, "tex", 1, 1f, 5, 5, 10, probabilidad, espacioEntreNaves, 100);

            // Assert
            assertNotNull(escuadron);
            NaveEne[] naves = escuadron.getNavesEnemigas();
            assertNotNull(naves);
            assertEquals(8, naves.length);

            // Verifica posicionamiento correcto
            for (int j = 0; j < naves.length; j++) {
                assertEquals(x + (j * (width + espacioEntreNaves)), naves[j].getX(), 0.001f);
                assertEquals(y, naves[j].getY(), 0.001f);
                assertTrue(naves[j].estaVivo());
            }
            
            assertEquals(8, escuadron.getCantidadNavesVivas());
            assertTrue(escuadron.tieneNavesVivas());
        }
    }

    @Test
    public void testHaTocadoBorde() {
        // Arrange
        float anchoPantalla = 100f;
        // Escuadron cerca del borde derecho
        Escuadron escuadronDer = new Escuadron(80f, 0, 10, 10, Estado.VIVO, Direccion.DERECHA, "tex", 1, 1f, 5, 5, 10, 10, 5, 10);
        
        // Act & Assert
        // El escuadron ocupa 8 * 10 + 7 * 5 = 80 + 35 = 115 ancho, empezando en 80...
        // Por lo que naveEne.getX() + width de las ultimas seran mayores a 100
        assertTrue(escuadronDer.haTocadoBorde(anchoPantalla, Direccion.DERECHA));
        assertFalse(escuadronDer.haTocadoBorde(anchoPantalla, Direccion.IZQUIERDA)); // No toca izq y va izq
        
        // Escuadron cerca del borde izquierdo
        Escuadron escuadronIzq = new Escuadron(-10f, 0, 10, 10, Estado.VIVO, Direccion.IZQUIERDA, "tex", 1, 1f, 5, 5, 10, 10, 5, 10);
        
        assertTrue(escuadronIzq.haTocadoBorde(anchoPantalla, Direccion.IZQUIERDA));
    }

    @Test
    public void testMoverLateralmente() {
        for (int i = 0; i < 100; i++) {
            // Arrange
            float x = RandomUtils.randomFloat();
            float velocidad = RandomUtils.randomFloatNo0();
            float delta = 0.016f;
            Escuadron escuadron = new Escuadron(x, 0, 10, 10, Estado.VIVO, Direccion.DERECHA, "tex", 1, 1f, 5, 5, 10, 10, 5, 10);
            
            float[] inicialesX = new float[8];
            for (int j = 0; j < 8; j++) {
                inicialesX[j] = escuadron.getNavesEnemigas()[j].getX();
            }

            // Act
            escuadron.moverLateralmente(Direccion.DERECHA, velocidad, delta);

            // Assert
            for (int j = 0; j < 8; j++) {
                assertEquals(inicialesX[j] + (velocidad * delta), escuadron.getNavesEnemigas()[j].getX(), 0.001f);
            }
        }
    }

    @Test
    public void testBajar() {
        for (int i = 0; i < 100; i++) {
            // Arrange
            float y = RandomUtils.randomFloat();
            float cuantoBaja = RandomUtils.randomFloatNo0();
            Escuadron escuadron = new Escuadron(0, y, 10, 10, Estado.VIVO, Direccion.DERECHA, "tex", 1, 1f, 5, 5, 10, 10, 5, 10);
            
            // Act
            escuadron.bajar(cuantoBaja);

            // Assert (bajar is mover(ABAJO, cuantoBaja, 1f) so new Y = y - cuantoBaja
            for (int j = 0; j < 8; j++) {
                assertEquals(y - cuantoBaja, escuadron.getNavesEnemigas()[j].getY(), 0.001f);
            }
        }
    }

    @Test
    public void testTieneNavesVivas() {
        // Arrange
        Escuadron escuadron = new Escuadron(0, 0, 10, 10, Estado.VIVO, Direccion.DERECHA, "tex", 1, 1f, 5, 5, 10, 10, 5, 10);
        
        // Act & Assert
        assertTrue(escuadron.tieneNavesVivas());
        
        for (NaveEne n : escuadron.getNavesEnemigas()) {
            n.setEstado(Estado.MUERTO);
        }
        
        assertFalse(escuadron.tieneNavesVivas());
        assertEquals(0, escuadron.getCantidadNavesVivas());
    }

    @Test
    public void testDispararYGestionarDisparos() {
        // Arrange
        // Probabilidad = 100 para asegurar que todas disparan
        Escuadron escuadron = new Escuadron(0, 0, 10, 10, Estado.VIVO, Direccion.DERECHA, "tex", 1, 1f, 5, 5, 10, 100, 5, 10);
        
        // Act
        escuadron.disparar();

        // Assert
        int totalDisparos = 0;
        for (NaveEne n : escuadron.getNavesEnemigas()) {
            totalDisparos += n.getMisDisparos().size();
        }
        assertEquals(8, totalDisparos);

        // Act - Gestionar disparos (mover fuera de pantalla)
        escuadron.gestionarDisparosEnemigos(100f, 1f); // limite alto para que mueran
        
        totalDisparos = 0;
        for (NaveEne n : escuadron.getNavesEnemigas()) {
            totalDisparos += n.getMisDisparos().size();
        }
        assertEquals(0, totalDisparos);
    }
}
