package com.example.demo2.controladores;

import com.example.demo2.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * Controlador para la pantalla de registro.
 * Realiza comprobaciones de los campos del registro y al cumplir los requisitos se añade a la BD
 */
public class Registro implements Initializable {


    public TextField tfUsername;
    public TextField tfPhone;
    public TextField tfEmail;
    public TextField pfPassword;
    public TextField pfConfirmPassword;
    public TextField tfName;
    public String nombreCompleto;
    public String username;
    public String email;
    public String tlf1;
    public String contra;
    /**
     * Se realiza una Autoreferencia de la misma clase para crear una nueva y poner sus diferentes atributos
     * como el tlf,email,etc... sin afectar a la misma, y añadirla a la BD
     */
    public Registro user;
    @FXML
    AnchorPane panel;

    /**
     * Metodo que al iniciar la vista realiza un observable al TextField de telefono, y se asegura que cumpla ciertos requisitos
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Asegura que solo se ingresen números y si no es numerico entra dentrp
                tfPhone.setText(newValue.replaceAll("[^\\d]", "")); //y el valor nuevo(la letra), no aparecera
            }
            if (tfPhone.getText().length() > 9) { // Limita la longitud a 9 caracteres
                String limitedText = tfPhone.getText().substring(0, 9);
                tfPhone.setText(limitedText);
            }
        });
    }
    /**
     *Metodo para abrir la vista de Inicio (HelloController)
     */
    @FXML
    public void volverInicio(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
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
     * Metodo usado para comprobar si los campos del registro son adecuados para el mismo
     * (no hay espacios vacios, no existe ya en la BD,etc...),en el caso que no cumpla los requisitos con return; saldra
     * del metodo y aparecera un mensaje de error, si cumple los requisitos abre el metodo "addUserToDatabase" y pasa
     * los parametros necesarios para añadir el usuario a la BD
     */
    public void registerUser() {
        String nombreCompleto = tfName.getText();
        String email = tfEmail.getText();
        String tlf1 = tfPhone.getText();
        String username = tfUsername.getText();
        String contra = String.valueOf(pfPassword.getText());
        String confirmPassword = String.valueOf(pfConfirmPassword.getText());

        if (nombreCompleto.isEmpty() || email.isEmpty() || tlf1.isEmpty() || username.isEmpty() || contra.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Rellena todos los campos",
                    "Reintentalo",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!contra.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null,
                    "Las contraseñas no coinciden",
                    "Reintentalo",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }


        if(!tfEmail.getText().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
            JOptionPane.showMessageDialog(null,
                    "El Email no es correcto",
                    "Reintentalo",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!tfPhone.getText().matches("[0-9]{9}")){
            JOptionPane.showMessageDialog(null,
                    "El telefono no es correcto",
                    "Reintentalo",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/yritik?serverTimezone=UTC", "root", "");
            Statement stmt = conn.createStatement();

            String sql1 = "SELECT * FROM usuarios WHERE username='" + username + "'";
            ResultSet rs = stmt.executeQuery(sql1);

            if (rs.next()){
                JOptionPane.showMessageDialog(null,
                        "El nombre de usuario ya existe",
                        "Reintentalo",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/yritik?serverTimezone=UTC", "root", "");
            Statement stmt = conn.createStatement();

            String sql2 = "SELECT * FROM usuarios WHERE email='" + email + "'";
            ResultSet rs2 = stmt.executeQuery(sql2);

            if (rs2.next()){
                JOptionPane.showMessageDialog(null,
                        "El email ya esta registrado",
                        "Reintentalo",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        user = addUserToDatabase(nombreCompleto, email, tlf1, username, contra);

    }

    /**
     * Metodo usado para el boton de la interfaz de Registro para registrar un usuario
     */
    public void onEnter(ActionEvent ae){
        registerUser();
    }


    /**
     * Metodo usado para añadir un usuario a la BD
     * @param nombreCompleto
     * @param email
     * @param tlf1
     * @param username
     * @param contra
     * @return retorna el usuario que se ha añadido y esta presente en los atributos de esta clase
     */
    private Registro addUserToDatabase(String nombreCompleto, String email, String tlf1, String username, String contra) {
        Registro user = null;
        final String DB_URL = "jdbc:mysql://localhost/yritik?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO usuarios (nombreCompleto, email, tlf1, username, contra) " +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, nombreCompleto);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, tlf1);
            preparedStatement.setString(4, username);
            preparedStatement.setString(5, contra);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                user = new Registro();
                user.nombreCompleto = nombreCompleto;
                user.email = email;
                user.tlf1 = tlf1;
                user.username = username;
                user.contra = contra;
            }

            stmt.close();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        System.exit(0);
        return user;
    }

}
