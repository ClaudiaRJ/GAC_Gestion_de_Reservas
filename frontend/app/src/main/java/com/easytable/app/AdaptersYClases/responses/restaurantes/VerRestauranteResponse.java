package com.easytable.app.AdaptersYClases.responses.restaurantes;

import com.google.gson.annotations.SerializedName;

public class VerRestauranteResponse {
    @SerializedName("id")
    private Long id;
    @SerializedName("CIFRestaurante")
    private String CIFRestaurante;
    @SerializedName("nombre") // Asume que el backend lo mapea a 'nombre' en el JSON de respuesta
    private String nombreRestaurante;
    @SerializedName("descripcion") // Asume que el backend lo mapea a 'descripcion'
    private String descripcionRestaurante;
    @SerializedName("direccion")
    private String direccionRestaurante;
    @SerializedName("provincia")
    private String provinciaRestaurante;
    @SerializedName("ciudad")
    private String ciudadRestaurante;
    @SerializedName("telefono")
    private String telefonoRestaurante;
    @SerializedName("email")
    private String emailRestaurante;
    @SerializedName("emailGerente")
    private String emailGerenteResponsable;

    // Constructor, getters y setters (generados automáticamente en Android Studio o IntelliJ)
    // Clic derecho -> Generate -> Constructor, Getters, Setters

    public VerRestauranteResponse(Long id, String CIFRestaurante, String nombreRestaurante, String descripcionRestaurante, String direccionRestaurante, String provinciaRestaurante, String ciudadRestaurante, String telefonoRestaurante, String emailRestaurante, String emailGerenteResponsable) {
        this.id = id;
        this.CIFRestaurante = CIFRestaurante;
        this.nombreRestaurante = nombreRestaurante;
        this.descripcionRestaurante = descripcionRestaurante;
        this.direccionRestaurante = direccionRestaurante;
        this.provinciaRestaurante = provinciaRestaurante;
        this.ciudadRestaurante = ciudadRestaurante;
        this.telefonoRestaurante = telefonoRestaurante;
        this.emailRestaurante = emailRestaurante;
        this.emailGerenteResponsable = emailGerenteResponsable;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCIFRestaurante() { return CIFRestaurante; }
    public void setCIFRestaurante(String CIFRestaurante) { this.CIFRestaurante = CIFRestaurante; }
    public String getNombreRestaurante() { return nombreRestaurante; }
    public void setNombreRestaurante(String nombreRestaurante) { this.nombreRestaurante = nombreRestaurante; }
    public String getDescripcionRestaurante() { return descripcionRestaurante; }
    public void setDescripcionRestaurante(String descripcionRestaurante) { this.descripcionRestaurante = descripcionRestaurante; }
    public String getDireccionRestaurante() { return direccionRestaurante; }
    public void setDireccionRestaurante(String direccionRestaurante) { this.direccionRestaurante = direccionRestaurante; }
    public String getProvinciaRestaurante() { return provinciaRestaurante; }
    public void setProvinciaRestaurante(String provinciaRestaurante) { this.provinciaRestaurante = provinciaRestaurante; }
    public String getCiudadRestaurante() { return ciudadRestaurante; }
    public void setCiudadRestaurante(String ciudadRestaurante) { this.ciudadRestaurante = ciudadRestaurante; }
    public String getTelefonoRestaurante() { return telefonoRestaurante; }
    public void setTelefonoRestaurante(String telefonoRestaurante) { this.telefonoRestaurante = telefonoRestaurante; }
    public String getEmailRestaurante() { return emailRestaurante; }
    public void setEmailRestaurante(String emailRestaurante) { this.emailRestaurante = emailRestaurante; }
    public String getEmailGerenteResponsable() { return emailGerenteResponsable; }
    public void setEmailGerenteResponsable(String emailGerenteResponsable) { this.emailGerenteResponsable = emailGerenteResponsable; }
}