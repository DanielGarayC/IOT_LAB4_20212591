package com.example.lab4_20212591;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.lab4_20212591.adapter.LocationAdapter;
import com.example.lab4_20212591.model.Location;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationFragment extends Fragment {

    EditText etBuscar;
    Button btnBuscar;
    RecyclerView rvLocations;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_location, container, false);
        //Elementos del layout owo
        etBuscar = vista.findViewById(R.id.etBuscar);
        btnBuscar = vista.findViewById(R.id.btnBuscar);
        rvLocations = vista.findViewById(R.id.rvLocations);
        rvLocations.setLayoutManager(new LinearLayoutManager(getContext()));
        //Al dar a buscar se va a la función buscarUbicaciones
        btnBuscar.setOnClickListener(v -> buscarUbicaciones());

        return vista;
    }
    //Función encargada de generar la consulta a la API y entregar datos al adapter
    //Para completar el RecyclerView
    private void buscarUbicaciones() {
        //Se extrae lo que buscó el usuario
        String query = etBuscar.getText().toString().trim();
        if (query.isEmpty()) return;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //C llama al endpoint
        WeatherApiService api = retrofit.create(WeatherApiService.class);

        //Navegación al realizar la query
        api.buscarUbicaciones("ec24b1c6dd8a4d528c1205500250305", query)
                .enqueue(new Callback<List<Location>>() {
                    @Override
                    public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            LocationAdapter adapter = new LocationAdapter(response.body(), location -> {
                                Bundle bundle = new Bundle();
                                bundle.putString("idLocacion", String.valueOf(location.getId()));
                                bundle.putInt("dias", 14);

                                NavHostFragment.findNavController(LocationFragment.this)
                                        .navigate(R.id.action_locationFragment_to_forecasterFragment, bundle);
                            });
                            rvLocations.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Location>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }
}