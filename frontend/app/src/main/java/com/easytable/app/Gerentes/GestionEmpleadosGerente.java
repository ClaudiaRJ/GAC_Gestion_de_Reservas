package com.easytable.app.Gerentes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.easytable.app.R;
import androidx.appcompat.app.AppCompatActivity;

public class GestionEmpleadosGerente extends AppCompatActivity{

    private Button btnAnadirEmpleados, btnListaEmpleados, btnEliminarEmpleados;
    private ImageButton imgVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_gestion_empleados_activity);

        btnAnadirEmpleados = findViewById(R.id.btnAñadirEmpleados);
        btnListaEmpleados = findViewById(R.id.btnListaEmpleados);
        btnEliminarEmpleados = findViewById(R.id.btnEliminarEmpleados);
        imgVolver = findViewById(R.id.imgVolver);

        btnAnadirEmpleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GestionEmpleadosGerente.this, AnadirEmpleadosGerente.class));
            }
        });

        btnListaEmpleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GestionEmpleadosGerente.this, ListaEmpleadosGerente.class));
            }
        });

        btnEliminarEmpleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GestionEmpleadosGerente.this, EliminarEmpleadoGerente.class));
            }
        });

        imgVolver.setOnClickListener(v ->{
            finish();
        });
    }
}
