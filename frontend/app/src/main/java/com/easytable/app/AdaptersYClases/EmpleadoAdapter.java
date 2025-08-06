package com.easytable.app.AdaptersYClases;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.easytable.app.R; // Asegúrate de importar el paquete R de tu proyecto

import java.util.List;

public class EmpleadoAdapter extends RecyclerView.Adapter<EmpleadoAdapter.EmpleadoViewHolder> {

    private final List<Empleado> listaEmpleados;
    // Puedes añadir interfaces de listener si necesitas manejar clics en los botones desde la actividad
    private OnItemClickListener listener;

    // Interfaz para manejar clics en el adaptador
    public interface OnItemClickListener {
        void onModificarClick(int position);
        void onEliminarClick(int position);
    }

    // Método para establecer el listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Constructor
    public EmpleadoAdapter(List<Empleado> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    // Se infla el layout de cada ítem del RecyclerView
    @Override
    public EmpleadoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // ¡Cambiado para inflar tu layout personalizado item_empleado.xml!
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_empleado, parent, false);
        return new EmpleadoViewHolder(itemView, listener); // Pasar el listener al ViewHolder
    }

    // Se asigna el dato a cada ViewHolder
    @Override
    public void onBindViewHolder(EmpleadoViewHolder holder, int position) {
        Empleado empleado = listaEmpleados.get(position);
        holder.tvNombrePersona.setText(empleado.getNombre() + " " + empleado.getApellido());
        // Aquí podrías configurar el email si tuvieras un TextView para ello en tu layout
        // Por ejemplo: holder.tvEmail.setText(empleado.getEmail());
        // La imagen de perfil es estática en tu layout, así que no necesita ser configurada aquí a menos que quieras que sea dinámica.
    }

    // Devuelve el número total de elementos en la lista
    @Override
    public int getItemCount() {
        return listaEmpleados.size();
    }

    // Clase interna para el ViewHolder
    public static class EmpleadoViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgLogo;
        public TextView tvNombrePersona;
        public Button btnModificar;
        public Button btnEliminar;

        public EmpleadoViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            // Inicializamos las vistas con los IDs de tu item_empleado.xml
            imgLogo = itemView.findViewById(R.id.imgLogo);
            tvNombrePersona = itemView.findViewById(R.id.tvNombrePersona);
            btnModificar = itemView.findViewById(R.id.btnModificar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);

            // Configuramos los listeners para los botones
            btnModificar.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onModificarClick(position);
                    }
                }
            });

            btnEliminar.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onEliminarClick(position);
                    }
                }
            });
        }
    }
}
