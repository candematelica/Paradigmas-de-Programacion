package robots.vista.eventos;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import robots.recursos.Ctes;

public class ConfiguracionFilasColumnas {
    private int totalFilasActual;
    private int totalColumnasActual;

    public ConfiguracionFilasColumnas(int totalFilasActual, int totalColumnasActual) {
        this.totalFilasActual = totalFilasActual;
        this.totalColumnasActual = totalColumnasActual;
    }

    public int getTotalFilasActual() { return totalFilasActual; }
    public int getTotalColumnasActual() { return totalColumnasActual; }

    /**
     * preguntarConfFilasColumnas lanza una ventana de tipo Alert que pregunta por la configuración deseada
     * de filas y columnas.
     */
    public void preguntarConfFilasColumnas() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Configuración de filas y columnas");
        alert.setHeaderText("Selección actual: %d filas y %d columnas\n".formatted(totalFilasActual, totalColumnasActual));

        var labelInfo = new Label("Escribí a continuación tu nueva selección: \n" +
                "Filas y columnas mayores o iguales a %d y menores o iguales a %d.".formatted(Ctes.MENOR_TAM_GRILLA, Ctes.MAYOR_TAM_GRILLA));

        var labelFilas = new Label("Filas");
        TextField textFieldFilas = new TextField();
        var labelColumnas = new Label("Columnas");
        TextField textFieldColumnas = new TextField();
        var hboxPreguntarFilasColumnas = new HBox(labelFilas, textFieldFilas, labelColumnas, textFieldColumnas);
        hboxPreguntarFilasColumnas.setSpacing(5);

        var vboxDialogPane = new VBox(labelInfo, hboxPreguntarFilasColumnas);
        vboxDialogPane.setSpacing(5);
        alert.getDialogPane().setContent(vboxDialogPane);

        ButtonType botonListo = new ButtonType("Listo");
        alert.getButtonTypes().setAll(botonListo);

        alert.showAndWait().ifPresent(response -> {
            if (response == botonListo) {
                int filas = 0;
                int columnas = 0;
                try { // Si no se mandó un número, se cierra el juego
                    filas = Integer.parseInt(textFieldFilas.getText());
                    columnas = Integer.parseInt(textFieldColumnas.getText());
                } catch (NumberFormatException e) {
                    System.exit(0);
                }
                if (filas >= Ctes.MENOR_TAM_GRILLA && filas <= Ctes.MAYOR_TAM_GRILLA && columnas >= Ctes.MENOR_TAM_GRILLA && columnas <= Ctes.MAYOR_TAM_GRILLA) {
                    totalFilasActual = filas;
                    totalColumnasActual = columnas;
                } else { // Si no se mando una cantidad de filas y columnas válida, se cierra el juego.
                    System.exit(0);
                }
            } else {
                System.exit(0);
            }
        });
    }
}