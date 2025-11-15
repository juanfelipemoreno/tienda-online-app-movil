package com.example.tiendaonline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tiendaonline.data.model.ItemCarrito;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {

    private List<ItemCarrito> items;
    private OnCarritoListener listener;

    public interface OnCarritoListener {
        void onCantidadCambiada(ItemCarrito item, int nuevaCantidad);
        void onEliminarItem(ItemCarrito item);
    }

    public CarritoAdapter(List<ItemCarrito> items, OnCarritoListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carrito, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemCarrito item = items.get(position);

        holder.txtNombre.setText(item.getProducto().getNombre());
        holder.txtPrecio.setText(String.format("$%.0f", item.getProducto().getPrecio()));
        holder.txtCantidad.setText(String.valueOf(item.getCantidad()));
        holder.txtSubtotal.setText(String.format("Subtotal: $%.0f", item.getSubtotal()));

        Glide.with(holder.itemView.getContext())
                .load(item.getProducto().getImagenUrl())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(holder.imgProducto);

        holder.btnMenos.setOnClickListener(v -> {
            int cantidad = item.getCantidad();
            if (cantidad > 1) {
                item.setCantidad(cantidad - 1);
                listener.onCantidadCambiada(item, cantidad - 1);
                notifyItemChanged(position);
            }
        });

        holder.btnMas.setOnClickListener(v -> {
            int cantidad = item.getCantidad();
            item.setCantidad(cantidad + 1);
            listener.onCantidadCambiada(item, cantidad + 1);
            notifyItemChanged(position);
        });

        holder.btnEliminar.setOnClickListener(v -> {
            listener.onEliminarItem(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProducto;
        TextView txtNombre, txtPrecio, txtCantidad, txtSubtotal;
        Button btnMenos, btnMas;
        ImageButton btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProducto = itemView.findViewById(R.id.imgProductoCarrito);
            txtNombre = itemView.findViewById(R.id.txtNombreCarrito);
            txtPrecio = itemView.findViewById(R.id.txtPrecioCarrito);
            txtCantidad = itemView.findViewById(R.id.txtCantidadCarrito);
            txtSubtotal = itemView.findViewById(R.id.txtSubtotal);
            btnMenos = itemView.findViewById(R.id.btnMenos);
            btnMas = itemView.findViewById(R.id.btnMas);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}