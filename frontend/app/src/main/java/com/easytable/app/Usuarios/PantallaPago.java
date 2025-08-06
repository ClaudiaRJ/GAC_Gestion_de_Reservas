package com.easytable.app.Usuarios;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.easytable.app.R;

public class PantallaPago extends AppCompatActivity {

    private TextView tvPrecio;
    private EditText etNumTarjeta, etFechaCaducidad, etCVC, etTitular;
    private Button btnConfirmarPago;
    private ImageButton imgVolver;
    private double precioTotal;
    private int comensales;
    private String restaurante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_pago_activty);

        etNumTarjeta = findViewById(R.id.etNumeroTarjeta);
        etFechaCaducidad = findViewById(R.id.etFechaCaducidad);
        etCVC = findViewById(R.id.etCVC);
        etTitular = findViewById(R.id.etNombTitularTarjeta);
        tvPrecio = findViewById(R.id.tvCoste);
        btnConfirmarPago = findViewById(R.id.btnPagar);
        imgVolver = findViewById(R.id.imgVolver);

        // Obtener datos del intent
        restaurante = getIntent().getStringExtra("restaurante");
        String fecha = getIntent().getStringExtra("fecha");
        String hora = getIntent().getStringExtra("hora");
        comensales = getIntent().getIntExtra("comensales", 1);
        precioTotal = getIntent().getDoubleExtra("precio", comensales * 16.0);

        tvPrecio.setText("Total a pagar: " + precioTotal + "€");

        btnConfirmarPago.setOnClickListener(v -> {
            if (validarDatos()) {
                // Aquí iría la lógica real del pago (API de pago, etc.)

                new AlertDialog.Builder(PantallaPago.this)
                        .setTitle("Pago y reserva confirmados")
                        .setMessage("El pago se ha realizado correctamente y tu reserva ha sido creada.")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                            Intent intent = new Intent(PantallaPago.this, MenuPrincipalUsuarios.class);
                            intent.putExtra("restaurante", restaurante);
                            intent.putExtra("fecha", fecha);
                            intent.putExtra("hora", hora);
                            intent.putExtra("comensales", comensales);
                            intent.putExtra("precio", precioTotal);
                            startActivity(intent);
                            finish(); // Cierra esta actividad para no volver con atrás
                        })
                        .show();
            }
        });

        imgVolver.setOnClickListener(v -> {
            Intent intent = new Intent(PantallaPago.this, MenuRestauranteDetallesUsuario.class);
            // Si quieres pasar datos, agrégalos aquí
            startActivity(intent);
            finish();
        });
    }

    private boolean validarDatos() {
        boolean valido = true;

        if (TextUtils.isEmpty(etNumTarjeta.getText().toString()) || etNumTarjeta.getText().toString().length() < 16) {
            etNumTarjeta.setError("Número de tarjeta inválido");
            valido = false;
        }

        if (TextUtils.isEmpty(etFechaCaducidad.getText().toString())) {
            etFechaCaducidad.setError("Fecha de caducidad requerida");
            valido = false;
        }

        if (TextUtils.isEmpty(etCVC.getText().toString()) || etCVC.getText().toString().length() < 3) {
            etCVC.setError("CVC inválido");
            valido = false;
        }

        if (TextUtils.isEmpty(etTitular.getText().toString())) {
            etTitular.setError("Titular requerido");
            valido = false;
        }

        return valido;
    }
}
