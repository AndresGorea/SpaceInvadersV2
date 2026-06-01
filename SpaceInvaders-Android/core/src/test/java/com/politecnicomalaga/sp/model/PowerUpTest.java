package com.politecnicomalaga.sp.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.politecnicomalaga.sp.util.RandomUtils;
import com.politecnicomalaga.sp.model.Ovni.Estado;
import com.politecnicomalaga.sp.model.Ovni.Direccion;

public class PowerUpTest {

    private PowerUp.Tipo randomTipo() {
        PowerUp.Tipo[] tipos = PowerUp.Tipo.values();
        return tipos[RandomUtils.randomInt(tipos.length)];
    }

    @Test
    public void testConstructorGettersYSetters() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            float x = RandomUtils.randomFloat();
            float y = RandomUtils.randomFloat();
            float width = RandomUtils.randomFloatNo0();
            float height = RandomUtils.randomFloatNo0();
            PowerUp.Tipo tipo = randomTipo();
            String textura = RandomUtils.randomString(5, 15);

            // Act
            PowerUp powerUp = new PowerUp(x, y, width, height, tipo, textura);

            // Assert Getters
            assertNotNull(powerUp);
            assertEquals(x, powerUp.getX());
            assertEquals(y, powerUp.getY());
            assertEquals(width, powerUp.getWidth());
            assertEquals(height, powerUp.getHeight());
            assertEquals(Estado.VIVO, powerUp.getEstado());
            assertEquals(Direccion.ABAJO, powerUp.getDir());
            assertEquals(tipo, powerUp.getTipo());
            assertEquals(textura, powerUp.getTextura());

            // Arrange Setters (inherited)
            float newX = RandomUtils.randomFloat();
            float newY = RandomUtils.randomFloat();

            // Act Setters
            powerUp.setX(newX);
            powerUp.setY(newY);

            // Assert Setters
            assertEquals(newX, powerUp.getX());
            assertEquals(newY, powerUp.getY());
        }
    }

    @Test
    public void testDesaparecer() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            float y = RandomUtils.randomFloat();
            PowerUp powerUp = new PowerUp(0, y, 10, 10, randomTipo(), "tex");
            
            // Act
            powerUp.desaparecer(y + 10f); // limiteInferior es mayor que Y, por lo que sale

            // Assert
            assertEquals(Estado.MUERTO, powerUp.getEstado());
        }
    }

    @Test
    public void testNoDesaparecer() {
        for (int i = 0; i < 1000; i++) {
            // Arrange
            float y = RandomUtils.randomFloat();
            PowerUp powerUp = new PowerUp(0, y, 10, 10, randomTipo(), "tex");
            
            // Act
            powerUp.desaparecer(y - 10f); // limiteInferior es menor que Y, por lo que no sale

            // Assert
            assertEquals(Estado.VIVO, powerUp.getEstado());
        }
    }
}
