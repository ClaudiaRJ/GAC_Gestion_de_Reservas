package com.easytable.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.easytable.app.AdaptersYClases.HelperDAO;
import com.easytable.app.Usuarios.AjustesPerfilUsuario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AtencionCliente extends AppCompatActivity {

    private EditText etNombre, etEmail, etMensaje;
    private Button btnEnviar;
    private ImageButton imgVolver;
    private HelperDAO helperDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atencion_activity);

        etNombre = findViewById(R.id.etNombreCliente);
        etEmail = findViewById(R.id.etEmailCliente);
        etMensaje = findViewById(R.id.etMensajeCliente);
        btnEnviar = findViewById(R.id.btnEnviarMensaje);
        imgVolver = findViewById(R.id.imgVolver);

        helperDao = new HelperDAO(this);

        btnEnviar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            String queja = etMensaje.getText().toString();
            String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            helperDao.insertarConsulta(nombre, queja, fecha);

            etNombre.setText("");
            etEmail.setText("");
            etMensaje.setText("");

            mostrarConsultas();

            // Mostrar diálogo de confirmación
            new AlertDialog.Builder(AtencionCliente.this)
                    .setTitle("Consulta enviada")
                    .setMessage("Tu mensaje ha sido enviado correctamente.")
                    .setCancelable(false)
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        // Volver a AjustesPerfilUsuario
                        Intent intent = new Intent(AtencionCliente.this, AjustesPerfilUsuario.class);
                        startActivity(intent);
                        finish(); // Opcional: cerrar esta actividad para no volver con atrás
                    })
                    .show();
        });

        imgVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AtencionCliente.this, AjustesPerfilUsuario.class));
            }
        });

        mostrarConsultas();
    }


    private void mostrarConsultas() {
        ArrayList<String> consultas = helperDao.obtenerConsultas();
        for (String consulta : consultas) {
            System.out.println("Consulta: " + consulta);
        }
    }

}
