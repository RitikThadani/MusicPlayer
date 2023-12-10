package com.example.demo2.controladores;

import javafx.scene.control.TextField;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Controlador para la pantalla de cambiar contraseña.
 * Cambia la contraseña de un usuario
 */
public class CambiarContra {

    public TextField usuarioText;
    public TextField nuevaContra;
    Connection con = null;
    PreparedStatement pst = null;

    /**
     * Metodo usado para cambiar la contraseña del usuario en el caso de que este exista
     */
    public void cambiarContra() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/yritik?serverTimezone=UTC", "root", "");
            String us = usuarioText.getText();
            String contra = nuevaContra.getText();
            String sql = "UPDATE usuarios SET contra=? WHERE username=?;";
            pst = con.prepareCall(sql);;
            pst.setString(1, contra);
            pst.setString(2, us);
            int res = pst.executeUpdate();

            if (res>0){
                JOptionPane.showMessageDialog(null,
                        "Se cambio la contraseña");
            }else {
                JOptionPane.showMessageDialog(null,
                        "Fallo al cambiar la contraseña");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
