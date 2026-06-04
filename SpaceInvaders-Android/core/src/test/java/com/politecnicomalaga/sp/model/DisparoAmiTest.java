package com.politecnicomalaga.sp.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.politecnicomalaga.sp.util.RandomUtils;
import com.politecnicomalaga.sp.model.Ovni.Estado;
import com.politecnicomalaga.sp.model.Ovni.Direccion;

public class DisparoAmiTest {

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
            DisparoAmi disparo = new DisparoAmi(x, y, width, height, estado, dir, textura);

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

    @Test
    public void testDesaparecer() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            float y = RandomUtils.randomFloat();
            DisparoAmi disparo = new DisparoAmi(0, y, 10, 10, Estado.VIVO, Direccion.ARRIBA, "tex");
            
            // Act
            disparo.desaparecer(y - 10f); // limiteSuperior es menor que Y, por lo que sale

            // Assert
            assertEquals(Estado.MUERTO, disparo.getEstado());
        }
    }

    @Test
    public void testNoDesaparecer() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            float y = RandomUtils.randomFloat();
            DisparoAmi disparo = new DisparoAmi(0, y, 10, 10, Estado.VIVO, Direccion.ARRIBA, "tex");
            
            // Act
            disparo.desaparecer(y + 10f); // limiteSuperior es mayor que Y, por lo que no sale

            // Assert
            assertEquals(Estado.VIVO, disparo.getEstado());
        }
    }

    @Test
    public void testComprobarColision() {
        // Arrange
        DisparoAmi disparo = new DisparoAmi(10, 10, 5, 5, Estado.VIVO, Direccion.ARRIBA, "tex");
        
        NaveEne mockEnemigoVivo = mock(NaveEne.class);
        when(mockEnemigoVivo.estaVivo()).thenReturn(true);
        when(mockEnemigoVivo.getX()).thenReturn(10f);
        when(mockEnemigoVivo.getY()).thenReturn(10f);
        when(mockEnemigoVivo.getWidth()).thenReturn(10f);
        when(mockEnemigoVivo.getHeight()).thenReturn(10f);
        
        NaveEne mockEnemigoMuerto = mock(NaveEne.class);
        when(mockEnemigoMuerto.estaVivo()).thenReturn(false);

        NaveEne[] enemigos = { mockEnemigoMuerto, mockEnemigoVivo };

        // Act
        boolean result = disparo.comprobarColision(enemigos);

        // Assert
        assertTrue(result);
        assertEquals(Estado.MUERTO, disparo.getEstado());
        verify(mockEnemigoVivo, times(1)).recibirDisparo();
        verify(mockEnemigoMuerto, never()).recibirDisparo();
    }

    @Test
    public void testComprobarColision_CuandoBalaEstaMuerta() {
        // Arrange
        DisparoAmi disparo = new DisparoAmi(10, 10, 5, 5, Estado.MUERTO, Direccion.ARRIBA, "tex");
        NaveEne mockEnemigoVivo = mock(NaveEne.class);
        NaveEne[] enemigos = { mockEnemigoVivo };

        // Act
        boolean result = disparo.comprobarColision(enemigos);

        // Assert
        assertFalse(result);
        verify(mockEnemigoVivo, never()).recibirDisparo();
    }
}
