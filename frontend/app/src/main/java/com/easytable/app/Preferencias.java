package com.easytable.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class Preferencias extends AppCompatActivity {

    private Switch switchNotifications;
    private Spinner spinnerNotificationType;
    private EditText editLocation;
    private Button buttonSavePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferencias_activity);

        switchNotifications = findViewById(R.id.switchNotificacion);
        spinnerNotificationType = findViewById(R.id.spNotificacionTipo);
        editLocation = findViewById(R.id.etUbicacion);
        buttonSavePreferences = findViewById(R.id.btnGuardarPreferencias);

        // Configurar el Spinner antes de cargar preferencias
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.notification_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNotificationType.setAdapter(adapter);

        loadPreferences();

        buttonSavePreferences.setOnClickListener(v -> savePreferences());
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean notificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", true);
        switchNotifications.setChecked(notificationsEnabled);

        String notificationType = sharedPreferences.getString("notification_type", "Push");
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerNotificationType.getAdapter();
        int spinnerPosition = adapter.getPosition(notificationType);
        spinnerNotificationType.setSelection(spinnerPosition);

        String location = sharedPreferences.getString("preferred_location", "");
        editLocation.setText(location);
    }

    private void savePreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("notifications_enabled", switchNotifications.isChecked());
        editor.putString("notification_type", spinnerNotificationType.getSelectedItem().toString()); // ✅ añadido
        editor.putString("preferred_location", editLocation.getText().toString());

        editor.apply();
    }
}
