package com.easytable.app.Gerentes;

import android.content.Intent;
import android.content.SharedPreferences; // Necesario para cerrar sesión de forma segura
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.easytable.app.MenuPrincipal.IniciarSesion;
import com.easytable.app.R;
import androidx.appcompat.app.AppCompatActivity;

public class MenuPrincipalGerente extends AppCompatActivity {

    private TextView tvNombGerente;
    private String nombreMostrableGerente; // Nombre para mostrar en la UI
    private String emailGerente;
    private Button btnGestionEmpleados, btnGestionNegocio, btnReservas;
    private ImageButton imgMenu, imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal_gerente_activity);

        tvNombGerente = findViewById(R.id.tvNombreGerente);
        btnGestionEmpleados = findViewById(R.id.btnGestionEmpleados);
        btnReservas = findViewById(R.id.btnReservas);
        btnGestionNegocio = findViewById(R.id.btnGestionNegocio);
        imgMenu = findViewById(R.id.imgMenu);
        imgProfile = findViewById(R.id.imgProfile);
        tvNombGerente = findViewById(R.id.tvNombreGerente);

        Intent intentFromLogin = getIntent();
        // Recibe el nombre para la UI
        nombreMostrableGerente = intentFromLogin.getStringExtra("nombreMostrableGerente");
        // Recibe el email real del gerente
        emailGerente = intentFromLogin.getStringExtra("emailGerente");

        // Bloques de Log y Toast para depuración (considera eliminarlos en producción)
        if (emailGerente != null && !emailGerente.isEmpty()) {
            Log.d("MenuPrincipalGerente", "Email del gerente recibido: " + emailGerente);
            Toast.makeText(this, "Email Gerente: " + emailGerente, Toast.LENGTH_LONG).show();
        } else {
            Log.w("MenuPrincipalGerente", "emailGerente es null o vacío. Asegúrate de pasarlo desde la actividad de login.");
            Toast.makeText(this, "Error: Email del gerente no recibido.", Toast.LENGTH_LONG).show();
        }

        // Establece el nombre visible en la UI
        if (nombreMostrableGerente != null && !nombreMostrableGerente.isEmpty()) {
            tvNombGerente.setText(nombreMostrableGerente);
        } else {
            tvNombGerente.setText("Gerente desconocido"); // Mensaje más descriptivo
        }

        btnGestionEmpleados.setOnClickListener(v -> {
            startActivity(new Intent(MenuPrincipalGerente.this, GestionEmpleadosGerente.class));
        });

        btnReservas.setOnClickListener(v -> {
            // Pasa el email REAL para la API y el nombre para la UI de la siguiente actividad
            Intent intentToReservas = new Intent(MenuPrincipalGerente.this, MostrarReservasGerente.class);
            intentToReservas.putExtra("emailGerente", emailGerente);
            intentToReservas.putExtra("nombGerente", nombreMostrableGerente); // Pasa el nombre para la UI de MostrarReservasGerente
            startActivity(intentToReservas);
        });

        btnGestionNegocio.setOnClickListener(v -> {
            startActivity(new Intent(MenuPrincipalGerente.this, GestionNegocioGerente.class));
        });

        imgMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(MenuPrincipalGerente.this, v);
            // Considera usar R.menu.menu_desplegable_gerente si tienes un menú específico
            popupMenu.getMenuInflater().inflate(R.menu.menu_desplegable_usuario, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId(); // Para compatibilidad con versiones antiguas
                if (itemId == R.id.ajustesCuenta) {
                    Intent intentToAjustes = new Intent(MenuPrincipalGerente.this, AjustesPerfilGerente.class);
                    intentToAjustes.putExtra("emailGerente", emailGerente); // Pasa el email real
                    intentToAjustes.putExtra("nombGerente", nombreMostrableGerente); // Pasa el nombre real
                    startActivity(intentToAjustes);
                    return true;
                } else if (itemId == R.id.cerrarSesion) {
                    // Limpiar SharedPreferences para cerrar sesión completamente
                    SharedPreferences prefs = getSharedPreferences("MiAppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("jwtToken");
                    editor.remove("userEmail"); // También es buena idea limpiar el email del usuario/gerente
                    editor.apply();

                    Intent intentLogin = new Intent(MenuPrincipalGerente.this, IniciarSesion.class);
                    // Flags para asegurar que el usuario no pueda volver al menú principal con el botón 'atrás'
                    intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentLogin);
                    finish(); // Cierra la actividad actual
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Vuelve a establecer el nombre en caso de que la actividad se reanude
        if (nombreMostrableGerente != null && !nombreMostrableGerente.isEmpty()) {
            tvNombGerente.setText(nombreMostrableGerente);
            Log.d("MenuPrincipalGerente", "onResume: Nombre actualizado a: " + nombreMostrableGerente);
        } else {
            tvNombGerente.setText("Gerente desconocido");
            Log.w("MenuPrincipalGerente", "onResume: nombreMostrableGerente es nulo o vacío.");
        }
    }
}