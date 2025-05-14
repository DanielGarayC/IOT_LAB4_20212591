package com.example.lab4_20212591;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab4_20212591.adapter.ForecasterAdapter;
import com.example.lab4_20212591.model.ForecastDay;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForecasterFragment extends Fragment implements SensorEventListener {
    //flag
    boolean dialogoMostrado = false;
    //Definición de parámetros
    EditText etCiudad, etDias;
    Button btnBuscar;
    RecyclerView rvPronostico;
    ForecasterAdapter adapter;
    //Sensores declarados :D
    SensorManager sensorManager;
    Sensor accelerometer;
    float aceleracionInicial = 0;
    float aceleracionActual = 0;
    float aceleracion = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forecaster, container, false);
        //Definimos los elementos del layout owo
        etCiudad = view.findViewById(R.id.etCiudad);
        etDias = view.findViewById(R.id.etDias);
        btnBuscar = view.findViewById(R.id.btnBuscar);
        rvPronostico = view.findViewById(R.id.rvPronostico);
        rvPronostico.setLayoutManager(new LinearLayoutManager(getContext()));

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Lógica para recibir argumentos del flujo que proviene del LocationFragment
        Bundle args = getArguments();
        if (args != null && args.containsKey("idLocacion")) {
            String id = args.getString("idLocacion");
            int dias = args.getInt("dias", 14);  // valor por defecto again

            etCiudad.setText(id);
            etDias.setText(String.valueOf(dias));

            buscarPronostico(id, dias);
        }
        //Lógica al dar al botón Buscar
        btnBuscar.setOnClickListener(v -> {
            String ciudad = etCiudad.getText().toString().trim();
            String diasStr = etDias.getText().toString().trim();
            //Validaciones
            //Caso contrario no se cumplan se mostrará un Toast
            if (ciudad.isEmpty() || diasStr.isEmpty()) {
                Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int dias;
            try {
                dias = Integer.parseInt(diasStr);
                if (dias < 1 || dias > 14) {
                    Toast.makeText(getContext(), "Máximo 14 días", Toast.LENGTH_SHORT).show();
                    dias = 14;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Ingresa un número válido de días", Toast.LENGTH_SHORT).show();
                return;
            }

            buscarPronostico(ciudad, dias);
        });

        return view;
    }

    private void buscarPronostico(String ciudad, int dias) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //Se genera la URL para hacer la consulta a la API
        WeatherApiService api = retrofit.create(WeatherApiService.class);
        api.getForecast("ec24b1c6dd8a4d528c1205500250305", "id:" + ciudad, dias)
                .enqueue(new Callback<ForecastResponse>() {
                    @Override
                    public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            //Si la API responde con los datos correctamente, se entrega al adapter para
                            //completar los RecyclerView
                            List<ForecastDay> lista = response.body().getForecast().getForecastday();
                            adapter = new ForecasterAdapter(lista);
                            rvPronostico.setAdapter(adapter);
                        } else {
                            Toast.makeText(getContext(), "No se pudo obtener datos", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //Si falla la conexión a la API
                    @Override
                    public void onFailure(Call<ForecastResponse> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //Acelerómetro
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        if (aceleracionInicial == 0) {
            aceleracionInicial = (float) Math.sqrt(x * x + y * y + z * z);
            return;
        }

        aceleracionActual = (float) Math.sqrt((x * x + y * y + z * z));
        float delta = aceleracionActual - aceleracionInicial;
        //Se utiliza gpt para esta formula ya que tuve un problema de crasheo con el sensor
        //Salia el dialog cuando no habia ningun movimiento del celular
        aceleracion = aceleracion * 0.9f + delta;

        if (aceleracion > 20) {
            dialogoMostrado = true;
            //Se muestra un Dialog cuando se agita el celular a más de 20m/s^2
            new AlertDialog.Builder(getContext())
                    .setTitle("¿Deseas limpiar los pronósticos?")
                    .setMessage("Se ha detectado un movimiento fuerte.")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        if (rvPronostico != null) {
                            adapter = new ForecasterAdapter(new ArrayList<>());
                            rvPronostico.setAdapter(adapter);
                        }
                        dialogoMostrado = false;
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialogoMostrado = false;
                    })
                    .show();
            aceleracion = 0;
        }
    }
    //Para sensores (registro y pausa):
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        //Campos vacíos si se ingresa por la 2da forma a la sección Pronósticos
        if (getArguments() == null || !getArguments().containsKey("idLocacion")) {
            etCiudad.setText("");
            etDias.setText("");
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
