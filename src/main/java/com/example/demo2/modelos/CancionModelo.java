package com.example.demo2.modelos;

/**
 * Clase para crear canciones extraidas de la BD
 */
public class CancionModelo {

    private String nombre;
    private String duracion;
    private String linkCancion;
    private String artista;
    private String imagenUrl;
    private String codigo;

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public void setLinkCancion(String linkCancion) {
        this.linkCancion = linkCancion;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public void setCodigo(String codigo){this.codigo = codigo;}

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getArtista() {
        return artista;
    }

    public String getCodigo(){return codigo;}

    public String getNombre() {return nombre;}

    public String getImagenUrl() {
        return imagenUrl;
    }

    public String getLinkCancion() {
        return linkCancion;
    }

    public String getDuracion() {
        return duracion;
    }
}
