package com.example.lab4_20212591.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_20212591.FutureResponse;
import com.example.lab4_20212591.R;

import java.util.List;

public class FutureAdapter extends RecyclerView.Adapter<FutureAdapter.FutureViewHolder> {
    private List<FutureResponse.Hour> lista;

    public FutureAdapter(List<FutureResponse.Hour> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public FutureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_future, parent, false);
        return new FutureViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FutureViewHolder holder, int position) {
        FutureResponse.Hour h = lista.get(position);
        holder.tvHora.setText("Hora: " + h.getTime());
        holder.tvTemp.setText("Temp: " + h.getTemp_c() + "°C");
        holder.tvHumedad.setText("Humedad: " + h.getHumidity() + "%");
        holder.tvCondicion.setText("Clima: " + h.getCondition().getText());
        holder.tvLluvia.setText("Lluvia: " + h.getChance_of_rain() + "%");
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class FutureViewHolder extends RecyclerView.ViewHolder {
        TextView tvHora, tvTemp, tvHumedad, tvCondicion, tvLluvia;

        public FutureViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHora = itemView.findViewById(R.id.tvHora);
            tvTemp = itemView.findViewById(R.id.tvTemp);
            tvHumedad = itemView.findViewById(R.id.tvHumedad);
            tvCondicion = itemView.findViewById(R.id.tvCondicion);
            tvLluvia = itemView.findViewById(R.id.tvLluvia);
        }
    }
}