package com.easytable.app.MenuPrincipal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // Importar Log para depuración
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError; // Para manejar errores de autenticación si los hubiera
import com.android.volley.NetworkError;    // Para errores de red
import com.android.volley.NoConnectionError; // Para errores de conexión
import com.android.volley.ParseError;      // Para errores de parseo (JSON inválido)
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;     // Para errores del servidor (5xx)
import com.android.volley.TimeoutError;    // Para tiempos de espera agotados
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.easytable.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map; // Para añadir cabeceras si fueran necesarias

public class RecuperarContrasena extends AppCompatActivity {

    private EditText etEmail;
    private Button btnRecuperar;
    private TextView tvIniciarSesion;

    private static final String BASE_URL = "http://10.0.2.2:8080/api";
    private static final String RECOVER_PASSWORD_ENDPOINT = "/auth/recuperar-contrasena";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reiniciar_contrasena_activity);

        etEmail = findViewById(R.id.etEmail);
        btnRecuperar = findViewById(R.id.btnReiniciarContraseña);
        tvIniciarSesion = findViewById(R.id.tvIniciarSesion);

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recuperarContraseña();
            }
        });

        tvIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecuperarContrasena.this, IniciarSesion.class));
                finish();
            }
        });
    }

    private void recuperarContraseña() {
        String email = etEmail.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Por favor, ingresa un email válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = BASE_URL + RECOVER_PASSWORD_ENDPOINT;
        Log.d("RecuperarContraseña", "URL de recuperación: " + url);

        JSONObject datos = new JSONObject();
        try {
            datos.put("email", email);
            Log.d("RecuperarContraseña", "Cuerpo de la solicitud: " + datos.toString());
        } catch (JSONException e) {
            Log.e("RecuperarContraseña", "Error al construir JSON para recuperación: " + e.getMessage());
            Toast.makeText(this, "Error interno al preparar la solicitud.", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, datos,
                response -> {
                    // La respuesta exitosa generalmente indica que el correo fue enviado
                    Log.d("RecuperarContraseña", "Respuesta exitosa: " + response.toString());
                    Toast.makeText(this, "Si el email está registrado, recibirás un correo para restablecer la contraseña.", Toast.LENGTH_LONG).show();
                    // Opcional: Navegar a la pantalla de login después de un intento exitoso
                    startActivity(new Intent(RecuperarContrasena.this, IniciarSesion.class));
                    finish();
                },
                error -> {
                    Log.e("RecuperarContraseña", "Error de Volley: " + error.getMessage());
                    String errorMessage = "No se pudo enviar el correo.";

                    if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
                        errorMessage = "Error de conexión: No hay internet o el servidor no responde.";
                    } else if (error instanceof AuthFailureError) {
                        errorMessage = "Error de autenticación: Verifica tu permiso."; // Aunque no suele pasar en recuperación
                    } else if (error instanceof ServerError) {
                        // Aquí puedes intentar leer el cuerpo del error si el backend devuelve un mensaje JSON
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String errorData = new String(error.networkResponse.data);
                                JSONObject errorJson = new JSONObject(errorData);
                                if (errorJson.has("message")) { // O "error", "description", según tu backend
                                    errorMessage = errorJson.getString("message");
                                } else {
                                    errorMessage = "Error del servidor: " + error.networkResponse.statusCode;
                                }
                                Log.e("RecuperarContraseña", "Error del servidor (cuerpo): " + errorData);
                            } catch (JSONException e) {
                                Log.e("RecuperarContraseña", "Error parseando JSON de error del servidor: " + e.getMessage());
                                errorMessage = "Error del servidor.";
                            }
                        } else {
                            errorMessage = "Error del servidor.";
                        }
                    } else if (error instanceof ParseError) {
                        errorMessage = "Error al procesar la respuesta del servidor.";
                    }

                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}