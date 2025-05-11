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
import android.widget.Button;
import android.widget.EditText;

import com.example.lab4_20212591.adapter.ForecasterAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForecasterFragment extends Fragment {

    EditText etCiudad, etDias;
    Button btnBuscar;
    RecyclerView rvPronostico;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forecaster, container, false);

        etCiudad = view.findViewById(R.id.etCiudad);
        etDias = view.findViewById(R.id.etDias);
        btnBuscar = view.findViewById(R.id.btnBuscar);
        rvPronostico = view.findViewById(R.id.rvPronostico);
        rvPronostico.setLayoutManager(new LinearLayoutManager(getContext()));

        btnBuscar.setOnClickListener(v -> {
            String ciudad = etCiudad.getText().toString().trim();
            String diasStr = etDias.getText().toString().trim();

            if (ciudad.isEmpty() || diasStr.isEmpty()) return;

            int dias = Integer.parseInt(diasStr);
            if (dias < 1 || dias > 14) dias = 14;

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.weatherapi.com/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WeatherApiService api = retrofit.create(WeatherApiService.class);
            api.getForecast("ec24b1c6dd8a4d528c1205500250305", ciudad, dias)
                    .enqueue(new Callback<ForecastResponse>() {
                        @Override
                        public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<ForecastDay> lista = response.body().getForecast().getForecastday();
                                rvPronostico.setAdapter(new ForecasterAdapter(lista));
                            }
                        }

                        @Override
                        public void onFailure(Call<ForecastResponse> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
        });

        return view;
    }
}