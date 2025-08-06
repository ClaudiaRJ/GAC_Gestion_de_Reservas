package com.easytable.app.Gerentes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easytable.app.API.ApiService;
import com.easytable.app.API.RetrofitClient;
import com.easytable.app.AdaptersYClases.Empleado;
import com.easytable.app.AdaptersYClases.EmpleadoAdapter;
import com.easytable.app.AdaptersYClases.responses.empleados.VerEmpleadoResponse;
import com.easytable.app.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaEmpleadosGerente extends AppCompatActivity {

    private static final String TAG = "ListaEmpleadosGerente";

    private RecyclerView recyclerView;
    private EmpleadoAdapter adapter;
    private ImageButton imgVolver;
    private List<Empleado> empleadosList;

    private ApiService apiService;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_empleados_activty);

        // --- 1. Inicialización de UI ---
        recyclerView = findViewById(R.id.recyclerView);
        imgVolver = findViewById(R.id.imgVolver);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // --- 2. Inicialización de API y Autenticación ---
        apiService = RetrofitClient.getApiService();

        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
        authToken = prefs.getString("jwtToken", null);

        if (authToken == null || authToken.isEmpty()) {
            Toast.makeText(this, "Error de autenticación: Token no disponible. Por favor, inicie sesión.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Token JWT no encontrado en SharedPreferences.");
            finish();
            return;
        }

        // --- 3. Configuración de Adapter y RecyclerView ---
        empleadosList = new ArrayList<>();
        adapter = new EmpleadoAdapter(empleadosList);
        recyclerView.setAdapter(adapter);

        // --- 4. Cargar empleados desde la API ---
        loadEmpleadosFromApi();

        // --- 5. Configuración de Listeners ---
        imgVolver.setOnClickListener(v -> {
            startActivity(new Intent(ListaEmpleadosGerente.this, GestionEmpleadosGerente.class));
            finish();
        });
    }

    private void loadEmpleadosFromApi() {
        String fullAuthToken = "Bearer " + authToken;
        Log.d(TAG, "Iniciando carga de empleados con token: " + fullAuthToken);

        Call<List<VerEmpleadoResponse>> call = apiService.listarEmpleadosActivos(fullAuthToken);

        call.enqueue(new Callback<List<VerEmpleadoResponse>>() {
            @Override
            public void onResponse(Call<List<VerEmpleadoResponse>> call, Response<List<VerEmpleadoResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<VerEmpleadoResponse> fetchedEmpleados = response.body();
                    Log.d(TAG, "Empleados cargados exitosamente desde la API. Total: " + fetchedEmpleados.size());

                    empleadosList.clear();
                    for (VerEmpleadoResponse empResponse : fetchedEmpleados) {
                        Empleado empleado = new Empleado(
                                empResponse.getNombre(),
                                empResponse.getApellido(),
                                empResponse.getEmail(),
                                empResponse.getTelefono()
                        );
                        empleadosList.add(empleado);
                    }

                    adapter.notifyDataSetChanged();

                    if (empleadosList.isEmpty()) {
                        Toast.makeText(ListaEmpleadosGerente.this, "No se encontraron empleados activos.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorBody = "Error desconocido al cargar empleados.";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error al leer errorBody de carga de empleados: " + e.getMessage());
                    }
                    Log.e(TAG, "Error al cargar empleados: Código " + response.code() + " - Mensaje: " + response.message() + " - Cuerpo: " + errorBody);
                    Toast.makeText(ListaEmpleadosGerente.this, "Error al cargar empleados: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<VerEmpleadoResponse>> call, Throwable t) {
                Log.e(TAG, "Fallo en la conexión al cargar empleados: " + t.getMessage(), t);
                Toast.makeText(ListaEmpleadosGerente.this, "Error de red: No se pudo conectar al servidor.", Toast.LENGTH_LONG).show();
            }
        });
    }
}