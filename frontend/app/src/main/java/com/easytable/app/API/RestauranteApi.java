package com.easytable.app.API;
import java.io.Serializable;

public class RestauranteApi implements Serializable {
    // El campo JSON es "nombre", así que lo mapeamos directamente.
    // Si fuera "restaurantName" en JSON y "nombre" en Java, usarías @SerializedName("restaurantName")
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public RestauranteApi(String nombre) {}

    @Override
    public String toString() {
        return "RestauranteApi{" +
                "nombre='" + nombre + '\'' +
                '}';
    }
}