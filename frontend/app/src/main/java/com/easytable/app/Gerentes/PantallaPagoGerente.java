package com.easytable.app.Gerentes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easytable.app.Gerentes.MenuPrincipalGerente;
import com.easytable.app.Gerentes.PantallaCostesGerente;
import com.easytable.app.R;

import androidx.appcompat.app.AppCompatActivity;

public class PantallaPagoGerente extends AppCompatActivity {

    private TextView tvPrecio;
    private EditText etNumTarjeta, etFechaCadudcidad, etCVC, etTitular;
    private Button btnConfirmarPago, imgVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_pago_activty);

        etNumTarjeta = findViewById(R.id.etNumeroTarjeta);
        etFechaCadudcidad = findViewById(R.id.etFechaCaducidad);
        etCVC = findViewById(R.id.etCVC);
        etTitular = findViewById(R.id.etNombTitularTarjeta);
        tvPrecio = findViewById(R.id.tvCoste);
        btnConfirmarPago = findViewById(R.id.btnPagar);
        imgVolver = findViewById(R.id.imgVolver);

        btnConfirmarPago.setOnClickListener(v -> {
            Toast.makeText(this, "Pago realizado correctamente", Toast.LENGTH_SHORT).show();
        });

        imgVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PantallaPagoGerente.this, PantallaCostesGerente.class));
            }
        });
    }
}
