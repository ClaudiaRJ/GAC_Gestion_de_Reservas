package com.easytable.app.Gerentes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.easytable.app.R;
import com.easytable.app.BasesDatos.DBRestaurantes;

public class ModificarDescripcionGerente extends AppCompatActivity {

    private EditText editTextDescripcion;
    private DBRestaurantes dbRestaurantes;
    private ImageButton imgVolver;
    private Button btnConfirmarCambios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_descripcion_activity);

        // Inicializar la conexión con la base de datos
        dbRestaurantes = new DBRestaurantes(this);
        editTextDescripcion = findViewById(R.id.edModificarDescripción);
        imgVolver = findViewById(R.id.imgVolver);
        btnConfirmarCambios = findViewById(R.id.btnPagar);

        // Configurar el botón "Aceptar cambio"
        findViewById(R.id.btnPagar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la nueva descripción del EditText
                String nuevaDescripcion = editTextDescripcion.getText().toString().trim();

                // Verificar si la descripción no está vacía
                if (!nuevaDescripcion.isEmpty()) {
                    // Llamar al método para modificar la descripción en la base de datos
                    modificarDescripcion(nuevaDescripcion);
                    Toast.makeText(ModificarDescripcionGerente.this, "Descripción modificada", Toast.LENGTH_SHORT).show();
                } else {
                    // Mostrar un mensaje de error si la descripción está vacía
                    Toast.makeText(ModificarDescripcionGerente.this, "La descripción no puede estar vacía", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgVolver.setOnClickListener(v ->{
            startActivity(new Intent(ModificarDescripcionGerente.this, MenuPrincipalGerente.class));
        });
    }

    // Método para modificar la descripción en la base de datos
    public void modificarDescripcion(String nuevoDescripcion) {
        SQLiteDatabase db = dbRestaurantes.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBRestaurantes.COLUMN_DESCRIPCION, nuevoDescripcion);

        String whereClause = "id_local = ?";
        String[] whereArgs = new String[]{"1"};
        int rowsAffected = db.update(DBRestaurantes.TABLE_RESTAURANTE, values, whereClause, whereArgs);

        if (rowsAffected > 0) {
            System.out.println("Descripción modificada correctamente.");
        } else {
            System.out.println("No se encontró el restaurante con el id especificado.");
        }
        db.close();
    }
}
