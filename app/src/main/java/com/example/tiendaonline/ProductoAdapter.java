package com.example.tiendaonline;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendaonline.data.db.CarritoDao;
import com.example.tiendaonline.data.db.DatabaseHelper;
import com.example.tiendaonline.data.model.Productos;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    private List<Productos> listaProductos;
    private Context context;
    private OnProductoListener listener;

    public interface OnProductoListener {
        void onEditarProducto(Productos producto);
        void onEliminarProducto(Productos producto);
    }

    public ProductoAdapter(List<Productos> listaProductos, OnProductoListener listener) {
        this.listaProductos = listaProductos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Productos producto = listaProductos.get(position);
        holder.txtNombreProducto.setText(producto.getNombre());
        holder.txtDescripcion.setText(producto.getDescripcion());
        holder.txtPrecio.setText(String.format("$ %.0f", producto.getPrecio()));


        Glide.with(holder.itemView.getContext())
                .load(producto.getImagenUrl())               // puede ser URL o ruta local
                .placeholder(R.drawable.ic_placeholder)       // imagen mientras carga
                .error(R.drawable.ic_placeholder)                   // si falla la carga
                .into(holder.imgProducto);

        holder.btnAgregarCarrito.setOnClickListener(v -> {
            agregarAlCarrito(producto);
        });

        holder.btnEditar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditarProducto(producto);
            }
        });

        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEliminarProducto(producto);
            }
        });
    }

    private void agregarAlCarrito(Productos producto) {
        SharedPreferences prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(context, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        CarritoDao carritoDao = new CarritoDao(context, db);

        long result = carritoDao.agregarProducto(userId, producto.getId(), 1);

        if (result > 0) {
            Toast.makeText(context, "✅ Producto agregado al carrito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "❌ Error al agregar al carrito", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public void actualizarLista(List<Productos> nuevaLista) {
        this.listaProductos = nuevaLista;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreProducto, txtDescripcion, txtPrecio;
        ImageView imgProducto;
        Button btnAgregarCarrito;
        ImageButton btnEditar, btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombreProducto = itemView.findViewById(R.id.txtNombreProducto);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            imgProducto = itemView.findViewById(R.id.imgProducto);
            btnAgregarCarrito = itemView.findViewById(R.id.btnAgregarCarrito);
            btnEditar = itemView.findViewById(R.id.btnEditarProducto);
            btnEliminar = itemView.findViewById(R.id.btnEliminarProducto);
        }
    }
}