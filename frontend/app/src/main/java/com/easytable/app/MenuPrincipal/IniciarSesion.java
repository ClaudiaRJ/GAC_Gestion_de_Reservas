package com.easytable.app.MenuPrincipal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.easytable.app.API.ApiService;
import com.easytable.app.AdaptersYClases.requests.menuPrincipal.LoginRequest;
import com.easytable.app.AdaptersYClases.responses.menuPrincipal.LoginResponse;

import com.easytable.app.Gerentes.MenuPrincipalGerente;
import com.easytable.app.R;
import com.easytable.app.Usuarios.MenuPrincipalUsuarios;

import com.google.gson.Gson; // ¡NUEVA IMPORTACIÓN NECESARIA PARA DEPURACIÓN!

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class IniciarSesion extends AppCompatActivity {
    private static final String TAG = "IniciarSesion";
    private EditText etEmail, etPassword;
    private Button btnIniciarSesion;
    private TextView tvCrearCuenta,tvOlvidarContrasena;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.iniciar_sesion_activity);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etContraseña);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        tvCrearCuenta = findViewById(R.id.tvCrearCuenta);
        tvOlvidarContrasena = findViewById(R.id.tvRecuperarContrasena);


        setupRetrofit();
        Log.d(TAG, "onCreate: RetrofitClient y ApiService configurados.");


        btnIniciarSesion.setOnClickListener(v -> loginUser());

        tvCrearCuenta.setOnClickListener(v -> startActivity(new Intent(IniciarSesion.this, CrearCuenta.class)));
        tvOlvidarContrasena.setOnClickListener(v -> {
            // Aquí iniciarías una nueva actividad para manejar la recuperación de contraseña
            Intent intent = new Intent(IniciarSesion.this, RecuperarContrasena.class);
            startActivity(intent);
        });
    }

    private void setupRetrofit() {
        Log.d(TAG, "setupRetrofit: Iniciando configuración de Retrofit");
        okhttp3.logging.HttpLoggingInterceptor logging = new okhttp3.logging.HttpLoggingInterceptor();
        logging.setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY);

        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .client(client)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        Log.d(TAG, "setupRetrofit: Retrofit configurado correctamente");
    }

    private void loginUser() {
        Log.d(TAG, "loginUser: Iniciando proceso de login");
        final String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Log.e(TAG, "loginUser: Email o contraseña vacíos");
            Toast.makeText(this, "Por favor ingresa los datos", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest = new LoginRequest(email, password);
        Log.d(TAG, "loginUser: Realizando petición de login con email: " + email);

        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d(TAG, "onResponse: Respuesta recibida");
                if (response.isSuccessful() && response.body() != null) {
                    // *** LÍNEA CRÍTICA 1: LOG DE LA RESPUESTA RAW DE LA API ***
                    Log.d(TAG, "RAW API RESPONSE: " + new Gson().toJson(response.body()));

                    Log.d(TAG, "onResponse: Respuesta exitosa, LoginResponse: " + response.body().toString());
                    LoginResponse loginResponse = response.body();

                    String token = loginResponse.getToken();
                    String userEmail = email; // El email ingresado en el campo de texto
                    String userName = loginResponse.getName(); // El nombre de la respuesta de login

                    // *** LÓGICA CLAVE: Obtener el ID del objeto UsuarioDatos ***
                    Long userId = null;
                    if (loginResponse.getUsuarioDatos() != null) {
                        userId = loginResponse.getUsuarioDatos().getId();
                        // También puedes usar el email y nombre del UsuarioDatos si quieres asegurarte
                        // userEmail = loginResponse.getUsuarioDatos().getEmail();
                        // userName = loginResponse.getUsuarioDatos().getNombre();
                    }


                    if (token != null && !token.isEmpty()) {
                        SharedPreferences sharedPreferences = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("jwtToken", token);
                        editor.putString("userEmail", userEmail);
                        editor.putString("userName", userName);

                        if (userId != null) {
                            editor.putLong("clienteId", userId); // <-- Aquí se guarda el ID
                            Log.d(TAG, "GUARDANDO EN SHARED PREFS: clienteId = " + userId);
                        } else {
                            Log.e(TAG, "El ID de usuario recibido (dentro de UsuarioDatos) es nulo. NO SE PUEDE GUARDAR.");
                            Toast.makeText(IniciarSesion.this, "Error: ID de usuario no recibido del servidor.", Toast.LENGTH_LONG).show();
                        }

                        editor.apply();
                        Log.d(TAG, "Token, email, nombre y clienteId guardados en SharedPreferences.");
                    } else {
                        Log.e(TAG, "Token recibido es nulo o vacío después de login exitoso.");
                        Toast.makeText(IniciarSesion.this, "Error de autenticación: Token no válido.", Toast.LENGTH_SHORT).show();
                    }

                    handleResponse(loginResponse, userEmail);
                } else {
                    String errorMessage = "Error en la autenticación";
                    try {
                        if (response.errorBody() != null) {
                            errorMessage = response.errorBody().string();
                            Log.e(TAG, "onResponse: Mensaje de error del servidor: " + errorMessage);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: Error al leer errorBody: " + e.getMessage());
                    }
                    Log.e(TAG, "onResponse: Código de respuesta: " + response.code());
                    Toast.makeText(IniciarSesion.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: Error de conexión: " + t.getMessage());
                Toast.makeText(IniciarSesion.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleResponse(LoginResponse loginResponse, String emailIngresado) {
        Log.d(TAG, "handleResponse: Procesando respuesta para email: " + emailIngresado);
        String role = loginResponse.getRole();
        String nombreUsuario = loginResponse.getName();
        // Obtener el userId de UsuarioDatos
        Long userId = loginResponse.getUsuarioDatos() != null ? loginResponse.getUsuarioDatos().getId() : null;


        if (role != null) {
            Log.d(TAG, "handleResponse: Usuario autenticado, rol: " + role + ", nombre: " + nombreUsuario + ", ID: " + userId);

            if ("GERENTE".equals(role)) {
                Log.d(TAG, "handleResponse: Iniciando MenuPrincipalGerente");
                Intent intent = new Intent(IniciarSesion.this, MenuPrincipalGerente.class);
                intent.putExtra("nombGerente", nombreUsuario);
                intent.putExtra("emailGerente", emailIngresado);
                if (userId != null) { // Pasa el ID al gerente también
                    intent.putExtra("gerenteId", userId);
                }
                startActivity(intent);
            } else if ("CLIENTE".equals(role)) {
                Log.d(TAG, "handleResponse: Iniciando MenuPrincipalUsuarios");
                Intent intent = new Intent(IniciarSesion.this, MenuPrincipalUsuarios.class);
                intent.putExtra("nombUsuario", nombreUsuario);
                intent.putExtra("emailUsuario", emailIngresado);
                if (userId != null) { // Asegúrate de pasar el clienteId al usuario principal
                    intent.putExtra("clienteId", userId);
                }
                startActivity(intent);
            } else {
                Log.e(TAG, "handleResponse: Rol de usuario desconocido: " + role);
                Toast.makeText(this, "Rol de usuario desconocido", Toast.LENGTH_SHORT).show();
            }
            finish();
        } else {
            Log.e(TAG, "handleResponse: Error en la autenticación: Rol o Token nulo.");
            Toast.makeText(this, "Error en la autenticación o respuesta inesperada del servidor", Toast.LENGTH_SHORT).show();
        }
    }
}