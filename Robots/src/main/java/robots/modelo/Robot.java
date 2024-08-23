package robots.modelo;

import robots.recursos.Ctes;

public class Robot extends ObjetoMovible {
    private int puntaje;
    private boolean destruido;

    public Robot(int posFil, int posCol, int pasos, int puntaje) {
        super(pasos, posFil, posCol);
        this.puntaje = puntaje;
        this.destruido = false;
    }

    public int getPuntaje() {
        return puntaje;
    }
    public boolean isDestruido() {
        return destruido;
    }

    /**
     * destruir destruye al robot impidiendole moverse y asignandole un puntaje de 0
     */
    public void destruir() {
        super.setPasos(Ctes.SIN_PASOS);
        puntaje = 0;
        destruido = true;
    }
}