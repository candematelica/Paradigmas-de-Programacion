package robots.vista;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import robots.modelo.JuegoModelo;
import robots.recursos.Ctes;
import robots.vista.eventos.ConfiguracionFilasColumnas;
import robots.vista.eventos.TeclaPresionadaEventHandler;

public class JuegoVista {
    private final JuegoModelo juegoModelo;
    private final VBox vista;
    private final Stage stage;

    public JuegoVista(JuegoModelo juegoModelo, Stage stage) {
        this.juegoModelo = juegoModelo;

        var labelInfoJuego = new Label("Nivel: %d Puntaje: %d".formatted(juegoModelo.getNivelActual(), juegoModelo.getJugador().getPuntaje()));
        var grilla = new Grilla(juegoModelo, this, stage);
        var vboxVentana = new VBox(labelInfoJuego, grilla.getGrilla(), inicializarBotones(grilla));
        vboxVentana.setAlignment(Pos.CENTER);
        vboxVentana.setOnKeyPressed(new TeclaPresionadaEventHandler(grilla));

        var confFilCol = new ConfiguracionFilasColumnas(juegoModelo.getTotalFilas(), juegoModelo.getTotalColumnas());
        confFilCol.preguntarConfFilasColumnas();
        grilla.reiniciarJuego(confFilCol.getTotalFilasActual(), confFilCol.getTotalColumnasActual());
        actualizarEtiquetas(grilla);

        this.vista = vboxVentana;
        this.stage = stage;
    }

    public void iniciar() {
        var scene = new Scene(vista);
        stage.setTitle("Robots");
        stage.setScene(scene);
        stage.show();
        vista.requestFocus();
    }

    /**
     * actualizarEtiquetas actualiza las etiquetas y botones de la vista.
     */
    public void actualizarEtiquetas(Grilla grilla) {
        if (vista != null) {
            var labelInfoJuego = new Label("Nivel: %d Puntaje: %d".formatted(juegoModelo.getNivelActual(), juegoModelo.getJugador().getPuntaje()));
            vista.getChildren().removeFirst();
            vista.getChildren().addFirst(labelInfoJuego);
            vista.getChildren().removeLast();
            vista.getChildren().addLast(inicializarBotones(grilla));
        }
    }

    /**
     * inicializarBotones inicializa los botones de la ventana.
     */
    private HBox inicializarBotones(Grilla grilla) {
        var botonTPAleatoria = new Button(Ctes.TITULO_BOTON_TP_ALEATORIO);
        var botonTPSegura = new Button(Ctes.TITULO_BOTON_TP_SEGURA + " (quedan: %d)".formatted(juegoModelo.getJugador().getUsosRestantesTPSegura()));
        var botonEsperarPorRobots = new Button(Ctes.TITULO_BOTON_ESPERAR_ROBOTS);

        // Configuramos los botones para que ocupen todo el ancho de la ventana
        HBox.setHgrow(botonTPAleatoria, Priority.ALWAYS);
        HBox.setHgrow(botonTPSegura, Priority.ALWAYS);
        HBox.setHgrow(botonEsperarPorRobots, Priority.ALWAYS);
        // Configuramos la expansión horizontal en los botones
        botonTPSegura.setMaxWidth(Double.MAX_VALUE);
        botonTPAleatoria.setMaxWidth(Double.MAX_VALUE);
        botonEsperarPorRobots.setMaxWidth(Double.MAX_VALUE);

        botonTPSegura.setOnAction(_ -> {
            if (juegoModelo.getJugador().getUsosRestantesTPSegura() > 0) {
                grilla.setEsSeleccionTPSegura(true);
                grilla.actualizarGrilla();
                actualizarEtiquetas(grilla);
            }
        });
        botonTPAleatoria.setOnAction(_ -> {
            grilla.getJuegoModelo().tpAleatoriaJugador();
            grilla.accionMoverJugador(juegoModelo.getJugador().getPosFil(), juegoModelo.getJugador().getPosCol());
            grilla.actualizarGrilla();
            actualizarEtiquetas(grilla);
        });
        botonEsperarPorRobots.setOnAction(_ -> {
            grilla.accionMoverJugador(juegoModelo.getJugador().getPosFil(), juegoModelo.getJugador().getPosCol());
            grilla.actualizarGrilla();
            actualizarEtiquetas(grilla);
        });

        HBox hboxBotones = new HBox(botonTPAleatoria, botonTPSegura, botonEsperarPorRobots);
        hboxBotones.setAlignment(Pos.CENTER);
        return hboxBotones;
    }
}
