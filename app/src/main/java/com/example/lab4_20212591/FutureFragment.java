package com.example.lab4_20212591;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab4_20212591.adapter.FutureAdapter;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FutureFragment extends Fragment {

    EditText etCiudad, etFecha;
    MaterialButton btnBuscar;
    RecyclerView rvPronostico;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_future, container, false);

        etCiudad = view.findViewById(R.id.etCiudad);
        etFecha  = view.findViewById(R.id.etDias);
        btnBuscar = view.findViewById(R.id.btnBuscar);
        rvPronostico = view.findViewById(R.id.rvPronostico);
        rvPronostico.setLayoutManager(new LinearLayoutManager(getContext()));

        btnBuscar.setOnClickListener(v -> {
            String id = etCiudad.getText().toString().trim();
            String fecha = etFecha.getText().toString().trim();

            if (id.isEmpty() || fecha.isEmpty()) {
                Toast.makeText(getContext(), "Completa ambos campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validación de formato de fecha :D
            if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
                Toast.makeText(getContext(), "Formato de fecha inválido (YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
                return;
            }
            //Validación para que sea un pronóstico de un dia lejano (mayor a 14 días)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);

            try {
                Date fechaIngresada = sdf.parse(fecha);
                Calendar hoy = Calendar.getInstance();

                Calendar minimo = Calendar.getInstance();
                minimo.add(Calendar.DAY_OF_YEAR, 14); // mínimo 14 días en adelante

                Calendar maximo = Calendar.getInstance();
                maximo.add(Calendar.DAY_OF_YEAR, 300); // máximo 300 días en adelante

                if (fechaIngresada.before(minimo.getTime()) || fechaIngresada.after(maximo.getTime())) {
                    Toast.makeText(getContext(),
                            "La fecha debe ser entre " +
                                    sdf.format(minimo.getTime()) + " y " +
                                    sdf.format(maximo.getTime()),
                            Toast.LENGTH_LONG).show();
                    return;
                }

            } catch (ParseException e) {
                Toast.makeText(getContext(), "Fecha inválida. Usa el formato YYYY-MM-DD", Toast.LENGTH_SHORT).show();
                return;
            }

            buscarPronosticoFuturo(id, fecha);
        });

        return view;
    }

    private void buscarPronosticoFuturo(String ciudadId, String fecha) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApiService api = retrofit.create(WeatherApiService.class);
        api.getFuture("ec24b1c6dd8a4d528c1205500250305", "id:" + ciudadId, fecha)
                .enqueue(new Callback<FutureResponse>() {
                    @Override
                    public void onResponse(Call<FutureResponse> call, Response<FutureResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<FutureResponse.Hour> horas = response.body()
                                    .getForecast()
                                    .getForecastday().get(0)
                                    .getHour();
                            rvPronostico.setAdapter(new FutureAdapter(horas));
                        }
                    }

                    @Override
                    public void onFailure(Call<FutureResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }
}