package com.easytable.app.Gerentes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.easytable.app.API.ApiService;
import com.easytable.app.API.RetrofitClient;
import com.easytable.app.AdaptersYClases.UsuariosGerentes;

import com.easytable.app.AdaptersYClases.requests.usuarios.UpdateUsuarioRequest;
import com.easytable.app.AdaptersYClases.responses.usuarios.VerUsuarioResponse;
import com.easytable.app.R;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilGerente extends AppCompatActivity {

    private ImageButton imgAtras, imgEditPfp;
    private EditText etNombre, etApellido, etEmail, etTelefono, etUsuario;
    private ImageView imgPfp;
    private Button btnGuardarCambios;
    private UsuariosGerentes usuarioGerente; // Puede que necesites cargar esto desde la API, no solo inicializarlo.
    // Veo que compruebas 'if (usuarioGerente != null)' pero no lo inicializas
    // con datos del usuario logueado en este onCreate.
    // Lo corregiremos al final si es necesario.
    private static final String TAG = "EditarPerfilGerente";
    private ApiService apiService;
    private String authToken;
    private String gerenteEmail;
    private Long gerenteId; // Necesitarás el ID del usuario para UpdateUsuarioRequest

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_perfil_activity);

        // Vincular vistas
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etEmail = findViewById(R.id.etEmail);
        etTelefono = findViewById(R.id.etTelefono);
        etUsuario = findViewById(R.id.etUsuario);

        imgAtras = findViewById(R.id.imgVolver);
        imgEditPfp = findViewById(R.id.imgEditPfp);
        imgPfp = findViewById(R.id.imgPfp);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);

        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
        authToken = prefs.getString("jwtToken", null);
        gerenteEmail = prefs.getString("userEmail", null); // Asumiendo que guardas el email del usuario logueado
        gerenteId = prefs.getLong("userId", -1L); // <--- CAMBIO: Obtener el ID del usuario. Es CRÍTICO para UpdateUsuarioRequest

        if (authToken == null || authToken.isEmpty() || gerenteEmail == null || gerenteEmail.isEmpty() || gerenteId == -1L) {
            Toast.makeText(this, "Error de autenticación o perfil incompleto. Inicie sesión de nuevo.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Token JWT, Email de gerente o ID no encontrado en SharedPreferences.");
            finish();
            return;
        }

        apiService = RetrofitClient.getApiService();

        // Cargar datos del perfil al iniciar la actividad
        cargarPerfilGerente(); // <-- Nuevo método para cargar datos

        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarPerfilGerenteEnBackend(); // <-- Nuevo método para actualizar
            }
        });

        // Listener para el botón de Atras
        imgAtras.setOnClickListener(v -> finish()); // Simplificado para solo volver atrás
    }

    private void cargarPerfilGerente() {
        String fullAuthToken = "Bearer " + authToken;
        apiService.getPerfilUsuario(fullAuthToken, gerenteEmail).enqueue(new Callback<VerUsuarioResponse>() {
            @Override
            public void onResponse(Call<VerUsuarioResponse> call, Response<VerUsuarioResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    VerUsuarioResponse perfil = response.body();
                    etNombre.setText(perfil.getNombre());
                    etApellido.setText(perfil.getApellido());
                    etEmail.setText(perfil.getEmail());
                    etTelefono.setText(perfil.getTelefono());
                    // etUsuario.setText(perfil.getUsuario()); // Asumo que "usuario" es el email o no existe en VerUsuarioResponse
                    // Si existe un campo de nombre de usuario separado, úsalo.
                    // Actualizar el ID del gerente si no lo tienes ya o si el backend lo envía actualizado
                    // gerenteId = perfil.getId(); // Si la respuesta incluye el ID

                    Log.d(TAG, "Perfil de gerente cargado exitosamente.");
                } else {
                    String errorBody = "Error desconocido.";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error al leer errorBody en cargarPerfilGerente: " + e.getMessage());
                    }
                    Log.e(TAG, "Error al cargar perfil de gerente: Código " + response.code() + " - Mensaje: " + response.message() + " - Cuerpo del error: " + errorBody);
                    Toast.makeText(EditarPerfilGerente.this, "No se pudo cargar el perfil del gerente. Código: " + response.code(), Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<VerUsuarioResponse> call, Throwable t) {
                Log.e(TAG, "Fallo de red al cargar perfil de gerente: " + t.getMessage(), t);
                Toast.makeText(EditarPerfilGerente.this, "Error de conexión al cargar perfil. Verifique su internet.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void actualizarPerfilGerenteEnBackend() {
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String email = etEmail.getText().toString().trim(); // El email si es editable
        String telefono = etTelefono.getText().toString().trim();
        String usuario = etUsuario.getText().toString().trim(); // El nombre de usuario si es editable

        // Validaciones de campos
        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || telefono.isEmpty()) { // Quité 'usuario' si no es un campo de envío directo
            Toast.makeText(EditarPerfilGerente.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email inválido");
            Toast.makeText(EditarPerfilGerente.this, "Por favor, ingresa un email válido.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isDigitsOnly(telefono)) {
            etTelefono.setError("Teléfono solo puede contener números");
            Toast.makeText(EditarPerfilGerente.this, "El teléfono solo puede contener números.", Toast.LENGTH_SHORT).show();
            return;
        }

        UpdateUsuarioRequest updateRequest = new UpdateUsuarioRequest(gerenteId, nombre, apellido, telefono); // CAMBIO: DTO y parámetros

        Log.d(TAG, "Datos de perfil a enviar: " + updateRequest.toString());

        String fullAuthToken = "Bearer " + authToken;

        // CAMBIO: Llamada al método renombrado en ApiService
        apiService.updateUsuario(fullAuthToken, updateRequest).enqueue(new Callback<VerUsuarioResponse>() {
            @Override
            public void onResponse(Call<VerUsuarioResponse> call, Response<VerUsuarioResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Perfil actualizado con éxito: " + response.body());
                    Toast.makeText(EditarPerfilGerente.this, "Perfil actualizado correctamente.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditarPerfilGerente.this, userMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VerUsuarioResponse> call, Throwable t) {
                Log.e(TAG, "Fallo de red al actualizar perfil: " + t.getMessage(), t);
                Toast.makeText(EditarPerfilGerente.this, "Error de conexión. Verifique su internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}