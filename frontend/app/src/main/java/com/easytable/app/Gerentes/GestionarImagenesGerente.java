package com.easytable.app.Gerentes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.easytable.app.R;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class GestionarImagenesGerente extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private ImageButton[] imageButtons;
    private ImageButton imgVolver; // Este parece que lo tienes duplicado, revisa bien el layout

    private int selectedImageButtonId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestionar_imagenes_activity);

        imageView = findViewById(R.id.imageView);
        imageView.setVisibility(ImageView.GONE);
        imgVolver = findViewById(R.id.imgVolver);

        imageButtons = new ImageButton[] {
                findViewById(R.id.img1),
                findViewById(R.id.img2),
                findViewById(R.id.img3),
                findViewById(R.id.img4),
                findViewById(R.id.imageButton6),
                findViewById(R.id.imageButton5),
                findViewById(R.id.imageButton7),
                findViewById(R.id.imageButton8),
                findViewById(R.id.imageButton9)
        };

        for (ImageButton imageButton : imageButtons) {
            imageButton.setOnClickListener(v -> {
                selectedImageButtonId = v.getId();
                openGallery();
            });
        }

        Button btnAplicarCambios = findViewById(R.id.btnAplicarCambios);
        btnAplicarCambios.setOnClickListener(v -> {
            if (selectedImageButtonId != -1) {
                Toast.makeText(this, "Cambios aplicados.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se ha seleccionado ninguna imagen.", Toast.LENGTH_SHORT).show();
            }
        });

        imgVolver.setOnClickListener(v ->{
            finish();
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(ImageView.VISIBLE);
                ImageButton selectedButton = findViewById(selectedImageButtonId);
                if (selectedButton != null) {
                    selectedButton.setImageBitmap(bitmap);
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
