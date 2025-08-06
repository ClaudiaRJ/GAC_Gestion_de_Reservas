package com.easytable.app.Gerentes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easytable.app.API.ApiService;
import com.easytable.app.API.RestauranteApi;
import com.easytable.app.AdaptersYClases.Reserva;
import com.easytable.app.AdaptersYClases.ReservaAdapter;
import com.easytable.app.AdaptersYClases.responses.reservas.VerReservaResponse;
import com.easytable.app.MenuPrincipal.IniciarSesion;
import com.easytable.app.R;
import com.easytable.app.Usuarios.MenuReservasUsuario; // Nota: Si el gerente modifica, ¿lo hace con la misma UI de usuario?

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MostrarReservasGerente extends AppCompatActivity implements ReservaAdapter.OnReservaActionListener {

    private List<Reserva> listaReservas;
    private ReservaAdapter adaptador;
    private RecyclerView vistaReciclada;
    private TextView tvNumeroReservas;
    private String emailGerente;
    private String nombreMostrableGerente;
    private String tokenAuth;
    private ApiService servicioApi;
    private ImageButton imgMenuGerente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservas_gerente_activity);

        vistaReciclada = findViewById(R.id.recyclerView);
        tvNumeroReservas = findViewById(R.id.tvNumeroReservas);
        imgMenuGerente = findViewById(R.id.imgMenu);

        // Obtener el nombre para mostrar en la UI
        nombreMostrableGerente = getIntent().getStringExtra("nombGerente");
        // Obtener el email real del gerente para las llamadas a la API
        emailGerente = getIntent().getStringExtra("emailGerente");

        tokenAuth = obtenerTokenDePreferenciasCompartidas();

        if (emailGerente == null || emailGerente.isEmpty()) {
            Toast.makeText(this, "Error: Email de gerente no encontrado para API.", Toast.LENGTH_LONG).show();
            Log.w("MostrarReservasGerente", "No se recibió emailGerente en el Intent o está vacío, no se pueden cargar reservas.");
            finish();
            return;
        }

        listaReservas = new ArrayList<>();
        // CAMBIO AQUI: Pasar el tipo de vista para el gerente
        adaptador = new ReservaAdapter(this, listaReservas, this, ReservaAdapter.TIPO_USUARIO_GERENTE);
        vistaReciclada.setLayoutManager(new LinearLayoutManager(this));
        vistaReciclada.setAdapter(adaptador);

        configurarServicioApiConToken(tokenAuth);
        cargarReservas();

        // --- CÓDIGO DEL POPUPMENU PARA EL GERENTE ---
        if (imgMenuGerente != null) {
            final String finalNombreGerenteRecibido = nombreMostrableGerente;
            final String finalEmailGerenteRecibido = emailGerente;

            imgMenuGerente.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(MostrarReservasGerente.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_desplegable_gerente, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.ajustesCuenta) {
                        Toast.makeText(MostrarReservasGerente.this, "Navegar a Ajustes de Cuenta", Toast.LENGTH_SHORT).show();
                        Intent intentAjustes = new Intent(MostrarReservasGerente.this, AjustesPerfilGerente.class);
                        intentAjustes.putExtra("emailGerente", finalEmailGerenteRecibido);
                        intentAjustes.putExtra("nombGerente", finalNombreGerenteRecibido);
                        startActivity(intentAjustes);
                        return true;
                    } else if (itemId == R.id.cerrarSesion) {
                        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.remove("jwtToken");
                        editor.remove("userEmail");
                        editor.apply();

                        Intent intentLogin = new Intent(MostrarReservasGerente.this, IniciarSesion.class);
                        intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentLogin);
                        finish();
                        return true;
                    }
                    return false;
                });
                popupMenu.show();
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarReservas();
    }

    private void configurarServicioApiConToken(String token) {
        if (token == null || token.isEmpty()) {
            Log.e("MostrarReservasGerente", "No se puede configurar ServicioApi: Token nulo o vacío.");
            servicioApi = null;
            return;
        }

        OkHttpClient cliente = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder constructorPeticion = original.newBuilder()
                            .header("Authorization", "Bearer " + token);
                    Request peticion = constructorPeticion.build();
                    return chain.proceed(peticion);
                })
                .addInterceptor(new okhttp3.logging.HttpLoggingInterceptor().setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .client(cliente)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        servicioApi = retrofit.create(ApiService.class);
        Log.d("MostrarReservasGerente", "ServicioApi configurado con interceptor de autenticación.");
    }

    private void cargarReservas() {
        if (servicioApi == null) {
            Toast.makeText(this, "Error: No se pudo iniciar la carga de reservas (Servicio API no disponible).", Toast.LENGTH_LONG).show();
            Log.e("MostrarReservasGerente", "Servicio API es nulo. No se pueden cargar reservas.");
            runOnUiThread(() -> {
                listaReservas.clear();
                adaptador.notifyDataSetChanged();
                tvNumeroReservas.setText("0");
            });
            return;
        }

        Log.d("MostrarReservasGerente", "Llamando a getReservasActivasGerente con email: " + emailGerente);
        Call<List<VerReservaResponse>> llamada = servicioApi.getReservasActivasGerente(tokenAuth, emailGerente);

        llamada.enqueue(new Callback<List<VerReservaResponse>>() {
            @Override
            public void onResponse(Call<List<VerReservaResponse>> llamada, Response<List<VerReservaResponse>> respuesta) {
                if (respuesta.isSuccessful() && respuesta.body() != null) {
                    List<Reserva> reservasRecibidas = new ArrayList<>();
                    for (VerReservaResponse rr : respuesta.body()) {
                        RestauranteApi restauranteApi = (rr.getRestaurante() != null) ?
                                new RestauranteApi(rr.getRestaurante().getNombre()) : null;

                        Reserva nuevaReserva = new Reserva(
                                rr.getId(),
                                restauranteApi,
                                rr.getFecha(),
                                rr.getHora(),
                                rr.getCantidadPersonas(),
                                rr.getUsuarioEmail() != null ? rr.getUsuarioEmail() : "",
                                rr.getEstadoReserva()
                        );
                        reservasRecibidas.add(nuevaReserva);
                    }

                    List<Reserva> soloActivas = new ArrayList<>();
                    for (Reserva res : reservasRecibidas) {
                        if (res.estaActiva()) {
                            soloActivas.add(res);
                        }
                    }

                    runOnUiThread(() -> {
                        listaReservas.clear();
                        listaReservas.addAll(soloActivas);
                        adaptador.notifyDataSetChanged();
                        tvNumeroReservas.setText(String.valueOf(listaReservas.size()));
                        Toast.makeText(getApplicationContext(), "Reservas cargadas desde API.", Toast.LENGTH_SHORT).show();
                    });

                } else {
                    String cuerpoError = "";
                    try {
                        if (respuesta.errorBody() != null) {
                            cuerpoError = respuesta.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e("API_ERROR", "Error al leer cuerpoError: " + e.getMessage());
                    }
                    Log.e("API_ERROR", "Error al cargar reservas: Código " + respuesta.code() + " - Mensaje: " + respuesta.message() + " - Cuerpo del error: " + cuerpoError);

                    String mensajeToast;
                    if (respuesta.code() == 401 || respuesta.code() == 403) {
                        mensajeToast = "Sesión expirada o no autorizado. Inicie sesión de nuevo.";
                    } else if (respuesta.code() == 404) {
                        mensajeToast = "No se encontraron reservas para este gerente.";
                    } else {
                        mensajeToast = "Error al cargar reservas: " + respuesta.code();
                    }
                    Toast.makeText(getApplicationContext(), mensajeToast, Toast.LENGTH_LONG).show();

                    runOnUiThread(() -> {
                        listaReservas.clear();
                        adaptador.notifyDataSetChanged();
                        tvNumeroReservas.setText("0");
                    });
                }
            }

            @Override
            public void onFailure(Call<List<VerReservaResponse>> llamada, Throwable t) {
                Log.e("API_FAILURE", "Fallo de red al conectar con servidor: " + t.getMessage(), t);
                Toast.makeText(getApplicationContext(), "Fallo al conectar con servidor. Verifique su conexión.", Toast.LENGTH_SHORT).show();
                runOnUiThread(() -> {
                    listaReservas.clear();
                    adaptador.notifyDataSetChanged();
                    tvNumeroReservas.setText("0");
                });
            }
        });
    }

    private String obtenerTokenDePreferenciasCompartidas() {
        SharedPreferences preferencias = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
        String token = preferencias.getString("jwtToken", "");
        Log.d("MostrarReservas", "Token recuperado de Preferencias Compartidas: " + (token.isEmpty() ? "Vacío" : "OK"));
        return token;
    }

    @Override
    public void onModificarClick(Reserva reserva) {
        showModificarDialog(reserva);
    }

    @Override
    public void onCancelarClick(Reserva reserva) {
        showCancelarDialog(reserva);
    }

    private void showModificarDialog(Reserva reserva) {
        new AlertDialog.Builder(this)
                .setTitle("Modificar Reserva")
                .setMessage("¿Deseas modificar la reserva para " +
                        (reserva.getObjetoRestaurante() != null ? reserva.getObjetoRestaurante().getNombre() : "Restaurante Desconocido") +
                        " el " + reserva.getFecha() + " a las " + reserva.getHora() + "?")
                .setPositiveButton("Modificar", (dialog, which) -> {
                    // CUIDADO AQUÍ: Si MenuReservasUsuario es SOLO para usuarios,
                    // y el gerente necesita una interfaz diferente para modificar,
                    // deberías crear una actividad como 'MenuReservasGerenteModificar.java'
                    // o adaptar MenuReservasUsuario para ambos roles.
                    // Por ahora, asumo que MenuReservasUsuario puede manejar la modificación de un gerente.
                    Intent intent = new Intent(MostrarReservasGerente.this, MenuReservasUsuario.class);
                    intent.putExtra("reserva", reserva);
                    intent.putExtra("emailGerente", emailGerente); // Pasa el email real
                    intent.putExtra("tokenAuth", tokenAuth); // Pasa el token
                    // También podrías pasar un flag para indicar que es un gerente modificando
                    intent.putExtra("esGerente", true); // <-- Nuevo flag para MenuReservasUsuario
                    startActivity(intent);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void showCancelarDialog(Reserva reserva) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Cancelación")
                .setMessage("¿Estás seguro de que quieres cancelar la reserva para " +
                        (reserva.getObjetoRestaurante() != null ? reserva.getObjetoRestaurante().getNombre() : "Restaurante Desconocido") +
                        " el " + reserva.getFecha() + " a las " + reserva.getHora() + "?")
                .setPositiveButton("Sí, Cancelar", (dialog, which) -> {
                    cancelarReservaAPI(reserva);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void cancelarReservaAPI(Reserva reserva) {
        if (servicioApi == null) {
            Toast.makeText(this, "Error: Servicio API no disponible para cancelar.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (reserva.getId() == null) {
            Toast.makeText(this, "Error: ID de reserva no válido para cancelar.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Para el gerente, se usa userType="GERENTE" y emailToUse=emailGerente (el email del gerente que realiza la acción)
        // en la llamada a la API para modificar el estado de la reserva.
        Reserva reservaParaModificarEstado = new Reserva( // Podría ser una reserva para modificar estado
                reserva.getId(),
                reserva.getObjetoRestaurante(),
                reserva.getFecha(),
                reserva.getHora(),
                reserva.getComensales(),
                reserva.getEmailUsuario(), // Es el email del cliente de la reserva
                "CANCELADA" // Nuevo estado
        );

        String userType = "GERENTE"; // El tipo de usuario que realiza la acción
        String emailToUse = emailGerente; // El email del gerente que está logueado

        Call<Reserva> call = servicioApi.modificarReserva(
                reserva.getId(),
                tokenAuth,
                reservaParaModificarEstado, // Pasamos el objeto con el nuevo estado
                userType,
                emailToUse
        );

        call.enqueue(new Callback<Reserva>() {
            @Override
            public void onResponse(Call<Reserva> call, Response<Reserva> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MostrarReservasGerente.this, "Reserva cancelada exitosamente.", Toast.LENGTH_SHORT).show();
                    cargarReservas();
                } else {
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e("API_CANCEL_ERROR", "Error al leer errorBody: " + e.getMessage());
                    }
                    Log.e("API_CANCEL_ERROR", "Error al cancelar reserva: Código " + response.code() + " - Mensaje: " + response.message() + " - Cuerpo del error: " + errorBody);
                    Toast.makeText(MostrarReservasGerente.this, "Error al cancelar reserva: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Reserva> call, Throwable t) {
                Log.e("API_CANCEL_FAILURE", "Fallo de red al cancelar reserva: " + t.getMessage(), t);
                Toast.makeText(MostrarReservasGerente.this, "Fallo de conexión al cancelar reserva.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}