package com.easytable.app.Usuarios;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easytable.app.AdaptersYClases.Restaurante;
import com.easytable.app.R;
import androidx.appcompat.app.AppCompatActivity;

public class MenuRestauranteDetallesUsuario extends AppCompatActivity {

    private static final String TAG = "RestauranteDetalles";

    private TextView tvNombRestaurante, tvDescripcion, tvReservar;
    private ImageButton imgVolver;
    private ImageView imgRestauranteFondo;

    private Restaurante restauranteActual;
    private String usuarioEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_restaurante_detalles_activity);

        tvNombRestaurante = findViewById(R.id.tvNombreRestaurante);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        tvReservar = findViewById(R.id.tvReservar);
        imgVolver = findViewById(R.id.imgVolver);
        imgRestauranteFondo = findViewById(R.id.imgFondo);

        Intent intent = getIntent();
        restauranteActual = (Restaurante) intent.getSerializableExtra("restaurante");
        usuarioEmail = intent.getStringExtra("emailUsuario"); // <-- ¡RECUPERA EL EMAIL AQUÍ!

        if (restauranteActual != null) {
            tvNombRestaurante.setText(restauranteActual.getNombreRestaurante());
            tvDescripcion.setText(restauranteActual.getDescripcion());
            if (imgRestauranteFondo != null) {
                imgRestauranteFondo.setImageResource(restauranteActual.getImagenResId());
            }
            Log.d(TAG, "Restaurante cargado: " + restauranteActual.getNombreRestaurante());
        } else {
            Log.e(TAG, "Error: No se recibió el objeto Restaurante completo en el Intent.");
            Toast.makeText(this, "Error al cargar detalles del restaurante. Intente de nuevo.", Toast.LENGTH_LONG).show();
            tvNombRestaurante.setText("Restaurante no disponible");
            tvDescripcion.setText("No se pudieron cargar los detalles.");
            finish();
            return;
        }

        // Validación del email (opcional, ya se hace en MenuPrincipalUsuarios)
        if (usuarioEmail == null || usuarioEmail.isEmpty()) {
            Log.w(TAG, "Email de usuario no recibido en MenuRestauranteDetallesUsuario. Posiblemente de SharedPreferences.");
            // Si no viene en el intent, podrías intentar cargarlo de SharedPreferences aquí
            // SharedPreferences prefs = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
            // usuarioEmail = prefs.getString("userEmail", "");
        } else {
            Log.d(TAG, "Email de usuario recibido en MenuRestauranteDetallesUsuario: " + usuarioEmail);
        }


        tvReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Botón 'Reservar' clickeado. Navegando a MenuReservasUsuario.");
                Intent intentReservar = new Intent(MenuRestauranteDetallesUsuario.this, MenuReservasUsuario.class);
                intentReservar.putExtra("restaurante", restauranteActual);
                intentReservar.putExtra("emailUsuario", usuarioEmail); // <-- ¡AHORA 'usuarioEmail' DEBERÍA TENER UN VALOR!
                startActivity(intentReservar);
            }
        });

        imgVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuRestauranteDetallesUsuario.this, MenuPrincipalUsuarios.class));
                finish();
            }
        });
    }
}