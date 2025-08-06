// app/src/main/java/com/easytable/app/MenuPrincipal/CrearCuenta.java
package com.easytable.app.MenuPrincipal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log; // Importar Log para depuración
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.easytable.app.API.ApiService;
import com.easytable.app.API.RetrofitClient;
import com.easytable.app.AdaptersYClases.requests.menuPrincipal.RegisterClientRequest;
import com.easytable.app.AdaptersYClases.responses.menuPrincipal.RegisterResponse;
import com.easytable.app.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class CrearCuenta extends AppCompatActivity {
    private static final String TAG = "CrearCuenta"; // Etiqueta para logs

    private EditText etEmail, etPassword, etNombre, etApellido, etTelefono; // ELIMINADO etUsuario
    private Button btnCrearCuenta;
    private ImageButton imgVolver;

    // --- ELIMINAR: Ya no se usa la base de datos local ---
    // private DataBaseSQLUsuarios dbSQL;

    private ApiService apiService; // Instancia de ApiService

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.crear_cuenta_activty);

        apiService = RetrofitClient.getApiService();
        Log.d(TAG, "onCreate: ApiService inicializado usando RetrofitClient");


        // Inicializando los campos de texto
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etContraseña);
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etTelefono = findViewById(R.id.etTelefono);
        // --- ELIMINAR: etUsuario si no se usa para enviar al backend ---
        // etUsuario = findViewById(R.id.etUsuario); // Quitar si no se envía al backend o mapear a otro campo


        // Inicializando los botones
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
        imgVolver = findViewById(R.id.imgVolver);

        // Estableciendo los listeners de los botones
        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });

        imgVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CrearCuenta.this, IniciarSesion.class));
            }
        });
    }

    public void signUpUser() {
        Log.d(TAG, "signUpUser: Iniciando proceso de registro de usuario");
        // Obteniendo los valores de los campos
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        // --- ELIMINAR: String usuario = etUsuario.getText().toString().trim(); ---


        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(nombre) ||
                TextUtils.isEmpty(apellido) || TextUtils.isEmpty(telefono)) { // ELIMINADO || TextUtils.isEmpty(usuario)
            Log.e(TAG, "signUpUser: Campos vacíos detectados");
            Toast.makeText(this, "Por favor ingresa todos los datos", Toast.LENGTH_LONG).show();
            return;
        }

        // --- MODIFICACIÓN: Realizar llamada a la API para registrar al usuario ---
        RegisterClientRequest registerRequest = new RegisterClientRequest(email, password, nombre, apellido, telefono);
        Log.d(TAG, "signUpUser: Enviando request de registro al backend para email: " + email);

        apiService.registerClient(registerRequest).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                Log.d(TAG, "onResponse: Respuesta recibida para registro");
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "onResponse: Registro exitoso: " + response.body().getMessage());
                    Toast.makeText(CrearCuenta.this, "Usuario registrado con éxito", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(CrearCuenta.this, IniciarSesion.class));
                    finish();
                } else {
                    // --- Manejo de errores más detallado ---
                    String errorMessage = "Error en el registro";
                    try {
                        if (response.errorBody() != null) {
                            errorMessage = response.errorBody().string();
                            Log.e(TAG, "onResponse: Mensaje de error del servidor: " + errorMessage);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: Error al leer errorBody: " + e.getMessage());
                    }
                    Log.e(TAG, "onResponse: Código de respuesta: " + response.code());
                    Toast.makeText(CrearCuenta.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: Error de conexión durante el registro: " + t.getMessage());
                Toast.makeText(CrearCuenta.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}