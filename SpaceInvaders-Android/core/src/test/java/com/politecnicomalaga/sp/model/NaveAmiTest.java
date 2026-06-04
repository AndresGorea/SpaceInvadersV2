package com.politecnicomalaga.sp.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.politecnicomalaga.sp.util.RandomUtils;
import com.politecnicomalaga.sp.model.Ovni.Estado;
import com.politecnicomalaga.sp.model.Ovni.Direccion;
import com.politecnicomalaga.sp.util.Assets;
import com.badlogic.gdx.audio.Sound;

public class NaveAmiTest {

    private MockedStatic<Assets> mockedAssetsStatic;
    private Assets mockAssetsInstance;
    private Sound mockSound;

    @BeforeEach
    public void setUp() {
        // Mock the Assets singleton and the Sound to prevent LibGDX exceptions
        mockAssetsInstance = mock(Assets.class);
        mockSound = mock(Sound.class);

        when(mockAssetsInstance.getSound(anyString())).thenReturn(mockSound);

        mockedAssetsStatic = mockStatic(Assets.class);
        mockedAssetsStatic.when(Assets::getInstance).thenReturn(mockAssetsInstance);
    }

    @AfterEach
    public void tearDown() {
        if (mockedAssetsStatic != null) {
            mockedAssetsStatic.close();
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
            NaveAmi naveAmi = new NaveAmi(x, y, width, height, estado, dir, textura, vidas, cadencia, anchoBala, altoBala, velocidadBala);

            // Assert Getters
            assertNotNull(naveAmi);
            assertNotNull(naveAmi.getMisDisparos());
            assertTrue(naveAmi.getMisDisparos().isEmpty());
            assertEquals(0, naveAmi.getTiempoTripleDisparo());
            assertEquals(0, naveAmi.getTiempoEscudo());
            assertEquals(0, naveAmi.getTiempoVelocidad());
            assertFalse(naveAmi.tieneEscudo());

            // Arrange Setters (inherited)
            float newX = RandomUtils.randomFloat();
            float newY = RandomUtils.randomFloat();

            // Act Setters
            naveAmi.setX(newX);
            naveAmi.setY(newY);

            // Assert Setters
            assertEquals(newX, naveAmi.getX());
            assertEquals(newY, naveAmi.getY());
        }
    }

    @Test
    public void testDisparar_Normal() {
        // Arrange
        NaveAmi naveAmi = new NaveAmi(0, 0, 20, 20, Estado.VIVO, Direccion.NOMOVER, "tex", 3, 1f, 5, 5, 10);
        naveAmi.getMisDisparos().clear();

        // Act
        naveAmi.disparar();

        // Assert
        assertEquals(1, naveAmi.getMisDisparos().size());
        DisparoAmi d = naveAmi.getMisDisparos().get(0);
        assertEquals(Direccion.ARRIBA, d.getDir());
        verify(mockSound, atLeast(1)).play(0.4f);
    }

    @Test
    public void testDisparar_TripleDisparo() {
        // Arrange
        NaveAmi naveAmi = new NaveAmi(0, 0, 20, 20, Estado.VIVO, Direccion.NOMOVER, "tex", 3, 1f, 5, 5, 10);
        naveAmi.activarPowerUp(PowerUp.Tipo.MULTI_DISPARO);
        naveAmi.getMisDisparos().clear();

        // Act
        naveAmi.disparar();

        // Assert
        assertEquals(3, naveAmi.getMisDisparos().size());
    }

    @Test
    public void testActivarPowerUpYActualizar() {
        // Arrange
        NaveAmi naveAmi = new NaveAmi(0, 0, 20, 20, Estado.VIVO, Direccion.NOMOVER, "tex", 3, 1f, 5, 5, 10);

        // Act
        naveAmi.activarPowerUp(PowerUp.Tipo.ESCUDO);
        naveAmi.activarPowerUp(PowerUp.Tipo.MULTI_DISPARO);
        naveAmi.activarPowerUp(PowerUp.Tipo.VELOCIDAD);

        // Assert
        assertTrue(naveAmi.tieneEscudo());
        assertTrue(naveAmi.getTiempoTripleDisparo() > 0);
        assertTrue(naveAmi.getTiempoVelocidad() > 0);
        assertTrue(naveAmi.getVelocidadExtra() > 0);

        // Act 2: Actualizar
        float delta = 1f;
        float expectedEscudo = naveAmi.getTiempoEscudo() - delta;
        float expectedTriple = naveAmi.getTiempoTripleDisparo() - delta;
        float expectedVelocidad = naveAmi.getTiempoVelocidad() - delta;

        naveAmi.actualizarPowerUps(delta);

        // Assert 2
        assertEquals(expectedEscudo, naveAmi.getTiempoEscudo());
        assertEquals(expectedTriple, naveAmi.getTiempoTripleDisparo());
        assertEquals(expectedVelocidad, naveAmi.getTiempoVelocidad());
    }

    @Test
    public void testRecibirDisparo_ConEscudo() {
        // Arrange
        NaveAmi naveAmi = new NaveAmi(0, 0, 20, 20, Estado.VIVO, Direccion.NOMOVER, "tex", 3, 1f, 5, 5, 10);
        naveAmi.activarPowerUp(PowerUp.Tipo.ESCUDO);

        // Act
        naveAmi.recibirDisparo();

        // Assert
        assertEquals(3, naveAmi.getVidas()); // No pierde vida
        assertTrue(naveAmi.tieneEscudo());
    }

    @Test
    public void testRecibirDisparo_SinEscudo() {
        // Arrange
        NaveAmi naveAmi = new NaveAmi(0, 0, 20, 20, Estado.VIVO, Direccion.NOMOVER, "tex", 3, 1f, 5, 5, 10);

        // Act
        naveAmi.recibirDisparo();

        // Assert
        assertEquals(2, naveAmi.getVidas()); // Pierde vida
        verify(mockSound, atLeast(1)).play(0.7f);
    }

    @Test
    public void testGestionarMisDisparos() {
        // Arrange
        NaveAmi naveAmi = new NaveAmi(0, 0, 20, 20, Estado.VIVO, Direccion.NOMOVER, "tex", 3, 1f, 5, 5, 10);
        naveAmi.disparar();
        DisparoAmi d = naveAmi.getMisDisparos().get(0);
        float startY = d.getY();
        
        // Act - Mover disparo
        naveAmi.gestionarMisDisparos(1000f, 1f); // Limite alto para no desaparecer

        // Assert
        assertTrue(d.getY() > startY); // Se ha movido hacia arriba
        assertEquals(1, naveAmi.getMisDisparos().size());

        // Act - Disparo sale de la pantalla
        naveAmi.gestionarMisDisparos(-100f, 1f); // Limite bajo para forzar desaparecer (aunque en codigo es limiteSuperior)
        // Wait, desaparecer(limiteSuperior) checks if y > limiteSuperior
        naveAmi.gestionarMisDisparos(0f, 1f); // Si el limite es 0 y se movio a > 0, desaparece

        // Assert
        assertEquals(0, naveAmi.getMisDisparos().size());
    }
}
