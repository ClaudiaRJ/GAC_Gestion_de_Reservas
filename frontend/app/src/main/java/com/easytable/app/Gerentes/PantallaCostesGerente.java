package com.easytable.app.Gerentes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.easytable.app.R;
import com.easytable.app.Usuarios.ModificarReservaUsuarios;
import com.easytable.app.Usuarios.MostrarReservasActivasUsuarios;

public class PantallaCostesGerente extends AppCompatActivity {

    private Button btn12Meses, btn1Mes, btn3Meses, btnPagar;
    private ImageButton imgVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_costes_activity); // Asegúrate de que este sea el nombre correcto del archivo XML de la vista

        // Inicializar los botones
        btn12Meses = findViewById(R.id.button); // 12 meses 119,99€
        btn1Mes = findViewById(R.id.button2);   // 1 mes 14,99€
        btn3Meses = findViewById(R.id.btnPagar); // Pagar
        btnPagar = findViewById(R.id.btnPagar);  // Botón de "Pagar"

        // Establecer listeners para los botones
        btn12Meses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción cuando el botón "12 meses" es presionado
                Toast.makeText(PantallaCostesGerente.this, "Has seleccionado 12 meses 119,99€", Toast.LENGTH_SHORT).show();
            }
        });

        btn1Mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción cuando el botón "1 mes" es presionado
                Toast.makeText(PantallaCostesGerente.this, "Has seleccionado 1 mes 14,99€", Toast.LENGTH_SHORT).show();
            }
        });

        btn3Meses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción cuando el botón "3 meses" es presionado
                Toast.makeText(PantallaCostesGerente.this, "Has seleccionado 3 meses 39,99€", Toast.LENGTH_SHORT).show();
            }
        });

        // Acción para el botón de "Pagar"
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción cuando el botón "Pagar" es presionado
                Toast.makeText(PantallaCostesGerente.this, "Procesando el pago...", Toast.LENGTH_SHORT).show();
                // Aquí podrías agregar la lógica para procesar el pago o cambiar a otra actividad
            }
        });

        imgVolver.setOnClickListener(v -> {
            startActivity(new Intent(PantallaCostesGerente.this, MenuPrincipalGerente.class));
        });
    }
}
