package com.util;

public class Sesion {
    private static int rolIdActual = 0;

    // Guardamos el ID del rol al iniciar sesión
    public static void iniciarSesion(int rolId) {
        rolIdActual = rolId;
    }

    public static void cerrarSesion() {
        rolIdActual = 0;
    }

    // Si el rol es 3 (Lector/Usuario normal), es de solo lectura
    public static boolean esSoloLectura() {
        return rolIdActual == 3;
    }
}