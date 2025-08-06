package com.easytable.app.Gerentes;

import android.content.Intent;
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

public class AjustesPerfilGerente extends AppCompatActivity {

    private TextView tvNombUsuario, tvAtencionCliente, tvPreferencias;
    private String nombUsuario;
    private Button btnCerrarSesion;
    private ImageButton imgVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajustes_perfil_gerente_activity);

        tvNombUsuario = findViewById(R.id.tvNombUsuario);
        tvAtencionCliente = findViewById(R.id.tvAtencionCliente);
        tvPreferencias = findViewById(R.id.tvPreferencias);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        imgVolver = findViewById(R.id.imgVolver);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("nombUsuario")) {
            nombUsuario = intent.getStringExtra("nombUsuario");

            Log.d("AjustesPerfilUsuario", nombUsuario);
            if (nombUsuario != null && !nombUsuario.isEmpty()) {
                tvNombUsuario.setText(nombUsuario);
            } else {
                tvNombUsuario.setText("El usuario no existe");
                Toast.makeText(this, "Error: El usuario no existe", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, IniciarSesion.class));
            }
        } else {
            Toast.makeText(this, "Error: Datos de usuario no encontrados", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, IniciarSesion.class));
        }

        tvAtencionCliente.setOnClickListener(v ->
                startActivity(new Intent(AjustesPerfilGerente.this, AtencionCliente.class))
        );

        tvPreferencias.setOnClickListener(v ->
                startActivity(new Intent(AjustesPerfilGerente.this, Preferencias.class))
        );

        btnCerrarSesion.setOnClickListener(v -> {
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AjustesPerfilGerente.this, IniciarSesion.class));
            finish();
        });

        imgVolver.setOnClickListener(v ->{
           startActivity(new Intent(AjustesPerfilGerente.this, MenuPrincipalGerente.class));
        });
    }
}
