package com.example.lab4_20212591.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import androidx.navigation.Navigation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_20212591.R;
import com.example.lab4_20212591.model.Location;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<Location> lista;
    private OnLocationClickListener listener;

    public LocationAdapter(List<Location> lista, OnLocationClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    public interface OnLocationClickListener {
        void onLocationClick(Location location);
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location loc = lista.get(position);
        holder.tvNombre.setText(loc.getName());
        holder.tvRegion.setText(loc.getRegion() + ", " + loc.getCountry());
        holder.tvId.setText("ID: " + loc.getId());
        holder.tvLatLng.setText("Lat: " + loc.getLat() + " | Lon: " + loc.getLon());

        holder.itemView.setOnClickListener(v -> listener.onLocationClick(loc));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvRegion, tvId, tvLatLng;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvRegion = itemView.findViewById(R.id.tvRegion);
            tvId = itemView.findViewById(R.id.tvId);
            tvLatLng = itemView.findViewById(R.id.tvLatLng);
        }
    }
}