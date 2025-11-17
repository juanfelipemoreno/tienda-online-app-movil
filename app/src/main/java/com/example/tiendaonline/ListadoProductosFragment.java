package com.example.tiendaonline;

import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tiendaonline.data.db.DatabaseHelper;
import com.example.tiendaonline.data.db.ProductosDao;
import com.example.tiendaonline.data.model.Productos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ListadoProductosFragment extends Fragment implements ProductoAdapter.OnProductoListener{

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private ProductosDao productosDao;
    private FloatingActionButton fabAgregar;

    public ListadoProductosFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_listado_productos, container, false);

        recyclerView = view.findViewById(R.id.recyclerProductos);
        fabAgregar = view.findViewById(R.id.fabAgregarProducto);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseHelper helper = new DatabaseHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        productosDao = new ProductosDao(getContext(),db);

        cargarProductos();

        fabAgregar.setOnClickListener(v -> mostrarDialogoAgregar());

        return view;
    }

    private void cargarProductos() {
        List<Productos> listaProductos = productosDao.listar();
        adapter = new ProductoAdapter(listaProductos, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEditarProducto(Productos producto) {
        mostrarDialogoEditar(producto);
    }

    @Override
    public void onEliminarProducto(Productos producto) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar Producto")
                .setMessage("¿Estás seguro de eliminar '" + producto.getNombre() + "'?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    int resultado = productosDao.eliminar(producto.getId());

                    if (resultado > 0) {
                        Toast.makeText(getContext(), "✅ Producto eliminado", Toast.LENGTH_SHORT).show();
                        cargarProductos();
                    } else {
                        Toast.makeText(getContext(), "❌ Error al eliminar", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoAgregar() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_producto, null);

        EditText etNombre = dialogView.findViewById(R.id.etNombreProducto);
        EditText etDescripcion = dialogView.findViewById(R.id.etDescripcionProducto);
        EditText etPrecio = dialogView.findViewById(R.id.etPrecioProducto);
        EditText etImagenUrl = dialogView.findViewById(R.id.etImagenUrlProducto);

        // Usar androidx.appcompat.app.AlertDialog en lugar de android.app.AlertDialog
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Agregar Nuevo Producto");
        builder.setView(dialogView);
        builder.setPositiveButton("Guardar", null);
        builder.setNegativeButton("Cancelar", null);

        androidx.appcompat.app.AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button btnGuardar = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
            Button btnCancelar = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE);

            btnGuardar.setOnClickListener(v -> {
                String nombre = etNombre.getText().toString().trim();
                String descripcion = etDescripcion.getText().toString().trim();
                String precioStr = etPrecio.getText().toString().trim();
                String imagenUrl = etImagenUrl.getText().toString().trim();

                if (nombre.isEmpty() || precioStr.isEmpty()) {
                    Toast.makeText(getContext(), "Nombre y precio son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double precio = Double.parseDouble(precioStr);

                    Productos nuevoProducto = new Productos(nombre, precio, descripcion, imagenUrl);
                    long resultado = productosDao.insertar(nuevoProducto);

                    if (resultado > 0) {
                        Toast.makeText(getContext(), "✅ Producto agregado", Toast.LENGTH_SHORT).show();
                        cargarProductos();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "❌ Error al agregar", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Precio inválido", Toast.LENGTH_SHORT).show();
                }
            });

            btnCancelar.setOnClickListener(v -> dialog.dismiss());
        });

        dialog.show();
    }

    private void mostrarDialogoEditar(Productos producto) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_producto, null);

        EditText etNombre = dialogView.findViewById(R.id.etNombreProducto);
        EditText etDescripcion = dialogView.findViewById(R.id.etDescripcionProducto);
        EditText etPrecio = dialogView.findViewById(R.id.etPrecioProducto);
        EditText etImagenUrl = dialogView.findViewById(R.id.etImagenUrlProducto);

        // Rellenar con datos actuales
        etNombre.setText(producto.getNombre());
        etDescripcion.setText(producto.getDescripcion());
        etPrecio.setText(String.valueOf(producto.getPrecio()));
        etImagenUrl.setText(producto.getImagenUrl());

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Editar Producto")
                .setView(dialogView)
                .setPositiveButton("Actualizar", null)
                .setNegativeButton("Cancelar", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button btnActualizar = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnActualizar.setOnClickListener(v -> {
                String nombre = etNombre.getText().toString().trim();
                String descripcion = etDescripcion.getText().toString().trim();
                String precioStr = etPrecio.getText().toString().trim();
                String imagenUrl = etImagenUrl.getText().toString().trim();

                if (nombre.isEmpty() || precioStr.isEmpty()) {
                    Toast.makeText(getContext(), "Nombre y precio son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double precio = Double.parseDouble(precioStr);

                    producto.setNombre(nombre);
                    producto.setDescripcion(descripcion);
                    producto.setPrecio(precio);
                    producto.setImagenUrl(imagenUrl);

                    int resultado = productosDao.actualizar(producto);

                    if (resultado > 0) {
                        Toast.makeText(getContext(), "✅ Producto actualizado", Toast.LENGTH_SHORT).show();
                        cargarProductos();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "❌ Error al actualizar", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Precio inválido", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }
}