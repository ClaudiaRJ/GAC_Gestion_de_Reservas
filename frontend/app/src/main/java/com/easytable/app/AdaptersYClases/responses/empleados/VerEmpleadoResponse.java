package com.easytable.app.AdaptersYClases.responses.empleados;
import com.google.gson.annotations.SerializedName;

public class VerEmpleadoResponse {
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("apellido")
    private String apellido;
    @SerializedName("telefono")
    private String telefono;
    @SerializedName("email")
    private String email;
    @SerializedName("rolUsuario")
    private String rolUsuario; // Puede ser un String o un Enum en Android si lo defines
    @SerializedName("estado")
    private String estado;

    // Constructor, getters y setters
    public VerEmpleadoResponse(String nombre, String apellido, String telefono, String email, String rolUsuario, String estado) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.rolUsuario = rolUsuario;
        this.estado = estado;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRolUsuario() { return rolUsuario; }
    public void setRolUsuario(String rolUsuario) { this.rolUsuario = rolUsuario; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getRol(String rolUsuario){return rolUsuario;}
}