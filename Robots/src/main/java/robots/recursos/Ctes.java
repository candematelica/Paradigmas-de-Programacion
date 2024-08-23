package robots.recursos;

import java.security.PublicKey;

public class Ctes {
    public static final int CANT_INICIAL_TPSEGURA = 1;
    public static final int NIVEL_INICIAL = 1;
    public static final int SIN_PASOS = 0;
    public static final int PASO_SIMPLE = 1;
    public static final int PASO_DOBLE = 2;
    public static final int PUNTAJE_ROBOT_SIMPLE = 10;
    public static final int PUNTAJE_ROBOT_DOBLE = 20;
    public static final int CANT_INICIAL_ROBOTS = 5;
    public static  final double AMPLIADOR_ROBOTS_POR_NIVEL = 1.5;
    public static final int TAM_POR_DEFECTO_FILA = 20;
    public static final int TAM_POR_DEFECTO_COLUMNA = 35;
    public static final int MENOR_TAM_GRILLA = 20;
    public static final int MAYOR_TAM_GRILLA = 50;

    public static final String ESTADO_JUGADOR_PERDIO = "Perdió";
    public static final String ESTADO_JUGADOR_SIGUE = "Sigue";
    public static final String ESTADO_JUGADOR_GANO = "Ganó";
    public static final String TITULO_BOTON_TP_ALEATORIO = "Teletransporte Aleatorio";
    public static final String TITULO_BOTON_TP_SEGURA = "Teletransporte Seguro";
    public static final String TITULO_BOTON_ESPERAR_ROBOTS = "Esperar por Robots";

    public static final String IMG_CELDA_OSCURA = "/imagenes/celda1.png";
    public static final String IMG_CELDA_CLARA = "/imagenes/celda2.png";
    public static final String IMG_EXPLOSION = "/imagenes/explosion.png";
    public static final String IMG_ROBOT_SIMPLE = "/imagenes/robot_mov_simple.gif";
    public static final String IMG_ROBOT_DOBLE = "/imagenes/robot_mov_doble.gif";
    public static final String IMG_JUGADOR = "/imagenes/jugador.gif";
    public static final String IMG_JUGADOR_PERDEDOR = "/imagenes/jugador_pierde.png";
}
