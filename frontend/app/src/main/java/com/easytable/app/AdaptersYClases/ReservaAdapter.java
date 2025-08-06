package com.easytable.app.AdaptersYClases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easytable.app.R;

import java.util.List;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder> {

    private List<Reserva> listaReservas;
    private Context contexto;
    private OnReservaActionListener listener;
    private int tipoVistaUsuario;

    // Constantes para los tipos de vista
    private static final int VIEW_TYPE_CLIENTE = 1;
    private static final int VIEW_TYPE_GERENTE = 2;

    public interface OnReservaActionListener {
        void onModificarClick(Reserva reserva);
        void onCancelarClick(Reserva reserva);
    }

    public ReservaAdapter(Context contexto, List<Reserva> listaReservas, OnReservaActionListener listener, int tipoVistaUsuario) {
        this.contexto = contexto;
        this.listaReservas = listaReservas;
        this.listener = listener;
        this.tipoVistaUsuario = tipoVistaUsuario;
    }

    @Override
    public int getItemViewType(int position) {
        return tipoVistaUsuario;
    }


    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup padre, int viewType) {
        View vista;
        if (viewType == VIEW_TYPE_CLIENTE) {
            vista = LayoutInflater.from(padre.getContext()).inflate(R.layout.item_reserva, padre, false);
        } else { // viewType == VIEW_TYPE_GERENTE
            vista = LayoutInflater.from(padre.getContext()).inflate(R.layout.item_reservas_restaurante, padre, false);
        }
        return new ReservaViewHolder(vista, viewType); // Pasar el viewType al ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder titular, int posicion) {
        Reserva reserva = listaReservas.get(posicion);

        // La verificación del tipo de vista en onBindViewHolder está bien.
        // Accede al tipo de vista del ViewHolder directamente.
        if (titular.itemViewType == VIEW_TYPE_CLIENTE) { // Usar titular.itemViewType directamente
            // Rellenar datos para item_reserva (Cliente)
            if (titular.tvRestaurante != null) {
                if (reserva.getObjetoRestaurante() != null) {
                    titular.tvRestaurante.setText(reserva.getObjetoRestaurante().getNombre());
                } else {
                    titular.tvRestaurante.setText("Restaurante Desconocido");
                }
            }
            if (titular.imgLogo != null) {
                titular.imgLogo.setImageResource(R.drawable.logo_aldenaire_kitchen);
            }
        } else { // VIEW_TYPE_GERENTE
            // Rellenar datos para item_reserva_gerente (Gerente)
            if (titular.tvNombreCliente != null) {
                titular.tvNombreCliente.setText("Cliente: " + reserva.getEmailUsuario());
            }
            if (titular.tvEstado != null) {
                titular.tvEstado.setText("Estado: " + reserva.getEstado());
            }
            // Aquí puedes manejar el circuloEstado si es necesario, por ejemplo, cambiar su color
            // según el estado de la reserva.
            if (titular.circuloEstado != null) {
                // Ejemplo: cambiar color del círculo según el estado
                // if ("confirmada".equals(reserva.getEstado())) {
                //    titular.circuloEstado.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                // } else {
                //    titular.circuloEstado.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                // }
            }
        }

        // Datos comunes a ambos layouts
        titular.tvFecha.setText("Fecha: " + reserva.getFecha());
        titular.tvHora.setText("Hora: " + reserva.getHora());
        titular.tvComensales.setText("Comensales: " + String.valueOf(reserva.getComensales()));

        // Lógica de visibilidad y listeners para botones (común a ambos)
        if (reserva.estaActiva()) {
            titular.btnModificar.setVisibility(View.VISIBLE);
            titular.btnCancelar.setVisibility(View.VISIBLE);

            titular.btnModificar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onModificarClick(reserva);
                } else {
                    Toast.makeText(contexto, "Listener de Modificar no configurado.", Toast.LENGTH_SHORT).show();
                }
            });

            titular.btnCancelar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCancelarClick(reserva);
                } else {
                    Toast.makeText(contexto, "Listener de Cancelar no configurado.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            titular.btnModificar.setVisibility(View.GONE);
            titular.btnCancelar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listaReservas.size();
    }

    public static class ReservaViewHolder extends RecyclerView.ViewHolder {
        // Campos que pueden existir en AMBOS layouts
        TextView tvFecha, tvHora, tvComensales;
        Button btnModificar, btnCancelar;

        // Campos específicos de item_reserva (Cliente)
        TextView tvRestaurante;
        ImageView imgLogo;

        // Campos específicos de item_reserva_gerente (Gerente)
        TextView tvNombreCliente, tvEstado;
        View circuloEstado;

        // Almacena el tipo de vista que se le asignó a este ViewHolder en onCreateViewHolder
        private int itemViewType;

        public ReservaViewHolder(@NonNull View vistaElemento, int itemViewType) {
            super(vistaElemento);
            this.itemViewType = itemViewType; // Guardar el tipo de vista de este ViewHolder

            // Campos comunes
            tvFecha = vistaElemento.findViewById(R.id.tvFecha);
            tvHora = vistaElemento.findViewById(R.id.tvHora);
            tvComensales = vistaElemento.findViewById(R.id.tvComensales);
            btnModificar = vistaElemento.findViewById(R.id.btnModificar);
            btnCancelar = vistaElemento.findViewById(R.id.btnCancelar);

            // Campos específicos para CLIENTE
            if (itemViewType == VIEW_TYPE_CLIENTE) {
                tvRestaurante = vistaElemento.findViewById(R.id.tvNombreRestaurante);
                imgLogo = vistaElemento.findViewById(R.id.imgLogo);
            }
            // Campos específicos para GERENTE
            else if (itemViewType == VIEW_TYPE_GERENTE) {
                tvNombreCliente = vistaElemento.findViewById(R.id.tvNombreCliente);
                tvEstado = vistaElemento.findViewById(R.id.tvEstado);
                circuloEstado = vistaElemento.findViewById(R.id.circuloEstado);
            }
        }
    }

    public static final int TIPO_USUARIO_CLIENTE = VIEW_TYPE_CLIENTE;
    public static final int TIPO_USUARIO_GERENTE = VIEW_TYPE_GERENTE;
}