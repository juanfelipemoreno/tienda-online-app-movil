package com.example.tiendaonline;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiendaonline.data.db.CarritoDao;
import com.example.tiendaonline.data.db.DatabaseHelper;
import com.example.tiendaonline.data.model.ItemCarrito;

import java.util.List;

public class CarritoFragment extends Fragment {

    private RecyclerView recyclerCarrito;
    private TextView txtTotal;
    private Button btnFinalizarCompra;
    private CarritoAdapter adapter;
    private CarritoDao carritoDao;
    private int userId;

    public CarritoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carrito, container, false);

        SharedPreferences prefs = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(getContext(), "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
            return view;
        }

        recyclerCarrito = view.findViewById(R.id.recyclerCarrito);
        txtTotal = view.findViewById(R.id.txtTotal);
        btnFinalizarCompra = view.findViewById(R.id.btnFinalizarCompra);

        recyclerCarrito.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseHelper helper = new DatabaseHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        carritoDao = new CarritoDao(getContext(), db);

        cargarCarrito();

        btnFinalizarCompra.setOnClickListener(v -> finalizarCompra());

        return view;
    }

    private void cargarCarrito() {
        List<ItemCarrito> items = carritoDao.obtenerCarrito(userId);

        if (items.isEmpty()) {
            Toast.makeText(getContext(), "Tu carrito está vacío", Toast.LENGTH_SHORT).show();
            txtTotal.setText("Total: $0");
            return;
        }

        adapter = new CarritoAdapter(items, new CarritoAdapter.OnCarritoListener() {
            @Override
            public void onCantidadCambiada(ItemCarrito item, int nuevaCantidad) {
                carritoDao.actualizarCantidad(item.getId(), nuevaCantidad);
                actualizarTotal();
            }

            @Override
            public void onEliminarItem(ItemCarrito item) {
                carritoDao.eliminarItem(item.getId());
                cargarCarrito();
                Toast.makeText(getContext(), "Producto eliminado", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerCarrito.setAdapter(adapter);
        actualizarTotal();
    }

    private void actualizarTotal() {
        double total = carritoDao.calcularTotal(userId);
        txtTotal.setText(String.format("Total: $%.0f", total));
    }

    private void finalizarCompra() {
        List<ItemCarrito> items = carritoDao.obtenerCarrito(userId);

        if (items.isEmpty()) {
            Toast.makeText(getContext(), "El carrito está vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aquí se puede implementar la lógica para crear el pedido
        carritoDao.limpiarCarrito(userId);
        Toast.makeText(getContext(), "✅ Compra realizada exitosamente", Toast.LENGTH_LONG).show();
        cargarCarrito();
    }
}