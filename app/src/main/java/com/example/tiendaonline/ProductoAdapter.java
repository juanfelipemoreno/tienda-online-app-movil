package com.example.tiendaonline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    private List<Producto> listaProductos;

    public ProductoAdapter(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);
        holder.txtNombreProducto.setText(producto.getNombre());
        holder.txtDescripcion.setText(producto.getDescripcion());
        holder.txtPrecio.setText("$" + producto.getPrecio());
        holder.imgProducto.setImageResource(producto.getImagenResId());
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreProducto, txtDescripcion, txtPrecio;
        ImageView imgProducto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombreProducto = itemView.findViewById(R.id.txtNombreProducto);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            imgProducto = itemView.findViewById(R.id.imgProducto);
        }
    }
}