package robots.vista;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import robots.modelo.JuegoModelo;
import robots.modelo.Robot;
import robots.recursos.Ctes;
import robots.vista.eventos.ConfiguracionFilasColumnas;

import java.util.Objects;

public class Grilla {
    private final GridPane grilla;
    private final JuegoModelo juegoModelo;
    private final JuegoVista juegoVista;
    private final Stage stage;
    private boolean esSeleccionTPSegura;

    public Grilla(JuegoModelo juegoModelo, JuegoVista juegoVista, Stage stage) {
        this.grilla = new GridPane();
        this.juegoModelo = juegoModelo;
        this.juegoVista = juegoVista;
        this.stage = stage;
        this.esSeleccionTPSegura = false;
        inicializarGrilla();
        actualizarGrilla();
        juegoVista.actualizarEtiquetas(this);
    }

    public GridPane getGrilla() {
        return grilla;
    }
    public JuegoModelo getJuegoModelo() {
        return juegoModelo;
    }

    public void setEsSeleccionTPSegura(boolean esSeleccionTPSegura) { this.esSeleccionTPSegura = esSeleccionTPSegura; }

    /**
     * reiniciarJuego reinicia el juego y la grilla en sus valores iniciales.
     */
    public void reiniciarJuego(int totalFilas, int totalColumnas) {
        juegoModelo.reiniciarJuego(totalFilas, totalColumnas);
        inicializarGrilla();
        actualizarGrilla();
    }

    /**
     * inicializarGrilla inicializa la grilla junto con sus celdas según los valores de filas
     * y columnas elegidos.
     */
    private void inicializarGrilla() {
        grilla.getChildren().clear();
        grilla.setVgap(0);
        grilla.setHgap(0);

        for (int i = 0; i < juegoModelo.getTotalFilas(); i++) {
            for (int j = 0; j < juegoModelo.getTotalColumnas(); j++) {
                Celda celda;
                if ((i % 2 == 0 && j % 2 == 0) || (i % 2 != 0 && j % 2 != 0)) {
                    celda = new Celda(i, j, Ctes.IMG_CELDA_OSCURA, this, juegoVista);
                } else {
                    celda = new Celda(i, j, Ctes.IMG_CELDA_CLARA, this, juegoVista);
                }
                var stackPane = new StackPane(celda.getVista());
                grilla.add(stackPane, j, i);
            }
        }
        grilla.setAlignment(Pos.CENTER);
        stage.sizeToScene();
    }

    /**
     * actualizarGrilla vacía la grilla y la actualiza según los valores actuales.
     */
    public void actualizarGrilla() {
        // Vacío la grilla
        for (Node nodo : grilla.getChildren()) {
            if (nodo instanceof StackPane stack) {
                if (stack.getChildren().size() > 1) {
                    stack.getChildren().remove(stack.getChildren().getLast());
                }
            }
        }

        // Lleno la grilla con la información actual
        for (Node nodo : grilla.getChildren()) {
            if (nodo instanceof StackPane stack) {
                if (juegoModelo.getJugador().getPosFil() == GridPane.getRowIndex(nodo) && juegoModelo.getJugador().getPosCol() == GridPane.getColumnIndex(nodo)) {
                    Image imgJugador;
                    if (juegoModelo.getJugador().getEstado().equals(Ctes.ESTADO_JUGADOR_PERDIO)) {
                        imgJugador = new Image(Objects.requireNonNull(getClass().getResourceAsStream(Ctes.IMG_JUGADOR_PERDEDOR)));
                    } else {
                        imgJugador = new Image(Objects.requireNonNull(getClass().getResourceAsStream(Ctes.IMG_JUGADOR)));
                    }
                    var imageViewJugador = new ImageView(imgJugador);
                    imageViewJugador.setOnMouseClicked(_ -> accionMoverJugador(GridPane.getRowIndex(nodo), GridPane.getColumnIndex(nodo)));
                    imageViewJugador.setFitHeight(20);
                    imageViewJugador.setPreserveRatio(true);
                    stack.getChildren().add(imageViewJugador);
                } else {
                    for (Robot robot : juegoModelo.getRobots()) {
                        if (robot.getPosFil() == GridPane.getRowIndex(nodo) && robot.getPosCol() == GridPane.getColumnIndex(nodo)) {
                            Image imgRobot = null;
                            if (robot.isDestruido()) {
                                imgRobot = new Image(Objects.requireNonNull(getClass().getResourceAsStream(Ctes.IMG_EXPLOSION)));
                            } else if (robot.getPasos() == Ctes.PASO_SIMPLE) {
                                imgRobot = new Image(Objects.requireNonNull(getClass().getResourceAsStream(Ctes.IMG_ROBOT_SIMPLE)));
                            } else if (robot.getPasos() == Ctes.PASO_DOBLE) {
                                imgRobot = new Image(Objects.requireNonNull(getClass().getResourceAsStream(Ctes.IMG_ROBOT_DOBLE)));
                            }
                            var imageViewRobot = new ImageView(imgRobot);
                            imageViewRobot.setOnMouseClicked(_ -> accionMoverJugador(GridPane.getRowIndex(nodo), GridPane.getColumnIndex(nodo)));
                            imageViewRobot.setFitHeight(20);
                            imageViewRobot.setPreserveRatio(true);
                            stack.getChildren().add(imageViewRobot);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * accionMoverJugador es llamado cada vez que se busca mover un jugador en la grilla, ya sea
     * con el mouse o el teclado.
     */
    public void accionMoverJugador(int posFil, int posCol) {
        if (esSeleccionTPSegura) {
            juegoModelo.getJugador().decrementarUsosRestantesTPSegura();
            juegoModelo.getJugador().actualizarPosicion(posFil, posCol);
            esSeleccionTPSegura = false;
        }
        juegoModelo.moverJugador(posFil, posCol);
        actualizarGrilla();
        juegoVista.actualizarEtiquetas(this);

        if (juegoModelo.getJugador().getEstado().equals(Ctes.ESTADO_JUGADOR_PERDIO)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Perdiste! Llegaste al nivel %d y tu puntaje fue de %d.".formatted(juegoModelo.getNivelActual(), juegoModelo.getJugador().getPuntaje()));
            alert.setContentText("¿Deseas continuar jugando?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    var confFilCol = new ConfiguracionFilasColumnas(juegoModelo.getTotalFilas(), juegoModelo.getTotalColumnas());
                    confFilCol.preguntarConfFilasColumnas();
                    reiniciarJuego(confFilCol.getTotalFilasActual(), confFilCol.getTotalColumnasActual());
                    juegoVista.actualizarEtiquetas(this);
                } else {
                    System.exit(0);
                }
            });
        }
        if (juegoModelo.getJugador().getEstado().equals(Ctes.ESTADO_JUGADOR_GANO)) {
            juegoModelo.ganarNivel();
            actualizarGrilla();
            juegoVista.actualizarEtiquetas(this);
        }
    }
}