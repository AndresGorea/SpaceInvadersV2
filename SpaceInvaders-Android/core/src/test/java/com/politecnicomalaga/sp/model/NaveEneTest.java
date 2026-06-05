package com.politecnicomalaga.sp.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.politecnicomalaga.sp.util.RandomUtils;
import com.politecnicomalaga.sp.model.Ovni.Estado;
import com.politecnicomalaga.sp.model.Ovni.Direccion;

public class NaveEneTest {

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
            int probabilidad = RandomUtils.randomInt(0, 100);
            int puntos = RandomUtils.randomInt(10, 500);

            // Act
            NaveEne naveEne = new NaveEne(x, y, width, height, estado, dir, textura, vidas, cadencia, anchoBala, altoBala, velocidadBala, probabilidad, puntos);

            // Assert Getters
            assertNotNull(naveEne);
            assertEquals(probabilidad, naveEne.getProbabilidadDisparo());
            assertEquals(puntos, naveEne.getPuntos());
            assertNotNull(naveEne.getMisDisparos());
            assertTrue(naveEne.getMisDisparos().isEmpty());

            // Arrange Setters
            int newProbabilidad = RandomUtils.randomInt(0, 100);
            int newPuntos = RandomUtils.randomInt(10, 500);

            // Act Setters
            naveEne.setProbabilidadDisparo(newProbabilidad);
            naveEne.setPuntos(newPuntos);

            // Assert Setters
            assertEquals(newProbabilidad, naveEne.getProbabilidadDisparo());
            assertEquals(newPuntos, naveEne.getPuntos());
        }
    }

    @Test
    public void testDisparar_SiempreDispara() {
        // Arrange
        NaveEne naveEne = new NaveEne(0, 0, 20, 20, Estado.VIVO, Direccion.NOMOVER, "tex", 3, 1f, 5, 5, 10, 100, 100); // 100% prob
        naveEne.getMisDisparos().clear();

        // Act
        naveEne.disparar();

        // Assert
        assertEquals(1, naveEne.getMisDisparos().size());
        DisparoEne d = naveEne.getMisDisparos().get(0);
        assertEquals(Direccion.ABAJO, d.getDir());
    }

    @Test
    public void testDisparar_NuncaDispara() {
        // Arrange
        NaveEne naveEne = new NaveEne(0, 0, 20, 20, Estado.VIVO, Direccion.NOMOVER, "tex", 3, 1f, 5, 5, 10, 0, 100); // 0% prob
        naveEne.getMisDisparos().clear();

        // Act
        naveEne.disparar();

        // Assert
        assertEquals(0, naveEne.getMisDisparos().size());
    }

    @Test
    public void testGestionarMisDisparos() {
        // Arrange
        NaveEne naveEne = new NaveEne(0, 0, 20, 20, Estado.VIVO, Direccion.NOMOVER, "tex", 3, 1f, 5, 5, 10, 100, 100);
        naveEne.disparar();
        DisparoEne d = naveEne.getMisDisparos().get(0);
        float startY = d.getY();
        
        // Act - Mover disparo
        naveEne.gestionarMisDisparos(-1000f, 1f); // Limite muy bajo para no desaparecer

        // Assert
        assertTrue(d.getY() < startY); // Se ha movido hacia abajo
        assertEquals(1, naveEne.getMisDisparos().size());

        // Act - Disparo sale de la pantalla
        // En desaparecer(), desaparece si getY() < limite
        naveEne.gestionarMisDisparos(100f, 1f); // Limite alto para forzar desaparecer

        // Assert
        assertEquals(0, naveEne.getMisDisparos().size());
    }
}
