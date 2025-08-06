package com.easytable.app.AdaptersYClases.responses.usuarios;

import com.google.gson.annotations.SerializedName;

public class VerUsuarioResponse {

    @SerializedName("id")
    private Long id;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("apellido")
    private String apellido;
    @SerializedName("email")
    private String email;
    @SerializedName("telefono")
    private String telefono;
    @SerializedName("rolUsuario")
    private String rolUsuario; // Puede ser un String o un Enum en Android si lo defines
    @SerializedName("estado")
    private String estado; // Campo 'enabled' mapeado a 'estado' en la respuesta si el backend lo hace así

    /**
     * Constructor completo para la clase VerUsuarioResponse.
     *
     * @param id El ID único del usuario.
     * @param nombre El nombre del usuario.
     * @param apellido El apellido del usuario.
     * @param email El correo electrónico del usuario.
     * @param telefono El número de teléfono del usuario.
     * @param rolUsuario El rol del usuario (ej. "CLIENTE", "EMPLEADO", "GERENTE").
     * @param estado El estado de habilitación del usuario ("Activo" o "Desactivado").
     */
    public VerUsuarioResponse(Long id, String nombre, String apellido, String email, String telefono, String rolUsuario, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.rolUsuario = rolUsuario;
        this.estado = estado;
    }

    // Constructor vacío, necesario para que Gson pueda deserializar
    public VerUsuarioResponse() {
    }

    // --- Getters ---
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getRolUsuario() {
        return rolUsuario;
    }

    public String getEstado() {
        return estado;
    }

    // --- Setters ---
    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setRolUsuario(String rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "VerUsuarioResponse{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", rolUsuario='" + rolUsuario + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}