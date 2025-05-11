package com.example.lab4_20212591.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_20212591.R;
import com.example.lab4_20212591.model.Location;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<Location> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Location location);
    }

    public LocationAdapter(List<Location> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location loc = lista.get(position);
        holder.tvNombre.setText(loc.getName());
        holder.tvRegion.setText(loc.getRegion() + ", " + loc.getCountry());

        // ✅ Delegar el clic al listener
        holder.itemView.setOnClickListener(v -> listener.onItemClick(loc));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvRegion;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvRegion = itemView.findViewById(R.id.tvRegion);
        }
    }
}