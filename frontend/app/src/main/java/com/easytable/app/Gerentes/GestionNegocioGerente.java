package com.easytable.app.Gerentes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.easytable.app.R;

public class GestionNegocioGerente extends AppCompatActivity {
    private Button btnModificarDescripcion, btnModificarHorario, btnGestionarImagenes;
    private ImageButton imgVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_gestion_negocio_activity);

        btnModificarDescripcion = findViewById(R.id.btnModificarDescripción);
        btnModificarHorario = findViewById(R.id.btnModificarHorario);
        btnGestionarImagenes = findViewById(R.id.btnGestionarImagenes);
        imgVolver = findViewById(R.id.imgVolver);

        btnModificarDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GestionNegocioGerente.this, ModificarDescripcionGerente.class));
            }
        });

        btnModificarHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GestionNegocioGerente.this, ModificarHorarioGerente.class));
            }
        });

        btnGestionarImagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí, GestionNegocioGerente lanza GestionarImagenesGerente
                startActivity(new Intent(GestionNegocioGerente.this, GestionarImagenesGerente.class));
            }
        });

        imgVolver.setOnClickListener(v ->{
            finish();
        });
    }
}
