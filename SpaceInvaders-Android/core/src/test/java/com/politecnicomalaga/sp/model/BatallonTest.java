package com.politecnicomalaga.sp.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.politecnicomalaga.sp.util.RandomUtils;
import com.politecnicomalaga.sp.model.Ovni.Estado;
import com.politecnicomalaga.sp.model.Ovni.Direccion;

public class BatallonTest {

    @Test
    public void testConstructorGettersYSetters() {
        for (int i = 0; i < 100; i++) {
            // Arrange
            float x = RandomUtils.randomFloat();
            float y = RandomUtils.randomFloat();
            float espacioVertical = 10f;
            float width = 10f;
            float height = 10f;
            Direccion dir = Direccion.DERECHA;
            float velocidad = RandomUtils.randomFloatNo0();
            int probabilidad = RandomUtils.randomInt(0, 100);
            float espacioEntreNaves = 5f;

            // Act
            Batallon batallon = new Batallon(x, y, espacioVertical, width, height, Estado.VIVO, dir, "tex", 1, 1f, 5, 5, 10, probabilidad, espacioEntreNaves, velocidad);

            // Assert
            assertNotNull(batallon);
            assertEquals(velocidad, batallon.getVelocidad());
            assertEquals(dir, batallon.getDireccionActual());
            assertNotNull(batallon.getEscuadrones());
            assertEquals(4, batallon.getEscuadrones().length);
            
            assertEquals(32, batallon.getMaxNaves());
            assertEquals(32, batallon.getCantidadNavesVivas());
            assertTrue(batallon.tieneTropas());
        }
    }

    @Test
    public void testMover_TocaBorde() {
        // Arrange
        float anchoPantalla = 100f;
        float xInicial = 80f; // Cerca del borde derecho
        float cuantoBaja = 10f;
        float delta = 0.016f;
        
        Batallon batallon = new Batallon(xInicial, 100f, 10f, 10f, 10f, Estado.VIVO, Direccion.DERECHA, "tex", 1, 1f, 5, 5, 10, 10, 5, 10f);
        
        float yEscuadron0Inicial = batallon.getEscuadrones()[0].getNavesEnemigas()[0].getY();

        // Act
        batallon.mover(anchoPantalla, 200f, cuantoBaja, delta);

        // Assert
        assertEquals(Direccion.IZQUIERDA, batallon.getDireccionActual());
        assertEquals(yEscuadron0Inicial - cuantoBaja, batallon.getEscuadrones()[0].getNavesEnemigas()[0].getY(), 0.001f);
    }

    @Test
    public void testMover_NoTocaBorde() {
        // Arrange
        float anchoPantalla = 1000f;
        float xInicial = 50f; // Lejos de los bordes
        float cuantoBaja = 10f;
        float delta = 0.016f;
        float velocidad = 10f;
        
        Batallon batallon = new Batallon(xInicial, 100f, 10f, 10f, 10f, Estado.VIVO, Direccion.DERECHA, "tex", 1, 1f, 5, 5, 10, 10, 5, velocidad);
        
        float xEscuadron0Inicial = batallon.getEscuadrones()[0].getNavesEnemigas()[0].getX();
        float yEscuadron0Inicial = batallon.getEscuadrones()[0].getNavesEnemigas()[0].getY();

        // Act
        batallon.mover(anchoPantalla, 200f, cuantoBaja, delta);

        // Assert
        assertEquals(Direccion.DERECHA, batallon.getDireccionActual());
        assertEquals(xEscuadron0Inicial + (velocidad * delta), batallon.getEscuadrones()[0].getNavesEnemigas()[0].getX(), 0.001f);
        assertEquals(yEscuadron0Inicial, batallon.getEscuadrones()[0].getNavesEnemigas()[0].getY(), 0.001f); // No ha bajado
    }

    @Test
    public void testTieneTropas() {
        // Arrange
        Batallon batallon = new Batallon(0, 0, 10f, 10f, 10f, Estado.VIVO, Direccion.DERECHA, "tex", 1, 1f, 5, 5, 10, 10, 5, 10f);
        
        // Act & Assert
        assertTrue(batallon.tieneTropas());
        
        // Matamos a todos
        for (Escuadron esc : batallon.getEscuadrones()) {
            for (NaveEne nave : esc.getNavesEnemigas()) {
                nave.setEstado(Estado.MUERTO);
            }
        }
        
        assertFalse(batallon.tieneTropas());
        assertEquals(0, batallon.getCantidadNavesVivas());
    }
}
