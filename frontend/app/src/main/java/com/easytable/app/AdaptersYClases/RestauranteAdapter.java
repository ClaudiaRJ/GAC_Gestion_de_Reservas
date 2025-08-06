package com.easytable.app.AdaptersYClases;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easytable.app.R;

import java.util.List;

public class RestauranteAdapter extends RecyclerView.Adapter<RestauranteAdapter.RestauranteViewHolder> {

    private List<Restaurante> listaRestaurantes;
    private final OnRestauranteClickListener listener;

    public RestauranteAdapter(List<Restaurante> listaRestaurantes, OnRestauranteClickListener listener) {
        this.listaRestaurantes = listaRestaurantes;
        this.listener = listener;
    }

    public interface OnRestauranteClickListener {
        void onRestauranteClick(Restaurante restaurante);
    }

    @NonNull
    @Override
    public RestauranteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurante, parent, false);
        return new RestauranteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestauranteViewHolder holder, int position) {
        Restaurante restaurante = listaRestaurantes.get(position);
        holder.tvNombre.setText(restaurante.getNombreRestaurante());
        holder.tvDescripcion.setText(restaurante.getDescripcion());
        holder.imgRestaurante.setImageResource(restaurante.getImagenResId());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRestauranteClick(restaurante);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaRestaurantes.size();
    }

    // Método para actualizar la lista en el adaptador
    public void updateList(List<Restaurante> newList) {
        this.listaRestaurantes = newList;
        notifyDataSetChanged();  // Notificar que los datos han cambiado
    }

    public static class RestauranteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDescripcion;
        ImageView imgRestaurante;

        public RestauranteViewHolder(View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            imgRestaurante = itemView.findViewById(R.id.imgRestaurante);
        }
    }
}
