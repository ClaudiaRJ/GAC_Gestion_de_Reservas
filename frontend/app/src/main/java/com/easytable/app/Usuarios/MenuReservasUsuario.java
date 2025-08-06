package com.easytable.app.Usuarios;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.easytable.app.API.ApiService;
import com.easytable.app.AdaptersYClases.Reserva;
import com.easytable.app.AdaptersYClases.Restaurante;
import com.easytable.app.API.RestauranteApi; // Importa la clase RestauranteApi
import com.easytable.app.AdaptersYClases.requests.reservas.ReservasRequest;
import com.easytable.app.AdaptersYClases.responses.reservas.VerReservaResponse;
import com.easytable.app.MenuPrincipal.IniciarSesion; // Importa IniciarSesion
import com.easytable.app.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuReservasUsuario extends AppCompatActivity {

    private static final String TAG = "MenuReservasUsuario"; // Etiqueta para los logs

    private ImageView imgFondo;
    private TextView tvFecha, tvNumComensales;
    private CalendarView calendarView;
    private Button btnAbrirTimePicker, btnRealizarReserva;
    private ImageButton btnMas, btnMenos, imgVolver;

    // Datos de la reserva
    private int numComensales = 1;
    private String horaSeleccionada = "";
    private String fechaSeleccionada = "";
    private Restaurante restauranteSeleccionado;

    // Datos de la sesión del usuario y de la reserva existente
    private Long idReservaExistente = null;
    private String emailUsuarioLogueado;
    private String tokenAuth;
    private ApiService servicioApi;

    // Propiedades adicionales de la reserva existente para modificación
    private String emailReservaExistente = "";
    private String estadoReservaExistente = "PENDIENTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_reserva_usuario);

        restauranteSeleccionado = (Restaurante) getIntent().getSerializableExtra("restaurante");
        if (restauranteSeleccionado == null) {
            Log.e(TAG, "Error: No se recibió un objeto Restaurante válido en el Intent.");
            Toast.makeText(this, "Error al cargar detalles del restaurante. Intente de nuevo.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        emailUsuarioLogueado = getIntent().getStringExtra("emailUsuario");
        if (emailUsuarioLogueado == null || emailUsuarioLogueado.isEmpty()) {
            SharedPreferences prefs = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
            emailUsuarioLogueado = prefs.getString("userEmail", "");
            if (emailUsuarioLogueado.isEmpty()) {
                Log.e(TAG, "Error: Email de usuario no disponible. Redirigiendo a IniciarSesion.");
                Toast.makeText(this, "Por favor, inicia sesión para continuar.", Toast.LENGTH_LONG).show();
                Intent intentLogin = new Intent(this, IniciarSesion.class);
                intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentLogin);
                finish();
                return;
            } else {
                Log.d(TAG, "Email recuperado de SharedPreferences: " + emailUsuarioLogueado);
            }
        } else {
            Log.d(TAG, "Email recuperado del Intent: " + emailUsuarioLogueado);
        }

        tokenAuth = obtenerTokenDePreferenciasCompartidas();
        if (tokenAuth.isEmpty()) {
            Log.e(TAG, "Error: Token de autenticación no disponible. Redirigiendo a IniciarSesion.");
            Toast.makeText(this, "Sesión expirada. Por favor, inicia sesión de nuevo.", Toast.LENGTH_LONG).show();
            Intent intentLogin = new Intent(this, IniciarSesion.class);
            intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentLogin);
            finish();
            return;
        }

        configurarServicioApiConToken(tokenAuth);

        // --- 2. Inicializar Vistas de la UI ---
        imgFondo = findViewById(R.id.imgFondo);
        tvFecha = findViewById(R.id.tvFecha);
        tvNumComensales = findViewById(R.id.tvNumComensales);
        calendarView = findViewById(R.id.calendarView);
        btnAbrirTimePicker = findViewById(R.id.btnAbrirTimePicker);
        btnRealizarReserva = findViewById(R.id.btnRealizarReserva);
        btnMas = findViewById(R.id.btnMas);
        btnMenos = findViewById(R.id.btnMenos);
        imgVolver = findViewById(R.id.imgVolver);

        // --- 3. Configuración inicial de la UI y listeners ---
        imgFondo.setImageResource(restauranteSeleccionado.getImagenResId());

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat sdfApi = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        fechaSeleccionada = sdfApi.format(cal.getTime());
        tvFecha.setText("Fecha: " + sdfDisplay.format(cal.getTime()));
        Log.d(TAG, "Fecha inicial seleccionada (API): " + fechaSeleccionada);
        Log.d(TAG, "Fecha inicial mostrada: " + sdfDisplay.format(cal.getTime()));


        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            fechaSeleccionada = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            tvFecha.setText("Fecha: " + String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year));
            Log.d(TAG, "Fecha seleccionada por calendario (API): " + fechaSeleccionada);
        });

        btnAbrirTimePicker.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hora = calendar.get(Calendar.HOUR_OF_DAY);
            int minuto = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    MenuReservasUsuario.this,
                    (view, hourOfDay, minute) -> {
                        horaSeleccionada = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        btnAbrirTimePicker.setText("Hora: " + horaSeleccionada);
                        Log.d(TAG, "Hora seleccionada: " + horaSeleccionada);
                    },
                    hora, minuto, true
            );
            timePickerDialog.show();
        });

        btnMas.setOnClickListener(v -> {
            if (numComensales < 20) numComensales++;
            tvNumComensales.setText("Comensales: " + numComensales);
            Log.d(TAG, "Comensales: " + numComensales);
        });

        btnMenos.setOnClickListener(v -> {
            if (numComensales > 1) numComensales--;
            tvNumComensales.setText("Comensales: " + numComensales);
            Log.d(TAG, "Comensales: " + numComensales);
        });

        // Botón para volver atrás
        imgVolver.setOnClickListener(v -> {
            Log.d(TAG, "Botón 'Volver' clickeado. Finalizando actividad.");
            finish();
        });

        // --- 4. Lógica para prellenar si es una MODIFICACIÓN de reserva ---
        Reserva reservaExistente = (Reserva) getIntent().getSerializableExtra("reserva");
        if (reservaExistente != null) {
            idReservaExistente = reservaExistente.getId();
            emailReservaExistente = reservaExistente.getEmailUsuario();
            estadoReservaExistente = reservaExistente.getEstado();


            fechaSeleccionada = reservaExistente.getFecha();
            horaSeleccionada = reservaExistente.getHora();
            numComensales = reservaExistente.getComensales();

            tvFecha.setText("Fecha: " + formatFechaDisplay(fechaSeleccionada));
            btnAbrirTimePicker.setText("Hora: " + horaSeleccionada);
            tvNumComensales.setText("Comensales: " + numComensales);

            try {
                Date date = sdfApi.parse(fechaSeleccionada);
                if (date != null) {
                    calendarView.setDate(date.getTime(), true, true);
                }
            } catch (ParseException e) {
                Log.e(TAG, "Error al parsear fecha de reserva existente: " + fechaSeleccionada + " - " + e.getMessage());
            }

            btnRealizarReserva.setText("Modificar reserva");
            Log.d(TAG, "Modo Modificación: Reserva ID " + idReservaExistente + " cargada.");
        } else {
            Log.d(TAG, "Modo Creación: No se detectó reserva existente.");
        }

        // *** LÍNEA CRÍTICA 3: LOG DE LO QUE SE RECUPERA DE SHARED PREFS AL INICIAR LA ACTIVIDAD ***
        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
        long clienteIdCheck = prefs.getLong("clienteId", -1L);
        Log.d(TAG, "RECUPERADO DE SHARED PREFS EN ONCREATE: clienteId = " + clienteIdCheck);


        // --- 5. Listener para el botón principal (Realizar/Modificar) ---
        btnRealizarReserva.setOnClickListener(v -> {
            realizarOModificarReserva();
        });
    }

    // --- Métodos de utilidad y API ---

    /**
     * Convierte una fecha de formato "YYYY-MM-DD" (API) a "DD/MM/YYYY" (display).
     * @param fechaApi La fecha en formato YYYY-MM-DD.
     * @return La fecha formateada para mostrar o la original si hay error.
     */
    private String formatFechaDisplay(String fechaApi) {
        try {
            SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = apiFormat.parse(fechaApi);
            return (date != null) ? displayFormat.format(date) : fechaApi;
        } catch (ParseException e) {
            Log.e(TAG, "Error formateando fecha para mostrar: " + e.getMessage());
            return fechaApi;
        }
    }

    /**
     * Configura la instancia de ApiService con el interceptor de autenticación.
     * @param token El token JWT del usuario logueado.
     */
    private void configurarServicioApiConToken(String token) {
        if (token == null || token.isEmpty()) {
            Log.e(TAG, "No se puede configurar ServicioApi: Token nulo o vacío.");
            servicioApi = null;
            return;
        }

        OkHttpClient cliente = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", "Bearer " + token);
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .client(cliente)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        servicioApi = retrofit.create(ApiService.class);
        Log.d(TAG, "ServicioApi configurado con interceptor de autenticación.");
    }

    /**
     * Obtiene el token JWT de SharedPreferences.
     * @return El token JWT o una cadena vacía si no se encuentra.
     */
    private String obtenerTokenDePreferenciasCompartidas() {
        SharedPreferences preferencias = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
        String token = preferencias.getString("jwtToken", "");
        Log.d(TAG, "Token recuperado de SharedPreferences: " + (token.isEmpty() ? "VACÍO" : "OK"));
        return token;
    }

    /**
     * Obtiene el email del usuario logueado de SharedPreferences.
     * @return El email del usuario o una cadena vacía si no se encuentra.
     */
    private String obtenerEmailDePreferenciasCompartidas() {
        SharedPreferences preferencias = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
        String email = preferencias.getString("userEmail", "");
        Log.d(TAG, "Email recuperado de SharedPreferences: " + (email.isEmpty() ? "VACÍO" : email));
        return email;
    }


    /**
     * Realiza la lógica para crear una nueva reserva o modificar una existente.
     */
    private void realizarOModificarReserva() {
        if (fechaSeleccionada.isEmpty() || horaSeleccionada.isEmpty()) {
            Toast.makeText(this, "Por favor, selecciona una fecha y hora.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (restauranteSeleccionado == null || restauranteSeleccionado.getNombreRestaurante() == null || restauranteSeleccionado.getNombreRestaurante().isEmpty()) {
            Toast.makeText(this, "Error: No se pudo determinar el restaurante.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (emailUsuarioLogueado == null || emailUsuarioLogueado.isEmpty()) {
            Toast.makeText(this, "Error: No se pudo obtener tu email de usuario. Reinicia la aplicación.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (servicioApi == null) {
            Toast.makeText(this, "Error: Servicio API no disponible. Intenta de nuevo más tarde.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userRole = "CLIENTE";
        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
        userRole = prefs.getString("userRole", "CLIENTE");

        if (idReservaExistente != null) {
            // --- Lógica para MODIFICAR RESERVA ---
            Log.d(TAG, "Iniciando solicitud para MODIFICAR reserva con ID: " + idReservaExistente);
            Log.d(TAG, "Email del usuario que modifica: " + emailUsuarioLogueado + ", Email de la reserva original: " + emailReservaExistente);


            Reserva reservaActualizada = new Reserva();
            reservaActualizada.setId(idReservaExistente);
            reservaActualizada.setObjetoRestaurante(new RestauranteApi(restauranteSeleccionado.getNombreRestaurante()));
            reservaActualizada.setFecha(fechaSeleccionada);
            reservaActualizada.setHora(horaSeleccionada);
            reservaActualizada.setComensales(numComensales);
            reservaActualizada.setEmailUsuario(emailReservaExistente);
            reservaActualizada.setEstado(estadoReservaExistente);

            Call<Reserva> call = servicioApi.modificarReserva(
                    idReservaExistente,
                    tokenAuth,
                    reservaActualizada,
                    userRole,
                    emailUsuarioLogueado
            );

            call.enqueue(new Callback<Reserva>() {
                @Override
                public void onResponse(Call<Reserva> call, Response<Reserva> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Reserva modificada exitosamente. Respuesta: " + response.body());
                        Toast.makeText(MenuReservasUsuario.this, "Reserva modificada exitosamente.", Toast.LENGTH_LONG).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        String errorBody = "";
                        try {
                            if (response.errorBody() != null) {
                                errorBody = response.errorBody().string();
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "Error al leer errorBody al modificar reserva: " + e.getMessage());
                        }
                        Log.e(TAG, "Error al modificar reserva: Código " + response.code() + " - Mensaje: " + response.message() + " - Cuerpo del error: " + errorBody);
                        Toast.makeText(MenuReservasUsuario.this, "Error al modificar reserva: " + (response.message().isEmpty() ? "Código " + response.code() : response.message()), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Reserva> call, Throwable t) {
                    Log.e(TAG, "Fallo de red al modificar reserva: " + t.getMessage(), t);
                    Toast.makeText(MenuReservasUsuario.this, "Fallo de conexión al modificar reserva.", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Long restauranteId = restauranteSeleccionado.getId(); // <-- Asegúrate de que restauranteSeleccionado.getId() devuelve el ID

            // *** LÍNEA CRÍTICA 4: SEGUNDO INTENTO DE RECUPERACIÓN DEL ID AL CREAR LA RESERVA ***
            prefs = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
            Long clienteId = prefs.getLong("clienteId", -1L); // Obtén el ID del cliente de SharedPreferences
            Log.d(TAG, "RECUPERADO DE SHARED PREFS EN REALIZAR/MODIFICAR RESERVA: clienteId = " + clienteId);


            if (restauranteId == null || restauranteId <= 0) { // Añade validación
                Toast.makeText(this, "Error: ID de restaurante no válido.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "ID de restaurante es nulo o inválido: " + restauranteId);
                return;
            }
            if (clienteId == -1L || clienteId <= 0) { // Añade validación
                Toast.makeText(this, "Error: ID de cliente no válido. Inicia sesión de nuevo.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "ID de cliente es nulo o inválido: " + clienteId); // Este es el log que sigues viendo
                return;
            }

            Log.d(TAG, "Iniciando solicitud para CREAR nueva reserva.");

            ReservasRequest nuevaReservaRequest = new ReservasRequest(
                    restauranteId, // ¡Ahora envías el ID!
                    clienteId,       // ¡Ahora envías el ID!
                    fechaSeleccionada,
                    horaSeleccionada,
                    numComensales
            );

            Log.d(TAG, "Datos a enviar para nueva reserva: " + nuevaReservaRequest.toString());

            Call<VerReservaResponse> call = servicioApi.crearReserva(tokenAuth, nuevaReservaRequest);

            call.enqueue(new Callback<VerReservaResponse>() {
                @Override
                public void onResponse(Call<VerReservaResponse> call, Response<VerReservaResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d(TAG, "Reserva creada exitosamente. Respuesta: " + response.body());
                        Toast.makeText(MenuReservasUsuario.this, "Reserva creada exitosamente.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MenuReservasUsuario.this, PantallaPago.class);
                        intent.putExtra("reservaId", response.body().getId());
                        startActivity(intent);
                        finish();
                    } else {
                        String errorBody = "";
                        try {
                            if (response.errorBody() != null) {
                                errorBody = response.errorBody().string();
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "Error al leer errorBody al crear reserva: " + e.getMessage());
                        }
                        Log.e(TAG, "Error al crear reserva: Código " + response.code() + " - Mensaje: " + response.message() + " - Cuerpo del error: " + errorBody);
                        Toast.makeText(MenuReservasUsuario.this, "Error al crear reserva: " + (response.message().isEmpty() ? "Código " + response.code() : response.message()), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<VerReservaResponse> call, Throwable t) {
                    Log.e(TAG, "Fallo de red al crear reserva: " + t.getMessage(), t);
                    Toast.makeText(MenuReservasUsuario.this, "Fallo de conexión al crear reserva.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}