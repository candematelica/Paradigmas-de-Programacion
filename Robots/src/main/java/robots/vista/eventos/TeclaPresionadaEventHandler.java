package robots.vista.eventos;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import robots.vista.Grilla;

public class TeclaPresionadaEventHandler implements EventHandler<KeyEvent> {
    private final Grilla grilla;

    public TeclaPresionadaEventHandler(Grilla grilla) {
        this.grilla = grilla;
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        var teclaPresionada = keyEvent.getCode();
        boolean teclaValida = true;
        int posFil = grilla.getJuegoModelo().getJugador().getPosFil();
        int posCol = grilla.getJuegoModelo().getJugador().getPosCol();
        switch (teclaPresionada) {
            case W: // Arriba
                if (posFil > 0) { posFil--; }
                break;
            case X: // Abajo
                if (posFil < grilla.getJuegoModelo().getTotalFilas()-1) { posFil++; }
                break;
            case A: // Izquierda
                if (posCol > 0) { posCol--; }
                break;
            case D: // Derecha
                if (posCol < grilla.getJuegoModelo().getTotalColumnas()-1) { posCol++; }
                break;
            case S: // No moverse
                break;
            case Q: // Diagonal superior izquierda
                if (posFil > 0 && posCol > 0) {
                    posFil--;
                    posCol--;
                }
                break;
            case E: // Diagonal superior derecha
                if (posFil > 0 && posCol < grilla.getJuegoModelo().getTotalColumnas()-1) {
                    posFil--;
                    posCol++;
                }
                break;
            case Z: // Diagonal inferior izquierda
                if (posFil < grilla.getJuegoModelo().getTotalFilas()-1 && posCol > 0) {
                    posFil++;
                    posCol--;
                }
                break;
            case C: // Diagonal inferior derecha
                if (posFil < grilla.getJuegoModelo().getTotalFilas()-1 && posCol < grilla.getJuegoModelo().getTotalColumnas()-1) {
                    posFil++;
                    posCol++;
                }
                break;
            default:
                teclaValida = false;
                break;
        }

        if (teclaValida) {
            grilla.accionMoverJugador(posFil, posCol);
        }
    }
}
