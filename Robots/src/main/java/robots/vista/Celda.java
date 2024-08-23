package robots.vista;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Objects;

public class Celda {
    private final ImageView vista;

    public Celda(int posFil, int posCol, String rutaImgCelda, Grilla grilla, JuegoVista juegoVista) {
        Image imgCelda = new Image(Objects.requireNonNull(getClass().getResourceAsStream(rutaImgCelda)));
        var imageViewCelda = new ImageView(imgCelda);
        imageViewCelda.setFitHeight(20);
        imageViewCelda.setFitWidth(20);

        imageViewCelda.setOnMouseClicked(_ -> grilla.accionMoverJugador(posFil, posCol));

        this.vista = imageViewCelda;
    }

    public ImageView getVista() {
        return vista;
    }
}
