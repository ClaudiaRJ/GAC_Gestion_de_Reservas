// com.easytable.app.AdaptersYClases.Restaurante.java (Para ROOM Y API JSON)
package com.easytable.app.AdaptersYClases;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName; // Importar SerializedName
import java.io.Serializable;

@Entity(tableName = "Restaurante")
public class Restaurante implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public Long id;

    @SerializedName("nombre")
    public String nombreRestaurante;

    public String descripcion;
    public int imagenResId;

    public String restauranteReserva;


    public Restaurante() {
    }

    public Restaurante(String nombreRestaurante, String descripcion, int imagenResId, String restauranteReserva) {
        this.nombreRestaurante = nombreRestaurante;
        this.descripcion = descripcion;
        this.imagenResId = imagenResId;
        this.restauranteReserva = restauranteReserva != null ? restauranteReserva : "";
    }

    // Constructor sin el parámetro de reserva (restauranteReserva)
    public Restaurante(String nombreRestaurante, String descripcion, int imagenResId) {
        this(nombreRestaurante, descripcion, imagenResId, "");
    }

    // Métodos getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreRestaurante() { return nombreRestaurante; }
    public void setNombreRestaurante(String nombreRestaurante) { this.nombreRestaurante = nombreRestaurante; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getImagenResId() { return imagenResId; }
    public void setImagenResId(int imagenResId) { this.imagenResId = imagenResId; }
    public String getRestauranteReserva() { return restauranteReserva; }
    public void setRestauranteReserva(String restauranteReserva) { this.restauranteReserva = restauranteReserva; }

    public String getNombre() {
        return nombreRestaurante;
    }
}