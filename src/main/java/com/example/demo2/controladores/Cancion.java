package com.example.demo2.controladores;

import com.example.demo2.modelos.CancionModelo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Controlador para la pantalla de cancion, esta clase se usa para hacer una por cada cancion que se requiera en el
 * reproductor
 */
public class Cancion {

    @FXML
    private Label duracionLabel;

    @FXML
    private ImageView imagen;

    @FXML
    private Label nombreLabel;

    private String artista;

    public String urlCancionMP3;
    private String codigo;

    public static MediaPlayer mediaPlayer;

    /**
     * Referencia al controlador Reproductor para acceder a sus diferentes atributos
     */
    private Reproductor reproductor;
    public void setReproductor(Reproductor reproductor) {
        this.reproductor = reproductor;
    }


    /**
     * Metodo para establecer los elementos de la interfaz, a traves de la clase CancionModelo
     * @param cancion CancionModelo Clase usada para crear una base de lo que debe tener cada cancion
     */
    public void setDatos(CancionModelo cancion) {
        urlCancionMP3=cancion.getLinkCancion();
        nombreLabel.setText(cancion.getNombre());
        duracionLabel.setText(cancion.getDuracion());
        artista=cancion.getArtista();
        codigo = cancion.getCodigo();
    }

    /**
     * Metodo usado para un boton y reproducir la cancion, establece diferentes textos de la vista del reproductor con la cancion
     * actual y reproduce dicha cancion, si una ya esta reproduciendose la para y establece esta cancion
     */
    public void reproducirCancion() {
        calculoDeTiemp();
        reproductor.nombreCancionLabel.setText(nombreLabel.getText());
        reproductor.nombreCancionLabel1.setText(nombreLabel.getText());
        reproductor.artistaCancionLabel1.setText(artista);
        reproductor.artistaCancionLabel2.setText(artista);
        reproductor.duracionCancionLabel.setText(duracionLabel.getText());
        reproductor.duracion3.setText(duracionLabel.getText());
        reproductor.imagenCancion.setImage(imagen.getImage());

        Media media = new Media(new File(urlCancionMP3).toURI().toString());
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        reproductor.sliderPressed();

    }

    /**
     * Metodo usado para el boton de favoritos, añade la cancion a los favoritos del usuario actual
     */
    public void favoritos() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/yritik?serverTimezone=UTC", "root", "");
            Statement st = conn.createStatement();

            String sql = "INSERT INTO favs (codigoUsuario, codigoCancion) VALUES (?, ?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, reproductor.helloController.codigo);
            preparedStatement.setString(2, codigo);
            preparedStatement.executeUpdate();
            st.close();
            conn.close();
        } catch (Exception e) {}
    }

    /**
     * Metodo usado para el boton de quitar de favoritos, quita la cancion de la lista de favoritos del usuario actual
     */

    public void quitarfavs(){
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/yritik?serverTimezone=UTC", "root", "");
            Statement st = conn.createStatement();

            String sql = "DELETE FROM favs where codigoUsuario=? and codigoCancion=?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, reproductor.helloController.codigo);
            preparedStatement.setString(2, codigo);
            preparedStatement.executeUpdate();
            st.close();
            conn.close();
        } catch (Exception e) {}
    }


    /**
     * Metodo para actualizar el label de la duracion de las canciones a la duracion del archivo
     */
    public void calculoDeTiemp(){
        try {

            SimpleDateFormat timeInFormat = new SimpleDateFormat("ss", Locale.UK);
            SimpleDateFormat timeOutFormat = new SimpleDateFormat("mm:ss", Locale.UK);
            SimpleDateFormat timeOutOverAnHourFormat = new SimpleDateFormat("kk:mm:ss", Locale.UK);
            String duratin;

            File target = new File(urlCancionMP3);
            AudioFile f = AudioFileIO.read(target);
            AudioHeader ah = f.getAudioHeader();

            long trackLength = (long)ah.getTrackLength();

            Date timeIn;
            synchronized(timeInFormat) {
                timeIn = timeInFormat.parse(String.valueOf(trackLength));
            }
            if(trackLength < 3600L) {
                synchronized(timeOutFormat) {
                    duratin =  timeOutFormat.format(timeIn);
                }
            } else {
                synchronized(timeOutOverAnHourFormat) {
                    duratin = timeOutOverAnHourFormat.format(timeIn);
                }
            }

            duracionLabel.setText(duratin);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
