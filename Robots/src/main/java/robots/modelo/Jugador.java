package robots.modelo;

import robots.recursos.Ctes;

public class Jugador extends ObjetoMovible {
    private int usosRestantesTPSegura;
    private int puntaje;
    private String estado;

    public Jugador(int posFil, int posCol) {
        super(Ctes.PASO_SIMPLE, posFil, posCol);
        this.puntaje = 0;
        this.usosRestantesTPSegura = Ctes.CANT_INICIAL_TPSEGURA;
        this.estado = Ctes.ESTADO_JUGADOR_SIGUE;
    }

    public int getUsosRestantesTPSegura() {
        return usosRestantesTPSegura;
    }
    public int getPuntaje() {
        return puntaje;
    }
    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }

    public void reiniciarEstadoJugador(int posFil, int posCol) {
        super.setPosFil(posFil);
        super.setPosCol(posCol);
        this.puntaje = 0;
        this.usosRestantesTPSegura = Ctes.CANT_INICIAL_TPSEGURA;
        this.estado = Ctes.ESTADO_JUGADOR_SIGUE;
    }

    public void ganarNivel(int posFil, int posCol) {
        super.setPosFil(posFil);
        super.setPosCol(posCol);
        usosRestantesTPSegura++;
        this.estado = Ctes.ESTADO_JUGADOR_SIGUE;
    }

    /**
     * tp teletransporta al jugador a la posicion de fila y columna pasados por parametro.
     */
    public void actualizarPosicion(int posFil, int posCol) {
        super.setPosFil(posFil);
        super.setPosCol(posCol);
    }

    /**
     * aumentarPuntaje incrementa el puntaje del jugador en base a la cantidad de puntos ganados.
     */
    public void aumentarPuntaje(int puntos){
        this.puntaje += puntos;
    }

    /**
     * decrementarUsosRestantesTPSegura decrementa en uno la cantidad de usos restantes de la teletransportacion segura
     */
    public void decrementarUsosRestantesTPSegura(){
        this.usosRestantesTPSegura--;
    }
}
