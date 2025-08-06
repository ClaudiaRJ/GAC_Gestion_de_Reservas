// Archivo: UpdatePasswordRequest.java
package com.easytable.app.AdaptersYClases.requests.usuarios;

import com.google.gson.annotations.SerializedName;

public class UpdatePasswordRequest {

    @SerializedName("id")
    private Long id; // Asumo que el backend necesita el ID
    @SerializedName("newPassword")
    private String newPassword;

    public UpdatePasswordRequest(Long id, String newPassword) {
        this.id = id;
        this.newPassword = newPassword;
    }

    public UpdatePasswordRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "UpdatePasswordRequest{" +
                "id=" + id +
                ", newPassword='[PROTECTED]'" +
                '}';
    }
}