package com.example.tiendaonline.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.tiendaonline.data.model.Productos;

import java.util.ArrayList;
import java.util.List;

public class ProductosDao {
    private Context context;
    private SQLiteDatabase db;
    private String table = "productos";

    public ProductosDao(Context context, SQLiteDatabase db) {
        this.db = db;
        this.context = context;
    }

    public long insertar(Productos producto) {
        ContentValues values = new ContentValues();
        values.put("nombre", producto.getNombre());
        values.put("precio", producto.getPrecio());
        values.put("descripcion", producto.getDescripcion());
        values.put("imagenUrl", producto.getImagenUrl());
        return db.insert(table, null, values);
    }

    public List<Productos> listar() {
        List<Productos> lista = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + table, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
                String imagenUrl = cursor.getString(cursor.getColumnIndexOrThrow("imagenUrl"));

                lista.add(new Productos(id, nombre, precio,descripcion, imagenUrl));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public int actualizar(Productos producto) {

        ContentValues values = new ContentValues();
        values.put("nombre", producto.getNombre());
        values.put("precio", producto.getPrecio());
        values.put("descripcion", producto.getDescripcion());
        values.put("imagenUrl", producto.getImagenUrl());

        int filasActualizadas = db.update(table, values, "id = ?",
                new String[]{String.valueOf(producto.getId())});

        return filasActualizadas;
    }

    public int eliminar(int id) {

        int filasEliminadas = db.delete(table, "id = ?", new String[]{String.valueOf(id)});

        return filasEliminadas;
    }

    public Productos obtenerPorId(int id) {

        Productos producto = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + table + " WHERE id = ?",
                new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"));
            String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
            String imagenUrl = cursor.getString(cursor.getColumnIndexOrThrow("imagenUrl"));

            producto = new Productos(id, nombre, precio, descripcion, imagenUrl);
            Log.i("TiendaOnline_ProductosDao","Producto encontrado: " + nombre);
        } else {
            Log.w("TiendaOnline_ProductosDao", "⚠️ Producto no encontrado con ID: " + id);
        }

        cursor.close();
        return producto;
    }

    public List<Productos> buscarPorNombre(String nombre) {

        List<Productos> lista = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + table + " WHERE nombre LIKE ?",
                new String[]{"%" + nombre + "%"});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nombreProd = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
                String imagenUrl = cursor.getString(cursor.getColumnIndexOrThrow("imagenUrl"));

                lista.add(new Productos(id, nombreProd, precio, descripcion, imagenUrl));
            } while (cursor.moveToNext());
        }

        cursor.close();
        Log.i("TiendaOnline_ProductosDao", "Productos encontrados con '" + nombre + "': " + lista.size());

        return lista;
    }
}
