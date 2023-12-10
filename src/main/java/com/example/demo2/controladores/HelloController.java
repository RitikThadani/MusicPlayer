package com.example.demo2.controladores;

import com.example.demo2.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;

/**
 * Controlador para la pantalla de inicio de sesión del reproductor de música.
 * Simplemente habre los diferentes paneles : registro, cambiar contraseña y el reproductor
 */
public class HelloController{

    public TextField contraLogin;
    public TextField usuarioLogin;
    public Label fallo;
    @FXML
    Button boton;
    /**
     * Este String recoge el codigo del usuario al iniciar sesión para saber que usuario inicio sesión
     */
    public String codigo;

    @FXML
    AnchorPane panel;

    Connection con = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    /**
     * Abre el fxml de registrar usuario y cierra el actual
     */
    @FXML
    public void abrirRegistro(){


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("registro.fxml"));
        Stage nuevaVentana = new Stage();
        StackPane root = new StackPane();
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        nuevaVentana.setTitle("Nueva Ventana");
        nuevaVentana.setScene(scene);

        // Cerrar la ventana actual
        Stage stage = (Stage) panel.getScene().getWindow();
        stage.close();

        nuevaVentana.show();
    }

    /**
     * Abre el fxml de reproductor y cierra la vista actual, ademas le pasa el HelloController actual para acceder
     * a sus atributos como el codigo del usuario
     */
    @FXML
    public void abrirReproductor(){

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("reproductor.fxml"));
        Stage nuevaVentana = new Stage();
        StackPane root = new StackPane();
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        nuevaVentana.setTitle("Nueva Ventana");
        nuevaVentana.setScene(scene);

        //especificamos que el reproductor que se vaya a abrio tenga esta clase con el
        Reproductor reproductor = fxmlLoader.getController(); //obtenemos el controller del fxml
        reproductor.setHelloController(this); //ponemos la clase con dicho controller

        // Cerrar la ventana actual
        Stage stage = (Stage) panel.getScene().getWindow();
        stage.close();

        nuevaVentana.show();
    }
    /**
     * Abre el fxml de cambiarContraseña usuario y cierra el actual
     */
    @FXML
    public void abrirCambioContra(){

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("cambiarContra.fxml"));
        Stage nuevaVentana = new Stage();
        StackPane root = new StackPane();
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        nuevaVentana.setTitle("Nueva Ventana");
        nuevaVentana.setScene(scene);
        nuevaVentana.show();

    }


    /**
     * Metodo para el boton de iniciar sesion usado en la vista
     */
    public void onEnter(ActionEvent ae){
        iniciarSesion();
    }

    /**
     * Metodo usado para comprobar si el usuario y la contraseña del textField de la vista estan en la BD
     * Este metodo busca entre todos los usuarios alguno con dicho nombre y contraseña, si el select tiene alguna respuesta
     * se accede al metodo para abrir el reproductor
     */
    public void iniciarSesion() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/yritik?serverTimezone=UTC", "root", "");
            String us = usuarioLogin.getText();
            String contra = contraLogin.getText();
            String sql = "SELECT * FROM usuarios WHERE username=? AND contra=?";
            pst = con.prepareCall(sql);;
            pst.setString(1, us);
            pst.setString(2, contra);

            rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(null,
                        "Inicio de sesion correcto");
                codigo = (rs.getString("codigo"));
                abrirReproductor();

            } else {
                fallo.setText("Contraseña o Usuario incorrecto");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}