package com.easytable.app.Usuarios;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.easytable.app.API.ApiService;
import com.easytable.app.API.RetrofitClient;
import com.easytable.app.AdaptersYClases.requests.usuarios.UpdateUsuarioRequest;
import com.easytable.app.AdaptersYClases.responses.usuarios.VerUsuarioResponse;
import com.easytable.app.R;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilUsuario extends AppCompatActivity {

    private static final String TAG = "EditarPerfilUsuario";

    private ImageButton imgAtras, imgEditPfp;
    private EditText etNombre, etApellido, etEmail, etTelefono, etUsuario;
    private ImageView imgPfp;
    private Button btnGuardarCambios;

    private ApiService apiService;
    private String authToken;
    private String usuarioEmail; // Email del usuario logueado
    private Long usuarioId; // <--- CAMBIO: ID del usuario logueado

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_perfil_activity);

        // --- 1. Vincular vistas ---
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etEmail = findViewById(R.id.etEmail);
        etTelefono = findViewById(R.id.etTelefono);
        etUsuario = findViewById(R.id.etUsuario);

        imgAtras = findViewById(R.id.imgVolver);
        imgEditPfp = findViewById(R.id.imgEditPfp);
        imgPfp = findViewById(R.id.imgPfp);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);

        // --- 2. Inicialización de API y Autenticación ---
        apiService = RetrofitClient.getApiService();

        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
        authToken = prefs.getString("jwtToken", null);
        usuarioEmail = prefs.getString("userEmail", null);
        usuarioId = prefs.getLong("userId", -1L); // <--- CAMBIO: Obtener el ID del usuario

        if (authToken == null || authToken.isEmpty() || usuarioEmail == null || usuarioEmail.isEmpty() || usuarioId == -1L) {
            Toast.makeText(this, "Error de autenticación o perfil incompleto. Por favor, inicie sesión de nuevo.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Token JWT, Email de usuario o ID no encontrado en SharedPreferences.");
            finish();
            return;
        }

        // --- 3. Cargar datos del perfil desde la API al iniciar la actividad ---
        cargarPerfilUsuario();

        // --- 4. Configuración de Listeners ---
        btnGuardarCambios.setOnClickListener(v -> {
            actualizarPerfilUsuarioEnBackend();
        });

        imgAtras.setOnClickListener(v -> {
            startActivity(new Intent(EditarPerfilUsuario.this, AjustesPerfilUsuario.class));
            finish();
        });
    }

    private void cargarPerfilUsuario() {
        String fullAuthToken = "Bearer " + authToken;
        Log.d(TAG, "Cargando perfil para email: " + usuarioEmail + " con token: " + fullAuthToken);

        apiService.getPerfilUsuario(fullAuthToken, usuarioEmail).enqueue(new Callback<VerUsuarioResponse>() {
            @Override
            public void onResponse(Call<VerUsuarioResponse> call, Response<VerUsuarioResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    VerUsuarioResponse perfil = response.body();
                    Log.d(TAG, "Perfil cargado exitosamente: " + perfil.toString());
                    // Rellenar los campos de la UI con los datos del perfil
                    etNombre.setText(perfil.getNombre());
                    etApellido.setText(perfil.getApellido());
                    etEmail.setText(perfil.getEmail());
                    etTelefono.setText(perfil.getTelefono());
                    // etUsuario.setText(perfil.getUsuario()); // Asumo que "usuario" es el email o no existe en VerUsuarioResponse
                    // Si existe un campo de nombre de usuario separado, úsalo.

                    // Actualizar el ID del usuario si no lo tienes ya o si el backend lo envía actualizado
                    // usuarioId = perfil.getId(); // Si la respuesta incluye el ID
                } else {
                    String errorBody = "Error desconocido.";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error al leer errorBody en cargarPerfil: " + e.getMessage());
                    }
                    Log.e(TAG, "Error al cargar perfil: Código " + response.code() + " - Mensaje: " + response.message() + " - Cuerpo del error: " + errorBody);
                    Toast.makeText(EditarPerfilUsuario.this, "No se pudo cargar el perfil. Código: " + response.code(), Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<VerUsuarioResponse> call, Throwable t) {
                Log.e(TAG, "Fallo de red al cargar perfil: " + t.getMessage(), t);
                Toast.makeText(EditarPerfilUsuario.this, "Error de conexión al cargar perfil. Verifique su internet.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    // --- Nuevo método para actualizar el perfil en la API ---
    private void actualizarPerfilUsuarioEnBackend() {
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String usuario = etUsuario.getText().toString().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || telefono.isEmpty()) { // Quité 'usuario' si no es un campo directo
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email inválido");
            Toast.makeText(this, "Por favor, ingresa un email válido.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isDigitsOnly(telefono)) {
            etTelefono.setError("Teléfono solo puede contener números");
            Toast.makeText(this, "El teléfono solo puede contener números.", Toast.LENGTH_SHORT).show();
            return;
        }

        UpdateUsuarioRequest updateRequest = new UpdateUsuarioRequest(usuarioId, nombre, apellido, telefono); // CAMBIO: DTO y parámetros
        Log.d(TAG, "Datos de perfil a enviar para actualización: " + updateRequest.toString());

        String fullAuthToken = "Bearer " + authToken;

        // 3. Realizar la llamada a la API
        // CAMBIO: Llamada al método renombrado en ApiService
        apiService.updateUsuario(fullAuthToken, updateRequest).enqueue(new Callback<VerUsuarioResponse>() {
            @Override
            public void onResponse(Call<VerUsuarioResponse> call, Response<VerUsuarioResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Perfil actualizado con éxito: " + response.body());
                    Toast.makeText(EditarPerfilUsuario.this, "Perfil actualizado correctamente.", Toast.LENGTH_SHORT).show();
                    // Opcional: Actualizar SharedPreferences si el email o nombre de usuario ha cambiado y lo guardas ahí
                    // SharedPreferences.Editor editor = prefs.edit();
                    // editor.putString("userEmail", email);
                    // editor.putString("userName", nombre); // O el campo que uses para el nombre de usuario
                    // editor.apply();

                    finish(); // Volver a la pantalla anterior
                } else {
                    String errorBody = "Error desconocido.";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error al leer errorBody: " + e.getMessage());
                    }
                    Log.e(TAG, "Error al actualizar perfil: Código " + response.code() + " - Mensaje: " + response.message() + " - Cuerpo del error: " + errorBody);

                    String userMessage = "Error al actualizar perfil. Código: " + response.code();
                    if (response.code() == 400) {
                        userMessage = "Datos inválidos. Por favor, revisa la información.";
                    } else if (response.code() == 401 || response.code() == 403) {
                        userMessage = "No autorizado para actualizar perfil. Inicie sesión de nuevo.";
                    } else if (response.code() == 409) {
                        userMessage = "El email o nombre de usuario ya está en uso.";
                    }
                    Toast.makeText(EditarPerfilUsuario.this, userMessage, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<VerUsuarioResponse> call, Throwable t) {
                Log.e(TAG, "Fallo de red al actualizar perfil: " + t.getMessage(), t);
                Toast.makeText(EditarPerfilUsuario.this, "Error de conexión. Verifique su internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}