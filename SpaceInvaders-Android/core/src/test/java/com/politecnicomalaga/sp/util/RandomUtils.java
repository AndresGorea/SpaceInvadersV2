package com.politecnicomalaga.sp.util;

import com.politecnicomalaga.sp.model.Ovni.Direccion;
import com.politecnicomalaga.sp.model.Ovni.Estado;
import java.util.Random;

public class RandomUtils {

    private static final Random r = new Random();

    public static char randomChar() {
        return (char) (r.nextInt(256));
    }

    public static double randomDoubleNo0() {
        double random;
        do {
            random = r.nextDouble() * 1000;
        } while (random == 0.0);
        return random;
    }

    public static float randomFloat() {
        return r.nextFloat() * 1000f;
    }

    public static float randomFloatNo0() {
        float random;
        do {
            random = r.nextFloat() * 1000f;
        } while (random == 0.0f);
        return random;
    }

    public static int randomInt(int bound) {
        return r.nextInt(bound);
    }
    
    public static int randomInt(int min, int max) {
        return r.nextInt((max - min) + 1) + min;
    }

    public static String randomString(int minLength, int maxLength) {
        int length = r.nextInt(maxLength - minLength + 1) + minLength;
        StringBuilder buildText = new StringBuilder();
        for (int i = 0; i < length; i++) {
            buildText.append(randomChar());
        }
        return buildText.toString();
    }

    public static Estado randomEstado() {
        Estado[] estados = Estado.values();
        return estados[r.nextInt(estados.length)];
    }

    public static Direccion randomDireccion() {
        Direccion[] direcciones = Direccion.values();
        return direcciones[r.nextInt(direcciones.length)];
    }

    public static boolean randomBoolean() {
        return r.nextBoolean();
    }
}
