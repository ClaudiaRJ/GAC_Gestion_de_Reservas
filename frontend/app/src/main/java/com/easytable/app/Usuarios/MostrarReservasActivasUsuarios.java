package com.easytable.app.Usuarios;

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

public class MostrarReservasActivasUsuarios extends AppCompatActivity implements ReservaAdapter.OnReservaActionListener {

    private List<Reserva> listaReservas;
    private ReservaAdapter adaptador;
    private RecyclerView vistaReciclada;
    private TextView tvNumeroReservas;
    private TextView tvNombreUsuario;
    private String emailUsuario;
    private String tokenAuth;
    private ApiService servicioApi;
    private ImageButton imagMenuUsuario;
    private String nombreUsuarioDisplay; // Nueva variable para almacenar el nombre que se mostrará

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_reservas_activas_activity);

        vistaReciclada = findViewById(R.id.recyclerView);
        tvNumeroReservas = findViewById(R.id.tvNumeroReservas);
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        imagMenuUsuario = findViewById(R.id.imgMenu);

        // 1. Intentar obtener el email del Intent
        String emailFromIntent = getIntent().getStringExtra("emailUsuario");
        String nombreFromIntent = getIntent().getStringExtra("nombUsuario"); // Recupera el nombre también del Intent

        if (emailFromIntent != null && !emailFromIntent.isEmpty()) {
            emailUsuario = emailFromIntent;
            Log.d("MostrarReservas", "Email recuperado del Intent: " + emailUsuario);
        } else {
            // 2. Si no está en el Intent, intentar obtenerlo de SharedPreferences
            SharedPreferences prefs = getSharedPreferences("MiAppPrefs", MODE_PRIVATE);
            emailUsuario = prefs.getString("userEmail", null); // Recupera el email guardado en el login
            if (emailUsuario != null && !emailUsuario.isEmpty()) {
                Log.d("MostrarReservas", "Email recuperado de SharedPreferences: " + emailUsuario);
                Toast.makeText(this, "Email recuperado de sesión anterior.", Toast.LENGTH_SHORT).show();
            } else {
                // 3. Si no hay email en ningún lado, es un error fatal
                Toast.makeText(this, "Error: No se pudo encontrar el email del usuario.", Toast.LENGTH_LONG).show();
                Log.e("MostrarReservas", "Error fatal: Email de usuario no disponible ni en Intent ni en SharedPreferences.");
                finish(); // Ahora sí, se cierra porque no hay forma de operar.
                return;
            }
        }

        tokenAuth = obtenerTokenDePreferenciasCompartidas();

        // Lógica para determinar el nombre de usuario a mostrar y pasar
        if (nombreFromIntent != null && !nombreFromIntent.isEmpty()) {
            nombreUsuarioDisplay = nombreFromIntent; // Usa el del Intent si existe
        } else {
            // Si el nombre no vino en el Intent, intenta recuperarlo de SharedPreferences (si lo guardas)
            SharedPreferences prefs = getSharedPreferences("MiAppPrefs", MODE_PRIVATE);
            nombreUsuarioDisplay = prefs.getString("userName", "Usuario desconocido"); // <-- Asigna a nombreUsuarioDisplay
        }
        tvNombreUsuario.setText(nombreUsuarioDisplay); // Actualiza el TextView


        listaReservas = new ArrayList<>();
        // CAMBIO AQUI: Pasar el tipo de vista para el cliente
        adaptador = new ReservaAdapter(this, listaReservas, this, ReservaAdapter.TIPO_USUARIO_CLIENTE);
        vistaReciclada.setLayoutManager(new LinearLayoutManager(this));
        vistaReciclada.setAdapter(adaptador);

        if (tokenAuth != null && !tokenAuth.isEmpty()) {
            configurarServicioApiConToken(tokenAuth);
            cargarReservas();
        } else {
            Log.e("MostrarReservas", "Token de autenticación no disponible. No se cargarán las reservas.");
            Toast.makeText(this, "Sesión no válida. Por favor, inicie sesión de nuevo.", Toast.LENGTH_LONG).show();
            Intent intentLogin = new Intent(this, IniciarSesion.class);
            intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentLogin);
            finish();
        }

        // --- CÓDIGO PARA EL POPUPMENU DEL USUARIO ---
        if (imagMenuUsuario != null) {
            final String finalNombreUsuarioForMenu = nombreUsuarioDisplay;
            imagMenuUsuario.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(MostrarReservasActivasUsuarios.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_desplegable_usuario, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.reservasActivas) {
                        Toast.makeText(MostrarReservasActivasUsuarios.this, "Ya estás en tus reservas activas", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (itemId == R.id.ajustesCuenta) {
                        Intent intentAjustes = new Intent(MostrarReservasActivasUsuarios.this, AjustesPerfilUsuario.class);
                        intentAjustes.putExtra("nombUsuario", finalNombreUsuarioForMenu);
                        intentAjustes.putExtra("emailUsuario", emailUsuario);
                        startActivity(intentAjustes);
                        return true;
                    } else if (itemId == R.id.cerrarSesion) {
                        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.remove("jwtToken");
                        editor.remove("userEmail");
                        editor.remove("userName");
                        editor.apply();

                        Intent intentLogin = new Intent(MostrarReservasActivasUsuarios.this, IniciarSesion.class);
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
            Log.e("MostrarReservas", "No se puede configurar ServicioApi: Token nulo o vacío.");
            servicioApi = null;
            return;
        }

        OkHttpClient cliente = new OkHttpClient.Builder()
                .addInterceptor(cadena -> {
                    Request original = cadena.request();
                    Request.Builder constructorPeticion = original.newBuilder()
                            .header("Authorization", "Bearer " + token);
                    Request peticion = constructorPeticion.build();
                    return cadena.proceed(peticion);
                })
                .addInterceptor(new okhttp3.logging.HttpLoggingInterceptor().setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .client(cliente)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        servicioApi = retrofit.create(ApiService.class);
        Log.d("MostrarReservas", "ServicioApi configurado con interceptor de autenticación.");
    }

    private void cargarReservas() {
        if (servicioApi == null) {
            Toast.makeText(this, "Error: No se pudo iniciar la carga de reservas (Servicio API no disponible).", Toast.LENGTH_LONG).show();
            Log.e("MostrarReservas", "Servicio API es nulo. No se pueden cargar reservas.");
            runOnUiThread(() -> {
                listaReservas.clear();
                adaptador.notifyDataSetChanged();
                tvNumeroReservas.setText("0");
            });
            return;
        }

        Log.d("MostrarReservas", "Llamando a getReservasPorUsuario con email: " + emailUsuario);
        Call<List<VerReservaResponse>> llamada = servicioApi.getReservasPorUsuario(emailUsuario);

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
                        Log.d("MostrarReservas", "Tamaño final de listaReservas antes de notifyDataSetChanged: " + listaReservas.size());
                        adaptador.notifyDataSetChanged();
                        tvNumeroReservas.setText(String.valueOf(listaReservas.size()));
                        Log.d("MostrarReservas", "Reservas activas recibidas y añadidas a la lista: " + listaReservas.size());
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
                        mensajeToast = "No se encontraron reservas o el recurso no existe.";
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
                    Intent intent = new Intent(MostrarReservasActivasUsuarios.this, MenuReservasUsuario.class);
                    intent.putExtra("reserva", reserva);
                    intent.putExtra("emailUsuario", emailUsuario);
                    intent.putExtra("tokenAuth", tokenAuth);
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

        Reserva reservaParaCancelar = new Reserva(
                reserva.getId(),
                reserva.getObjetoRestaurante(),
                reserva.getFecha(),
                reserva.getHora(),
                reserva.getComensales(),
                reserva.getEmailUsuario(),
                "CANCELADA"
        );

        String userType = "CLIENTE";
        String emailToUse = emailUsuario;

        Call<Reserva> call = servicioApi.modificarReserva(
                reserva.getId(),
                tokenAuth,
                reservaParaCancelar,
                userType,
                emailToUse
        );

        call.enqueue(new Callback<Reserva>() {
            @Override
            public void onResponse(Call<Reserva> call, Response<Reserva> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MostrarReservasActivasUsuarios.this, "Reserva cancelada exitosamente.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MostrarReservasActivasUsuarios.this, "Error al cancelar reserva: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Reserva> call, Throwable t) {
                Log.e("API_CANCEL_FAILURE", "Fallo de red al cancelar reserva: " + t.getMessage(), t);
                Toast.makeText(MostrarReservasActivasUsuarios.this, "Fallo de conexión al cancelar reserva.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}