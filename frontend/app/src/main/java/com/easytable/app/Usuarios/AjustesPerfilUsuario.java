package com.easytable.app.Usuarios;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.easytable.app.AtencionCliente;
import com.easytable.app.MenuPrincipal.IniciarSesion;
import com.easytable.app.Preferencias;
import com.easytable.app.R;

public class AjustesPerfilUsuario extends AppCompatActivity {

    private static final String TAG = "AjustesPerfilUsuario";

    private TextView tvNombUsuario, tvReservasActivas, tvAtencionCliente, tvPreferencias, tvEditarPerfil;
    private Button btnCerrarSesion;
    private ImageButton imgVolver;

    private String nombreUsuarioDisplay;
    private String emailUsuario;
    private String tokenAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajustes_perfil_usuario_activity);

        // Inicializar vistas
        tvNombUsuario = findViewById(R.id.tvNombUsuario);
        tvReservasActivas = findViewById(R.id.tvReservasActivas);
        tvAtencionCliente = findViewById(R.id.tvAtencionCliente);
        tvPreferencias = findViewById(R.id.tvPreferencias);
        tvEditarPerfil = findViewById(R.id.tvEditarPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        imgVolver = findViewById(R.id.imgVolver);

        // 1. Intentar obtener el email y el nombre del usuario desde el Intent
        String emailFromIntent = getIntent().getStringExtra("emailUsuario");
        String nombreFromIntent = getIntent().getStringExtra("nombUsuario");

        if (emailFromIntent != null && !emailFromIntent.isEmpty()) {
            emailUsuario = emailFromIntent;
            Log.d(TAG, "Email recuperado del Intent: " + emailUsuario);
        } else {
            SharedPreferences prefs = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
            emailUsuario = prefs.getString("userEmail", "");
            if (emailUsuario.isEmpty()) {
                Log.e(TAG, "Error: Email de usuario no disponible ni en Intent ni en SharedPreferences.");
                Toast.makeText(this, "Error: No se pudo cargar la información de sesión.", Toast.LENGTH_LONG).show();
                Intent intentLogin = new Intent(this, IniciarSesion.class);
                intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentLogin);
                finish();
                return;
            } else {
                Log.d(TAG, "Email recuperado de SharedPreferences: " + emailUsuario);
            }
        }

        if (nombreFromIntent != null && !nombreFromIntent.isEmpty()) {
            nombreUsuarioDisplay = nombreFromIntent;
            Log.d(TAG, "Nombre recuperado del Intent: " + nombreUsuarioDisplay);
        } else {
            SharedPreferences prefs = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
            nombreUsuarioDisplay = prefs.getString("userName", "Usuario desconocido"); // Clave para el nombre
            Log.d(TAG, "Nombre recuperado de SharedPreferences: " + nombreUsuarioDisplay);
        }

        tvNombUsuario.setText(nombreUsuarioDisplay);

        tokenAuth = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE)
                .getString("jwtToken", null);
        if (tokenAuth == null || tokenAuth.isEmpty()) {
            Log.w(TAG, "Token de autenticación no disponible. Las llamadas a la API podrían fallar si fueran necesarias aquí.");
        }

        // --- Configurar Listeners para Navegación ---

        imgVolver.setOnClickListener(v -> {
            Log.d(TAG, "Botón 'Volver' clickeado. Navegando a MenuPrincipalUsuarios.");
            Intent intentVolver = new Intent(AjustesPerfilUsuario.this, MenuPrincipalUsuarios.class);
            intentVolver.putExtra("emailUsuario", emailUsuario);
            intentVolver.putExtra("nombUsuario", nombreUsuarioDisplay);
            intentVolver.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intentVolver);
            finish();
        });

        tvReservasActivas.setOnClickListener(v -> {
            Log.d(TAG, "TextView 'Reservas Activas' clickeado. Navegando a MostrarReservasActivasUsuarios.");
            Intent intentReservas = new Intent(AjustesPerfilUsuario.this, MostrarReservasActivasUsuarios.class);
            // Pasa el email y nombre a la actividad de reservas
            intentReservas.putExtra("emailUsuario", emailUsuario);
            intentReservas.putExtra("nombUsuario", nombreUsuarioDisplay);
            startActivity(intentReservas);
        });

        tvAtencionCliente.setOnClickListener(v -> {
            Log.d(TAG, "TextView 'Atención al Cliente' clickeado. Navegando a AtencionCliente.");
            // No se suelen pasar email/nombre a una actividad de atención al cliente a menos que sea un chat con identificación
            startActivity(new Intent(AjustesPerfilUsuario.this, AtencionCliente.class));
        });

        tvPreferencias.setOnClickListener(v -> {
            Log.d(TAG, "TextView 'Preferencias' clickeado. Navegando a Preferencias.");
            startActivity(new Intent(AjustesPerfilUsuario.this, Preferencias.class));
        });

        tvEditarPerfil.setOnClickListener(v -> {
            Log.d(TAG, "TextView 'Editar Perfil' clickeado. Navegando a EditarPerfilUsuario.");
            Intent intentEditar = new Intent(AjustesPerfilUsuario.this, EditarPerfilUsuario.class);
            intentEditar.putExtra("emailUsuario", emailUsuario);
            intentEditar.putExtra("nombUsuario", nombreUsuarioDisplay);
            intentEditar.putExtra("tokenAuth", tokenAuth);
            startActivity(intentEditar);
        });

        btnCerrarSesion.setOnClickListener(v -> {
            Log.d(TAG, "Botón 'Cerrar Sesión' clickeado.");
            SharedPreferences prefs = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("jwtToken");
            editor.remove("userEmail");
            editor.remove("userName");
            editor.apply();

            Log.i(TAG, "Datos de sesión limpiados de SharedPreferences. Redirigiendo a IniciarSesion.");

            Intent intentLogin = new Intent(AjustesPerfilUsuario.this, IniciarSesion.class);
            intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentLogin);
            finish();
        });
    }
}