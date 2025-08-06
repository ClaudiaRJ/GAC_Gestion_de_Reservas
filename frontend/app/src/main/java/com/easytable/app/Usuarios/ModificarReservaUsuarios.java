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
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.easytable.app.API.ApiService;
import com.easytable.app.API.RetrofitClient;
import com.easytable.app.AdaptersYClases.Reserva;
import com.easytable.app.AdaptersYClases.Restaurante;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import com.easytable.app.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ModificarReservaUsuarios extends AppCompatActivity {
    private static final String TAG = "ModificarReservaUsuario";

    private Restaurante restauranteSeleccionado;
    private ImageView imgFondo;
    private TextView tvFecha, tvNumComensales;
    private CalendarView calendarView;
    private Button btnAbrirTimePicker, btnModificarReserva;
    private ImageButton btnMas, btnMenos, imgVolver;
    private int numComensales = 1;
    private String horaSeleccionada = "";
    private String fechaSeleccionada = "";

    private Reserva reservaModificar;

    private ApiService apiService;
    private String authToken;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_reserva_activity);

        apiService = RetrofitClient.getApiService();

        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
        authToken = prefs.getString("jwtToken", null);
        userEmail = prefs.getString("userEmail", null);

        if (authToken == null || userEmail == null) {
            Toast.makeText(this, "Error de autenticación. Por favor, inicie sesión de nuevo.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Token JWT o email del usuario no encontrados en SharedPreferences.");
            finish();
            return;
        }

        // Recuperar la reserva a modificar desde el intent
        reservaModificar = (Reserva) getIntent().getSerializableExtra("reserva");
        // *** PRIMERA CORRECCIÓN: Usar getId() en lugar de getBackendId()
        if (reservaModificar == null || reservaModificar.getId() == null) {
            Toast.makeText(this, "Error: No se recibió la reserva para modificar o no tiene ID.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "No se recibió objeto Reserva o no tiene ID.");
            finish();
            return;
        }

        restauranteSeleccionado = (Restaurante) getIntent().getSerializableExtra("restaurante");

        imgFondo = findViewById(R.id.imgFondo);
        tvFecha = findViewById(R.id.tvFecha);
        tvNumComensales = findViewById(R.id.tvNumComensales);
        calendarView = findViewById(R.id.calendarView);
        btnAbrirTimePicker = findViewById(R.id.btnAbrirTimePicker);
        btnMas = findViewById(R.id.btnMas);
        btnMenos = findViewById(R.id.btnMenos);
        imgVolver = findViewById(R.id.imgVolver);
        btnModificarReserva = findViewById(R.id.btnModificarReserva);


        try {
            LocalDate parsedDate = LocalDate.parse(reservaModificar.getFecha(), DateTimeFormatter.ISO_LOCAL_DATE);
            fechaSeleccionada = parsedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault()));
        } catch (DateTimeParseException e) {
            Log.e(TAG, "Error parseando fecha de reserva (ISO_LOCAL_DATE) para pre-llenado: " + e.getMessage());
            fechaSeleccionada = reservaModificar.getFecha();
        }

        horaSeleccionada = reservaModificar.getHora();
        numComensales = reservaModificar.getComensales();

        tvFecha.setText("Fecha: " + fechaSeleccionada);
        btnAbrirTimePicker.setText("Hora: " + horaSeleccionada);
        tvNumComensales.setText("Comensales: " + numComensales);

        try {
            SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date dateForCalendar = sdfDisplay.parse(fechaSeleccionada);
            if (dateForCalendar != null) {
                calendarView.setDate(dateForCalendar.getTime(), true, true);
            }
        } catch (ParseException e) {
            Log.e(TAG, "Error al parsear fecha para CalendarView (dd/MM/yyyy): " + e.getMessage());
        }


        // --- 4. Configuración de Listeners para la interacción del usuario ---

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            fechaSeleccionada = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, (month + 1), year);
            tvFecha.setText("Fecha: " + fechaSeleccionada);
            Log.d(TAG, "Nueva fecha seleccionada: " + fechaSeleccionada);
        });

        btnAbrirTimePicker.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            int currentMinute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    ModificarReservaUsuarios.this,
                    (view, hourOfDay, minute) -> {
                        horaSeleccionada = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        btnAbrirTimePicker.setText("Hora: " + horaSeleccionada);
                        Log.d(TAG, "Nueva hora seleccionada: " + horaSeleccionada);
                    },
                    currentHour, currentMinute, true
            );
            timePickerDialog.show();
        });

        btnMas.setOnClickListener(v -> {
            if (numComensales < 20) numComensales++;
            tvNumComensales.setText("Comensales: " + numComensales);
            Log.d(TAG, "Comensales actualizados: " + numComensales);
        });

        btnMenos.setOnClickListener(v -> {
            if (numComensales > 1) numComensales--;
            tvNumComensales.setText("Comensales: " + numComensales);
            Log.d(TAG, "Comensales actualizados: " + numComensales);
        });

        imgVolver.setOnClickListener(v -> {
            finish();
        });

        // --- 5. Listener para el botón de Modificar Reserva (¡La llamada a la API!) ---
        btnModificarReserva.setOnClickListener(v -> {
            if (fechaSeleccionada.isEmpty() || horaSeleccionada.isEmpty() || numComensales == 0) {
                Toast.makeText(this, "Por favor, selecciona fecha, hora y número de comensales.", Toast.LENGTH_SHORT).show();
                return;
            }

            String fechaParaBackend = "";
            try {
                DateTimeFormatter appFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate localDate = LocalDate.parse(fechaSeleccionada, appFormatter);
                DateTimeFormatter apiFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                fechaParaBackend = localDate.format(apiFormatter);
            } catch (DateTimeParseException e) {
                Log.e(TAG, "Error al formatear fecha para el backend (esperado yyyy-MM-dd): " + e.getMessage());
                Toast.makeText(this, "Error con el formato de fecha, intente de nuevo.", Toast.LENGTH_SHORT).show();
                return;
            }

            reservaModificar.setFecha(fechaParaBackend);
            reservaModificar.setHora(horaSeleccionada);
            reservaModificar.setComensales(numComensales);

            // *** SEGUNDA CORRECCIÓN: Usar getId() en lugar de getBackendId()
            Long idReservaBackend = reservaModificar.getId();
            String tipoUsuario = "CLIENTE";

            String fullAuthToken = "Bearer " + authToken;

            Log.d(TAG, "Preparando llamada para modificar reserva. ID: " + idReservaBackend +
                    ", Fecha: " + fechaParaBackend + ", Hora: " + horaSeleccionada +
                    ", Comensales: " + numComensales + ", UserEmail: " + userEmail);


            Call<Reserva> call = apiService.modificarReserva(
                    idReservaBackend,
                    fullAuthToken,
                    reservaModificar,
                    tipoUsuario,
                    userEmail
            );

            call.enqueue(new Callback<Reserva>() {
                @Override
                public void onResponse(Call<Reserva> call, Response<Reserva> response) {
                    if (response.isSuccessful()) {
                        Reserva reservaRespuesta = response.body();
                        Toast.makeText(ModificarReservaUsuarios.this, "Reserva modificada con éxito.", Toast.LENGTH_SHORT).show();
                        // *** TERCERA CORRECCIÓN: Usar getId() en lugar de getBackendId()
                        Log.d(TAG, "Reserva ID " + reservaRespuesta.getId() + " modificada exitosamente.");

                        Intent intent = new Intent(ModificarReservaUsuarios.this, MostrarReservasActivasUsuarios.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorBody = "";
                        try {
                            if (response.errorBody() != null) {
                                errorBody = response.errorBody().string();
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "Error al leer el cuerpo del error de la API: " + e.getMessage());
                        }

                        Log.e(TAG, "Error al modificar reserva: Código " + response.code() + " - Mensaje: " + response.message() + " - Cuerpo del error: " + errorBody);

                        String toastMessage;
                        if (response.code() == 401 || response.code() == 403) {
                            toastMessage = "No autorizado para modificar esta reserva. Inicie sesión de nuevo.";
                        } else if (response.code() == 404) {
                            toastMessage = "Reserva no encontrada o recurso no disponible.";
                        } else if (response.code() == 400) {
                            toastMessage = "Datos de reserva inválidos. Revise la información.";
                        } else {
                            toastMessage = "Error al modificar reserva: " + response.code();
                        }
                        Toast.makeText(ModificarReservaUsuarios.this, toastMessage, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Reserva> call, Throwable t) {
                    Log.e(TAG, "Fallo de red al intentar modificar reserva: " + t.getMessage(), t);
                    Toast.makeText(ModificarReservaUsuarios.this, "Error de conexión. Verifique su internet.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}