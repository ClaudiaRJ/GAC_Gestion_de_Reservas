// Archivo: ConfirmationTokenRequest.java
package com.easytable.app.AdaptersYClases.requests;

import com.google.gson.annotations.SerializedName;

public class ConfirmationTokenRequest {

    @SerializedName("token")
    private String token;

    public ConfirmationTokenRequest(String token) {
        this.token = token;
    }

    public ConfirmationTokenRequest() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "ConfirmationTokenRequest{" +
                "token='" + token + '\'' +
                '}';
    }
}