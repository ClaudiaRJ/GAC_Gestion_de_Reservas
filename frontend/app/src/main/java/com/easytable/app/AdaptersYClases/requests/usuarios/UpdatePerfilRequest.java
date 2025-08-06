package com.easytable.app.AdaptersYClases.requests.usuarios;

public class UpdatePerfilRequest {
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String usuario;

    // Constructor
    public UpdatePerfilRequest(String nombre, String apellido, String email, String telefono, String usuario) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.usuario = usuario;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido(){return  apellido;}
    public void setApellido(String apellido){this.apellido = apellido;}
    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}
    public String getTelefono(){return  telefono;}
    public void setTelefono(String telefono){this.telefono = telefono;}
    public String getUsuario(){return  usuario;}
    public void setUsuario(String usuario){this.usuario = usuario;}
}
