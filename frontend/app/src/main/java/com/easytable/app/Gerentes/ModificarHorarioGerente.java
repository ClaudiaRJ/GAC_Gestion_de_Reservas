package com.easytable.app.Gerentes;

import static android.content.ContentValues.TAG;

import android.app.TimePickerDialog;
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
import com.easytable.app.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModificarHorarioGerente extends AppCompatActivity {
    private Restaurante restauranteSeleccionado;
    private ImageView imgFondo;
    private TextView tvFecha, tvHora, tvNumComensales;
    private CalendarView calendarView;
    private Button btnAbrirTimePicker, btnModificarHorario;
    private ImageButton btnMas, btnMenos, imgVolver;
    private int numComensales = 1;
    private String horaSeleccionada = "";
    private String fechaSeleccionada = "";
    private Reserva reservaModificar;
    private ApiService apiService; // Instancia de tu servicio API
    private String authToken; // Token JWT para la autenticación
    private String gerenteEmail; // Email del gerente para la lógica de negoci

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_horario_activity);

        apiService = RetrofitClient.getApiService();
        reservaModificar = (Reserva) getIntent().getSerializableExtra("Reserva");

        SharedPreferences prefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        authToken = prefs.getString("jwtToken", null); // Asegúrate de guardar el JWT con esta clave al iniciar sesión
        gerenteEmail = prefs.getString("userEmail", null);

        // Recuperar restaurante desde el intent
        restauranteSeleccionado = (Restaurante) getIntent().getSerializableExtra("restaurante");

        // Referencias a views
        imgFondo = findViewById(R.id.imgFondo);
        tvFecha = findViewById(R.id.tvFecha);
        calendarView = findViewById(R.id.calendarView);
        btnAbrirTimePicker = findViewById(R.id.btnAbrirTimePicker);
        imgVolver = findViewById(R.id.imgVolver);
        btnModificarHorario = findViewById(R.id.btnModificarHorario);

        if (restauranteSeleccionado != null) {
            imgFondo.setImageResource(restauranteSeleccionado.getImagenResId());
        }

        // --- Pre-llenar campos si se está modificando una reserva existente ---
        if (reservaModificar != null) {
            fechaSeleccionada = reservaModificar.getFecha();
            horaSeleccionada = reservaModificar.getHora();
            numComensales = reservaModificar.getComensales();

            tvFecha.setText("Fecha: " + fechaSeleccionada);
            btnAbrirTimePicker.setText("Hora: " + horaSeleccionada);
            tvNumComensales.setText("Comensales: " + numComensales);

            // Si tienes un CalendarView que necesite ser pre-ajustado:
            // Asegúrate de que la fecha de reservaModificar.getFecha() esté en un formato parseable.
            // Ejemplo: "dd/MM/yyyy"
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date date = sdf.parse(fechaSeleccionada);
                calendarView.setDate(date.getTime(), true, true);
            } catch (Exception e) {
                Log.e(TAG, "Error al parsear fecha de reserva para CalendarView: " + e.getMessage());
            }

            btnModificarHorario.setText("Modificar horario de reserva"); // Texto para el botón
        } else {
            // Si es una nueva reserva o no hay datos previos, inicializa con la fecha actual
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            fechaSeleccionada = sdf.format(cal.getTime());
            tvFecha.setText("Fecha: " + fechaSeleccionada);
            tvNumComensales.setText("Comensales: " + numComensales); // Inicializa el texto de comensales
        }


        // Fecha del calendario
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            fechaSeleccionada = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, (month + 1), year);
            tvFecha.setText("Fecha: " + fechaSeleccionada);
            Log.d(TAG, "Fecha seleccionada: " + fechaSeleccionada);
        });

        // Hora con TimePicker
        btnAbrirTimePicker.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hora = calendar.get(Calendar.HOUR_OF_DAY);
            int minuto = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    ModificarHorarioGerente.this,
                    (TimePicker view, int hourOfDay, int minute1) -> {
                        horaSeleccionada = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                        btnAbrirTimePicker.setText("Hora: " + horaSeleccionada);
                        Log.d(TAG, "Hora seleccionada: " + horaSeleccionada);
                    },
                    hora, minuto, true
            );
            timePickerDialog.show();
        });

        // Comensales + y -
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

        // Volver
        imgVolver.setOnClickListener(v -> {
            finish(); // Simplemente cierra esta actividad para volver a la anterior
        });

        // --- Lógica del botón Modificar Horario de Reserva (API Call) ---
        btnModificarHorario.setOnClickListener(v -> {
            if (reservaModificar == null || reservaModificar.getId() == null) {
                Toast.makeText(this, "Error: No se encontró la reserva a modificar.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Intento de modificar reserva nula o sin BackendId.");
                return;
            }

            if (fechaSeleccionada.isEmpty() || horaSeleccionada.isEmpty() || numComensales == 0) {
                Toast.makeText(this, "Por favor, selecciona fecha, hora y comensales.", Toast.LENGTH_SHORT).show();
                return;
            }

            reservaModificar.setFecha(fechaSeleccionada);
            reservaModificar.setHora(horaSeleccionada);
            reservaModificar.setComensales(numComensales);

            // Asegúrate de que el backendId sea de tipo Long si así lo espera la API
            Long idReservaBackend = reservaModificar.getId();
            String tipoUsuario = "GERENTE"; // O el tipo de usuario adecuado para esta operación

            if (authToken == null || gerenteEmail == null) {
                Toast.makeText(this, "Error de autenticación. Inicia sesión de nuevo.", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Token JWT o email del gerente no encontrados.");
                return;
            }

            Log.d(TAG, "Preparando modificación de reserva. ID: " + idReservaBackend +
                    ", Fecha: " + fechaSeleccionada + ", Hora: " + horaSeleccionada +
                    ", Comensales: " + numComensales);

            Call<Reserva> call = apiService.modificarReserva(idReservaBackend, tipoUsuario, reservaModificar, gerenteEmail, authToken);
            call.enqueue(new Callback<Reserva>() {
                @Override
                public void onResponse(Call<Reserva> call, Response<Reserva> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ModificarHorarioGerente.this, "Reserva modificada correctamente", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Reserva modificada exitosamente.");
                        // Opcional: Volver a la lista de reservas del gerente o a la pantalla principal
                        Intent intent = new Intent(ModificarHorarioGerente.this, MostrarReservasGerente.class);
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
                            Log.e(TAG, "Error al leer cuerpo de error: " + e.getMessage());
                        }
                        Log.e(TAG, "Error al modificar reserva: " + response.code() + " - " + response.message() + " - " + errorBody);
                        Toast.makeText(ModificarHorarioGerente.this, "Error al modificar reserva: " + response.message(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Reserva> call, Throwable t) {
                    Log.e(TAG, "Fallo de red al modificar reserva: " + t.getMessage(), t);
                    Toast.makeText(ModificarHorarioGerente.this, "Fallo de conexión al modificar reserva", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
