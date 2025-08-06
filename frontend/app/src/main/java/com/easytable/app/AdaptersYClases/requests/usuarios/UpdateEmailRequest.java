// Archivo: UpdateEmailRequest.java
package com.easytable.app.AdaptersYClases.requests.usuarios;

import com.google.gson.annotations.SerializedName;

public class UpdateEmailRequest {

    @SerializedName("id")
    private Long id; // Asumo que el backend necesita el ID
    @SerializedName("email")
    private String email;

    public UpdateEmailRequest(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public UpdateEmailRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UpdateEmailRequest{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}