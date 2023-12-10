package com.example.demo2.controladores;

import com.example.demo2.HelloApplication;
import com.example.demo2.modelos.CancionModelo;
import javafx.beans.binding.Bindings;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import static com.example.demo2.controladores.Cancion.mediaPlayer;


/**
 *  Controlador para la pantalla de reproductor, accede a la BD para mostrar canciones y reproducirlas, pudiendo parar
 *  y reanudar la misma, ademas de añadir canciones favoritas de la lista total de canciones a la de favoritas del usuario
 */
public class Reproductor implements Initializable {

    public Label pruebas;
    public Label duracion3;

    @FXML
    Button boton;
    @FXML
    private VBox cancionesLayout;
    @FXML
    Label nombreCancionLabel;
    @FXML
    Label nombreCancionLabel1;
    @FXML
    Label artistaCancionLabel1;
    @FXML
    Slider songSlider;
    @FXML
    Label artistaCancionLabel2;
    @FXML
    Label duracionCancionLabel;
    @FXML
    ImageView imagenCancion;
    public HelloController helloController;


    /**
     * Metodo para establecer el helloController anterior con el cual se inicio sesión
     * @param helloController el controllador de la vista anterior
     */
    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }


    /**
     *Al boton se le añade un estilo css del archivo styleReproductor.css
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boton.getStyleClass().add("boton-imagen");
        boton.getStyleClass().add("pausa"); // Clase inicial para la imagen de pausa
        todasLasCanciones();
    }

    /**
     * Este metodo se usa para que dentro del VBox de la interfaz del reproductor se añadan tantas canciones
     * como tenga la lista, se crea por cada CancionModelo su propia vista Cancion con su controllador y establece
     * los datos de la CancionModelo en el controllador para seguidamente mostrarlo
     * @param canciones Una lista de canciones de la clase de CancionModelo
     */
    public void mostrarCanciones(List<CancionModelo> canciones) {
        //para la lista de canciones

        for (int i = 0; i < canciones.size(); i++) {
            //FXMLLoader fxmlLoader=new FXMLLoader();
            //fxmlLoader.setLocation(getClass().getResource("src/main/resources/com/example/demo2/cancion.fxml"));

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("cancion.fxml"));

            try {
                HBox hBox = fxmlLoader.load();
                Cancion controllerCancion = fxmlLoader.getController();

                controllerCancion.setReproductor(this); //para poder cambiar labels de Reproductor desde su controlador
                controllerCancion.setDatos(canciones.get(i));
                cancionesLayout.getChildren().add(hBox);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Metodo para mostrar todas las canciones de la BD, limpiando primero el VBox
     */
    public void todasLasCanciones() {
        cancionesLayout.getChildren().clear();
        mostrarCanciones(cancionesCrearLista());
    }

    /**
     * Metodo para mostrar todas las canciones favoritas del usuario de la BD, limpiando primero el VBox
     */
    public void cancionesFavoritas() {
        cancionesLayout.getChildren().clear();
        mostrarCanciones(cancionesFavoritasCrearLista());
    }

    /**
     * Metodo que crea una Lista de canciones favoritas del usuario añadiendo clases de CancionModelo tantas canciones hayan
     * en la BD, accediendo a la BD y realizando una consulta a la misma
     * @return Retorna una lista de clases CancionModelo de las canciones favoritas del usuario
     */
    private List<CancionModelo> cancionesFavoritasCrearLista() {
        List<CancionModelo> ls = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/yritik?serverTimezone=UTC", "root", "");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from canciones join favs on canciones.codigo=favs.codigoCancion where favs.codigoUsuario="+helloController.codigo);

            while (rs.next()) {
                CancionModelo cancion = new CancionModelo();
                cancion.setNombre(rs.getString("nombre"));
                cancion.setArtista(rs.getString("artista"));
                cancion.setDuracion(rs.getString("duracion"));
                cancion.setLinkCancion(rs.getString("link"));
                ls.add(cancion);
            }
        } catch (Exception e) {
        }
        return ls;
    }
    /**
     * Metodo que crea una Lista de todas las canciones de la BD, añadiendo clases de CancionModelo tantas canciones hayan
     * en la BD, accediendo a la BD y realizando una consulta a la misma
     * @return Retorna una lista de clases CancionModelo de todas las canciones de la BD
     */
    private List<CancionModelo> cancionesCrearLista() {
        List<CancionModelo> ls = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/yritik?serverTimezone=UTC", "root", "");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM `canciones` WHERE 1");
            while (rs.next()) {
                CancionModelo cancion = new CancionModelo();
                cancion.setCodigo(rs.getString("codigo"));
                cancion.setNombre(rs.getString("nombre"));
                cancion.setArtista(rs.getString("artista"));
                cancion.setDuracion(rs.getString("duracion"));
                cancion.setLinkCancion(rs.getString("link"));
                ls.add(cancion);
            }
        } catch (Exception e) {
        }
        return ls;
    }

    /**
     * Metodo usuado por un boton para parar la cancion o reanudarla en el caso de que ya se este reproduciendo una,
     * cambiando ademas el estilo del boton y removiendo el que ya tenia
     */
    @FXML
    public void cancionEstado() {
        if (mediaPlayer != null) {
            if (boton.getStyleClass().contains("pausa")) {
                boton.getStyleClass().remove("pausa");
                boton.getStyleClass().add("tocar");
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.pause();
                }

            } else {
                boton.getStyleClass().remove("tocar");
                boton.getStyleClass().add("pausa");

                if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
                    mediaPlayer.play();
                }
            }
        }
    }

    /**
     * Metodo para intentar hacer un slider que siga la Cancion
     */
    public void sliderPressed() {
            mediaPlayer.seek(Duration.seconds(songSlider.getValue()));
            mediaPlayer.currentTimeProperty().addListener(((observableValue, oldValue, newValue) -> {
                songSlider.setValue(newValue.toSeconds());
                double slidervalor;
                slidervalor = mediaPlayer.getTotalDuration().toSeconds();
                songSlider.setMax(slidervalor);
            }));

            pruebas.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return getTime(mediaPlayer.getCurrentTime());
                }}, mediaPlayer.currentTimeProperty()));
    }

    /**
     * Metodo usado para el slider que devuelva el tiempo correctamente
     * @param time
     * @return
     */
    public String getTime(Duration time){
        int horas = (int) time.toHours();
        int minutos = (int) time.toMinutes();
        int segs = (int) time.toSeconds();

        if (segs > 59) segs = segs % 60;
        if (minutos > 59) minutos = minutos % 60;
        if (horas > 59) horas = horas % 60;

        if (horas > 0) return String.format("%d:%02d:%02d",
                horas,
                minutos,
                segs);
        else return String.format("%02d:%02d",
                minutos,
                segs);
    }
}
