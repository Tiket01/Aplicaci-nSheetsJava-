package com.example.formulariogooglesheetsjava;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Declarar las variables
    private EditText etNombres, etPrimerApellido, etSegundoApellido, etEdad, etFechaNacimiento, etEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar las variables
        etNombres = findViewById(R.id.etNombres);
        etPrimerApellido = findViewById(R.id.etPrimerApellido);
        etSegundoApellido = findViewById(R.id.etSegundoApellido);
        etEdad = findViewById(R.id.etEdad);
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento);
        etEstado = findViewById(R.id.etEstado);
        Button btnEnviar = findViewById(R.id.btnEnviar);

        // Permitir operaciones de red en el hilo principal (solo para pruebas)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnEnviar.setOnClickListener(v -> {
            // Obtener los datos del formulario
            String nombres = etNombres.getText().toString();
            String primerApellido = etPrimerApellido.getText().toString();
            String segundoApellido = etSegundoApellido.getText().toString();
            String edad = etEdad.getText().toString();
            String fechaNacimiento = etFechaNacimiento.getText().toString();
            String estado = etEstado.getText().toString();

            // Validar campos vacíos
            if (nombres.isEmpty() || primerApellido.isEmpty() || edad.isEmpty() || fechaNacimiento.isEmpty() || estado.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener la fecha y hora actuales
            String fechaRegistro = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String hora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

            // Enviar los datos
            enviarDatos(nombres, primerApellido, segundoApellido, edad, fechaNacimiento, hora, fechaRegistro, estado);
        });

    }

    private void enviarDatos(String nombres, String primerApellido, String segundoApellido, String edad, String fechaNacimiento, String hora, String fechaRegistro, String estado) {
        try {
            // URL del script de Google Apps
            URL url = new URL("https://script.google.com/macros/s/AKfycbydOTxqDUIJ-FdoAVD1n4OocLa7JVkg0A3U2g91IieB4Gn7X_JIfwiuyUxfx9khFjQN/exec");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            // Crear el JSON con los datos
            JSONObject json = new JSONObject();
            json.put("nombres", nombres);
            json.put("primerApellido", primerApellido);
            json.put("segundoApellido", segundoApellido);
            json.put("edad", edad);
            json.put("fechaNacimiento", fechaNacimiento);
            json.put("hora", hora);
            json.put("fechaRegistro", fechaRegistro);
            json.put("estado", estado);

            // Enviar los datos al servidor
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(json.toString());
            writer.flush();
            writer.close();

            // Verificar la respuesta del servidor
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Toast.makeText(this, "Datos enviados con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al enviar los datos", Toast.LENGTH_SHORT).show();
            }

            connection.disconnect();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
