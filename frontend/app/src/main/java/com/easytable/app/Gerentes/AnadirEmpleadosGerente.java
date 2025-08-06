package com.easytable.app.Gerentes;

import android.app.AlertDialog;
import android.content.Context; // Necesario para SharedPreferences
import android.content.Intent;
import android.content.SharedPreferences; // Para obtener el token
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log; // Para depuración
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.easytable.app.API.ApiService; // Importar tu ApiService
import com.easytable.app.API.RetrofitClient; // Importar tu RetrofitClient
import com.easytable.app.AdaptersYClases.requests.empleados.EmpleadoRequest; // ¡Importa la nueva clase EmpleadoRequest!
import com.easytable.app.R;

import java.io.IOException; // Necesario para manejar errorBody().string()

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnadirEmpleadosGerente extends AppCompatActivity {

    private static final String TAG = "AnadirEmpleadosGerente"; // Para logs

    private EditText etNombreEmpleado, etEmailEmpleado, etApellidoEmpleado, etTelefonoEmpleado;
    private Button btnGuardarEmpleado;
    private ImageButton imgVolver;

    private ApiService apiService;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anadir_empleado_activity);

        // --- 1. Inicialización de UI ---
        etNombreEmpleado = findViewById(R.id.etNombre);
        etEmailEmpleado = findViewById(R.id.etEmail);
        etApellidoEmpleado = findViewById(R.id.etApellido);
        etTelefonoEmpleado = findViewById(R.id.etTelefono);
        btnGuardarEmpleado = findViewById(R.id.btnAñadirEmpleado);
        imgVolver = findViewById(R.id.imgVolver);

        // --- 2. Inicialización de API y Autenticación ---
        apiService = RetrofitClient.getApiService();

        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
        authToken = prefs.getString("jwtToken", null);

        // Si no hay token, no podemos hacer la petición
        if (authToken == null || authToken.isEmpty()) {
            Toast.makeText(this, "Error de autenticación: Token no disponible. Por favor, inicie sesión.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Token JWT no encontrado en SharedPreferences.");
            finish(); // Cierra la actividad si no hay token
            return;
        }

        // --- 3. Configuración de Listeners ---
        btnGuardarEmpleado.setOnClickListener(v -> {
            registrarEmpleadoEnBackend(); // Llamada al nuevo método
        });

        imgVolver.setOnClickListener(v -> {
            // Regresar a la actividad anterior, que presumiblemente es MenuPrincipalGerente
            startActivity(new Intent(AnadirEmpleadosGerente.this, MenuPrincipalGerente.class));
            finish(); // Cierra esta actividad
        });
    }

    private void registrarEmpleadoEnBackend() {
        String nombre = etNombreEmpleado.getText().toString().trim();
        String email = etEmailEmpleado.getText().toString().trim();
        String apellido = etApellidoEmpleado.getText().toString().trim();
        String telefono = etTelefonoEmpleado.getText().toString().trim();

        // 1. Validar campos (la misma lógica que ya tienes)
        if (!validarCampos(nombre, email, apellido, telefono)) {
            return; // Si la validación falla, sale del método
        }

        // 2. Crear el objeto de request (EmpleadoRequest)
        EmpleadoRequest empleadoRequest = new EmpleadoRequest(nombre, apellido, email, telefono);
        Log.d(TAG, "Datos de empleado a enviar: " + empleadoRequest.toString());

        // 3. Añadir "Bearer " al token
        String fullAuthToken = "Bearer " + authToken;
        Log.d(TAG, "Token de autorización: " + fullAuthToken);


        // 4. Realizar la llamada a la API
        apiService.registrarEmpleado(fullAuthToken, empleadoRequest).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Éxito: Empleado registrado en el backend
                    Log.d(TAG, "Respuesta exitosa del backend: " + response.body());
                    new AlertDialog.Builder(AnadirEmpleadosGerente.this)
                            .setTitle("Éxito")
                            .setMessage("Empleado añadido correctamente al sistema.")
                            .setPositiveButton("OK", (dialog, which) -> {
                                limpiarCampos();
                                dialog.dismiss();
                                // Opcional: Navegar a la lista de empleados después de añadir uno
                                // startActivity(new Intent(AnadirEmpleadosGerente.this, ListarEmpleadosGerente.class));
                                // finish();
                            })
                            .setCancelable(false)
                            .show();
                } else {
                    // Error HTTP del servidor (ej. 400 Bad Request, 409 Conflict, 401 Unauthorized)
                    String errorBody = "Error desconocido.";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error al leer el cuerpo del error: " + e.getMessage());
                    }

                    Log.e(TAG, "Error al añadir empleado: Código " + response.code() + " - Mensaje: " + response.message() + " - Cuerpo del error: " + errorBody);

                    String userMessage;
                    if (response.code() == 400) {
                        userMessage = "Datos de empleado inválidos. Por favor, revisa la información.";
                        // Si tu backend devuelve mensajes de error específicos en el body, podrías parsearlos aquí
                        // Ejemplo: {"email": "Ya existe un empleado con este email"}
                    } else if (response.code() == 401 || response.code() == 403) {
                        userMessage = "No autorizado para añadir empleados. Por favor, inicie sesión de nuevo.";
                    } else if (response.code() == 409) { // Conflicto, ej. email ya registrado
                        userMessage = "Ya existe un empleado con este email. Usa uno diferente.";
                    } else {
                        userMessage = "Error del servidor: " + response.code() + ". Inténtalo de nuevo.";
                    }
                    Toast.makeText(AnadirEmpleadosGerente.this, userMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Fallo de red (ej. sin conexión a internet, servidor caído)
                Log.e(TAG, "Fallo de red al añadir empleado: " + t.getMessage(), t);
                Toast.makeText(AnadirEmpleadosGerente.this, "Error de conexión. Verifique su conexión a internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // El método validarCampos sigue siendo el mismo y es correcto
    private boolean validarCampos(String nombre, String email, String apellido, String telefono) {
        boolean valido = true;

        if (TextUtils.isEmpty(nombre)) {
            etNombreEmpleado.setError("Nombre requerido");
            valido = false;
        }

        if (TextUtils.isEmpty(email)) {
            etEmailEmpleado.setError("Email requerido");
            valido = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailEmpleado.setError("Email inválido");
            valido = false;
        }

        if (TextUtils.isEmpty(apellido)) {
            etApellidoEmpleado.setError("Apellido requerido");
            valido = false;
        }
        if (TextUtils.isEmpty(telefono)) {
            etTelefonoEmpleado.setError("Teléfono requerido");
            valido = false;
        } else if (!TextUtils.isDigitsOnly(telefono)) {
            etTelefonoEmpleado.setError("Teléfono solo puede contener números");
            valido = false;
        }

        if (!valido) {
            Toast.makeText(this, "Completa todos los campos correctamente.", Toast.LENGTH_SHORT).show();
        }

        return valido;
    }

    private void limpiarCampos() {
        etNombreEmpleado.setText("");
        etEmailEmpleado.setText("");
        etApellidoEmpleado.setText("");
        etTelefonoEmpleado.setText("");
    }
}