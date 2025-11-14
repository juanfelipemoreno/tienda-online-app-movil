package com.example.tiendaonline.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
}
