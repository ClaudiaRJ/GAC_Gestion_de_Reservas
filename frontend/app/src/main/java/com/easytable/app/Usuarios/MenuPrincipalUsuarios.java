package com.easytable.app.Usuarios;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.easytable.app.AdaptersYClases.Restaurante;
import com.easytable.app.AdaptersYClases.RestauranteAdapter;
import com.easytable.app.MenuPrincipal.IniciarSesion; // Asegúrate de que esta ruta sea correcta
import com.easytable.app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MenuPrincipalUsuarios extends AppCompatActivity {

    private static final String TAG = "MenuPrincipalUsuarios";

    private TextView tvNombUsuario;
    private EditText etNombRestaurante;
    private ImageButton imgBuscar, imgMenu, imgProfile;
    private RecyclerView recyclerView;
    private RestauranteAdapter restauranteAdapter;
    private List<Restaurante> listaRestaurantes;
    private List<Restaurante> listaRestaurantesFiltrada;

    private String usuarioEmail;
    private String nombreUsuarioDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal_usuario_activity);

        recyclerView = findViewById(R.id.recyclerView);
        etNombRestaurante = findViewById(R.id.etBuscar);
        imgBuscar = findViewById(R.id.imgBuscar);
        imgMenu = findViewById(R.id.imgMenu);
        imgProfile = findViewById(R.id.imgProfile);
        tvNombUsuario = findViewById(R.id.tvNombreUsuario);

        String emailFromIntent = getIntent().getStringExtra("emailUsuario");
        String nombreFromIntent = getIntent().getStringExtra("nombUsuario");

        if (emailFromIntent != null && !emailFromIntent.isEmpty()) {
            usuarioEmail = emailFromIntent;
            Log.d(TAG, "Email recuperado del Intent: " + usuarioEmail);
        } else {
            SharedPreferences prefs = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
            usuarioEmail = prefs.getString("userEmail", "");
            if (usuarioEmail.isEmpty()) {
                Log.w(TAG, "No se encontró email de usuario ni en Intent ni en SharedPreferences. Redirigiendo a IniciarSesion.");
                Toast.makeText(this, "Sesión no válida. Por favor, inicia sesión de nuevo.", Toast.LENGTH_LONG).show();
                Intent intentLogin = new Intent(this, IniciarSesion.class);
                intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentLogin);
                finish();
                return;
            } else {
                Log.d(TAG, "Email recuperado de SharedPreferences: " + usuarioEmail);
            }
        }

        if (nombreFromIntent != null && !nombreFromIntent.isEmpty()) {
            nombreUsuarioDisplay = nombreFromIntent;
            Log.d(TAG, "Nombre recuperado del Intent: " + nombreUsuarioDisplay);
        } else {
            SharedPreferences prefs = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
            nombreUsuarioDisplay = prefs.getString("userName", "Usuario desconocido");
            Log.d(TAG, "Nombre recuperado de SharedPreferences: " + nombreUsuarioDisplay);
        }

        tvNombUsuario.setText(nombreUsuarioDisplay);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (savedInstanceState == null) {
            listaRestaurantes = new ArrayList<>();
            // --- ¡AQUÍ ESTÁ EL CAMBIO CLAVE! ---
            // Asigna un ID numérico a cada restaurante.
            // Estos IDs DEBEN coincidir con los IDs reales de los restaurantes en tu API/base de datos.
            Restaurante aldenaire = new Restaurante("Aldenaire Kitchen", "Disfruta de lo mejor de la parrilla y cócteles en un ambiente acogedor. Perfecto para compartir buenos momentos con amigos y saborear platos únicos.", R.drawable.logo_aldenaire_kitchen);
            aldenaire.setId(1L); // Asigna el ID que corresponda en tu backend
            listaRestaurantes.add(aldenaire);

            Restaurante mantelRojo = new Restaurante("Mantel rojo", "Un rincón único donde la tradición y el buen gusto se encuentran. Disfruta de platos caseros y un ambiente acogedor, ideal para cualquier ocasión especial.", R.drawable.logo_mantel_rojo);
            mantelRojo.setId(2L); // Asigna el ID que corresponda en tu backend
            listaRestaurantes.add(mantelRojo);

            Restaurante rimberio = new Restaurante("Restaurante Rimberio", "Un viaje de sabores auténticos que te transporta a Italia. Disfruta de nuestras deliciosas pastas, pizzas y platos tradicionales en un ambiente acogedor", R.drawable.logo_restaurante_rimberio);
            rimberio.setId(3L); // Asigna el ID que corresponda en tu backend
            listaRestaurantes.add(rimberio);

        } else {
            // Asegúrate de que los restaurantes se recuperen correctamente (deben ser Serializable)
            listaRestaurantes = (List<Restaurante>) savedInstanceState.getSerializable("restaurantes");
            if (listaRestaurantes == null) {
                listaRestaurantes = new ArrayList<>();
            }
        }

        listaRestaurantesFiltrada = new ArrayList<>(listaRestaurantes);

        restauranteAdapter = new RestauranteAdapter(listaRestaurantesFiltrada, restaurante -> {
            Log.d(TAG, "Restaurante clickeado: " + restaurante.getNombreRestaurante());
            // Log para verificar que el ID se pasa correctamente
            Log.d(TAG, "ID del restaurante a pasar: " + restaurante.getId());
            Intent intentDetalles = new Intent(MenuPrincipalUsuarios.this, MenuRestauranteDetallesUsuario.class);
            intentDetalles.putExtra("restaurante", restaurante);
            startActivity(intentDetalles);
        });

        recyclerView.setAdapter(restauranteAdapter);

        etNombRestaurante.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filtrarRestaurantes(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        imgBuscar.setOnClickListener(v -> {
            String query = etNombRestaurante.getText().toString();
            filtrarRestaurantes(query);
        });

        final String finalNombreUsuarioForMenu = nombreUsuarioDisplay;

        imgMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(MenuPrincipalUsuarios.this, v);
            getMenuInflater().inflate(R.menu.menu_desplegable_usuario, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.reservasActivas) {
                    Intent intent = new Intent(MenuPrincipalUsuarios.this, MostrarReservasActivasUsuarios.class);
                    intent.putExtra("nombUsuario", finalNombreUsuarioForMenu);
                    intent.putExtra("emailUsuario", usuarioEmail);
                    startActivity(intent);
                } else if (itemId == R.id.ajustesCuenta) {
                    Intent intentAjustes = new Intent(MenuPrincipalUsuarios.this, AjustesPerfilUsuario.class);
                    intentAjustes.putExtra("nombUsuario", finalNombreUsuarioForMenu);
                    intentAjustes.putExtra("emailUsuario", usuarioEmail);
                    startActivity(intentAjustes);
                } else if (itemId == R.id.cerrarSesion) {
                    SharedPreferences prefs = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("jwtToken");
                    editor.remove("userEmail");
                    editor.remove("userName");
                    editor.apply();

                    Intent intentLogin = new Intent(this, IniciarSesion.class);
                    intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentLogin);
                    finish();
                }
                return false;
            });
            popupMenu.show();
        });

        imgProfile.setOnClickListener(v -> {
            Intent intentNomb = new Intent(MenuPrincipalUsuarios.this, AjustesPerfilUsuario.class);
            intentNomb.putExtra("nombUsuario", nombreUsuarioDisplay);
            intentNomb.putExtra("emailUsuario", usuarioEmail);
            startActivity(intentNomb);
        });
    }

    private void filtrarRestaurantes(String query) {
        if (query.isEmpty()) {
            listaRestaurantesFiltrada.clear();
            listaRestaurantesFiltrada.addAll(listaRestaurantes);
        } else {
            listaRestaurantesFiltrada = listaRestaurantes.stream()
                    .filter(restaurante -> restaurante.getNombreRestaurante().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }
        restauranteAdapter.updateList(listaRestaurantesFiltrada);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("restaurantes", new ArrayList<>(listaRestaurantes));
    }
}