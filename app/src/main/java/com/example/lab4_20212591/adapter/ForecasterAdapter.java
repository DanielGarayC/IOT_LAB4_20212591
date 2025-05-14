package com.example.lab4_20212591.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_20212591.model.ForecastDay;
import com.example.lab4_20212591.R;

import java.util.List;

public class ForecasterAdapter extends RecyclerView.Adapter<ForecasterAdapter.ForecastViewHolder> {

    //Lista de pronóstico
    private List<ForecastDay> lista;

    public ForecasterAdapter(List<ForecastDay> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
        return new ForecastViewHolder(v);
    }

    //Se definen la data de los cards
    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {

        ForecastDay f = lista.get(position);
        holder.tvFecha.setText("Fecha: " + f.getDate());
        holder.tvTemp.setText("Máx: " + f.getDay().getMaxtemp_c() + "°C  |  Mín: " + f.getDay().getMintemp_c() + "°C");
        holder.tvCondicion.setText("Clima: " + f.getDay().getCondition().getText());
    }
    //Define cuantos elementos habrá en el RecyclerView :D
    @Override
    public int getItemCount() {
        return lista.size();
    }
    //Se representan cada item del RecyclerView
    public static class ForecastViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvTemp, tvCondicion;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvTemp = itemView.findViewById(R.id.tvTemp);
            tvCondicion = itemView.findViewById(R.id.tvCondicion);
        }
    }
}