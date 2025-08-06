package com.easytable.app.Gerentes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.easytable.app.API.ApiService;
import com.easytable.app.API.RetrofitClient;
//import com.easytable.app.AdaptersYClases.requests.empleados.EliminarEmpleadoRequest; // Ya no se usa
import com.easytable.app.R;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EliminarEmpleadoGerente extends AppCompatActivity {

    private static final String TAG = "EliminarEmpleadoGerente";

    private EditText etEmailEliminar;
    private Button btnEliminarEmpleado;
    private ImageButton imgVolver;

    private ApiService apiService;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eliminar_empleado);

        etEmailEliminar = findViewById(R.id.etEmail);
        btnEliminarEmpleado = findViewById(R.id.btnEliminarEmpleado);
        imgVolver = findViewById(R.id.imgVolver);

        apiService = RetrofitClient.getApiService();

        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
        authToken = prefs.getString("jwtToken", null);

        if (authToken == null || authToken.isEmpty()) {
            Toast.makeText(this, "Error de autenticación: Token no disponible. Por favor, inicie sesión.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Token JWT no encontrado en SharedPreferences.");
            finish();
            return;
        }

        // --- 3. Configuración de Listeners ---
        btnEliminarEmpleado.setOnClickListener(v -> {
            String email = etEmailEliminar.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Introduce el email del empleado a desactivar", Toast.LENGTH_SHORT).show();
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmailEliminar.setError("Email inválido");
                Toast.makeText(this, "Por favor, ingresa un email válido.", Toast.LENGTH_SHORT).show();
            } else {
                desactivarEmpleadoEnBackend(email);
            }
        });

        imgVolver.setOnClickListener(v -> {
            startActivity(new Intent(EliminarEmpleadoGerente.this, GestionEmpleadosGerente.class));
            finish();
        });
    }

    /**
     * Envía la petición para desactivar un empleado al backend (por email en la URL).
     * @param email El email del empleado a desactivar.
     */
    private void desactivarEmpleadoEnBackend(String email) { // CAMBIO de nombre del método
        String fullAuthToken = "Bearer " + authToken;

        Log.d(TAG, "Enviando petición para desactivar empleado: " + email + " con token: " + fullAuthToken);

        // CAMBIO: Llamada al método correcto en ApiService
        apiService.desactivarEmpleado(fullAuthToken, email).enqueue(new Callback<String>() { // Usar el método que recibe email en @Path
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Empleado desactivado correctamente en el backend. Respuesta: " + response.body());
                    Toast.makeText(EliminarEmpleadoGerente.this, "Empleado desactivado correctamente", Toast.LENGTH_SHORT).show();
                    etEmailEliminar.setText(""); // Limpiar campo
                } else {
                    String errorBody = "Error desconocido.";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error al leer errorBody: " + e.getMessage());
                    }
                    Log.e(TAG, "Error al desactivar empleado: Código " + response.code() + " - Mensaje: " + response.message() + " - Cuerpo: " + errorBody);

                    String userMessage;
                    if (response.code() == 404) {
                        userMessage = "Empleado no encontrado. Verifique el email.";
                    } else if (response.code() == 401 || response.code() == 403) {
                        userMessage = "No autorizado para desactivar empleados. Inicie sesión de nuevo.";
                    } else if (response.code() == 400) {
                        userMessage = "Petición inválida. Revise el email.";
                    }
                    else {
                        userMessage = "Error al desactivar empleado: " + response.code();
                    }
                    Toast.makeText(EliminarEmpleadoGerente.this, userMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Fallo de red al desactivar empleado: " + t.getMessage(), t);
                Toast.makeText(EliminarEmpleadoGerente.this, "Error de conexión. Verifique su internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}