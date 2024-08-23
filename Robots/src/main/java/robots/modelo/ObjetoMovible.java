package robots.modelo;

public class ObjetoMovible {
    private int pasos;
    private int posFil;
    private int posCol;

    public ObjetoMovible(int pasos, int posFil, int posCol) {
        this.pasos = pasos;
        this.posFil = posFil;
        this.posCol = posCol;
    }

    public int getPasos() {
        return pasos;
    }

    public int getPosFil() {
        return posFil;
    }

    public int getPosCol() {
        return posCol;
    }

    void setPasos(int pasos) { this.pasos = pasos; }

    void setPosFil(int posFil) {
        this.posFil = posFil;
    }

    void setPosCol(int posCol) {
        this.posCol = posCol;
    }

    /**
     * Declaración de los métodos privados que utilizará el método público moverse(),
     * hay uno contemplado por cada movimiento posible.
     */
    private void moverseAbajo() { posFil += pasos; }
    private void moverseArriba() { posFil -= pasos; }
    private void moverseDerecha() { posCol += pasos; }
    private void moverseIzquierda() { posCol -= pasos; }
    private void moverseDiagonalSuperiorDerecha() {
        posFil -= pasos;
        posCol += pasos;
    }
    private void moverseDiagonalSuperiorIzquierda() {
        posFil -= pasos;
        posCol -= pasos;
    }
    private void moverseDiagonalInferiorDerecha() {
        posFil += pasos;
        posCol += pasos;
    }
    private void moverseDiagonalInferiorIzquierda() {
        posFil += pasos;
        posCol -= pasos;
    }

    /**
     * moverse recibe dos parámetros enteros dirFil y dirCol, que corresponden
     * a las direcciones en las que moverse, y se mueve actualizando las posiciones segun
     * la cantidad de pasos.
     */
    public void moverse(int dirFil, int dirCol) {
        if (dirFil < posFil) {
            if (dirCol < posCol){
                moverseDiagonalSuperiorIzquierda();
            } else if (dirCol > posCol) {
                moverseDiagonalSuperiorDerecha();
            } else {
                moverseArriba();
            }
        } else if (dirFil > posFil) {
            if (dirCol < posCol){
                moverseDiagonalInferiorIzquierda();
            } else if (dirCol > posCol) {
                moverseDiagonalInferiorDerecha();
            } else {
                moverseAbajo();
            }
        } else {
            if (dirCol < posCol){
                moverseIzquierda();
            } else if (dirCol > posCol) {
                moverseDerecha();
            }
        }
    }
}
