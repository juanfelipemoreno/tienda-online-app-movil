package com.example.tiendaonline.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tiendaonline.data.model.Clientes;

import java.util.ArrayList;
import java.util.List;

public class ClientesDao {
    private Context context;
    private SQLiteDatabase db;
    private String table = "clientes";

    public ClientesDao(Context context, SQLiteDatabase db) {
        this.context = context;
        this.db = db;
    }

    public long insertar(Clientes cliente) {
        ContentValues values = new ContentValues();
        values.put("nombre", cliente.getNombre());
        values.put("email", cliente.getEmail());
        values.put("telefono", cliente.getTelefono());
        values.put("direccion", cliente.getDireccion());
        values.put("latitud", cliente.getLatitud());
        values.put("longitud", cliente.getLongitud());
        return db.insert(table, null, values);
    }

    public List<Clientes> listar() {
        List<Clientes> lista = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + table, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"));
                String direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion"));
                double latitud = cursor.getDouble(cursor.getColumnIndexOrThrow("latitud"));
                double longitud = cursor.getDouble(cursor.getColumnIndexOrThrow("longitud"));

                lista.add(new Clientes(id, nombre, email, telefono, direccion, latitud, longitud));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public Clientes obtenerPorEmail(String email) {
        Clientes cliente = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + table + " WHERE email = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            String emailDb = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"));
            String direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion"));
            double latitud = cursor.getDouble(cursor.getColumnIndexOrThrow("latitud"));
            double longitud = cursor.getDouble(cursor.getColumnIndexOrThrow("longitud"));

            cliente = new Clientes(id, nombre, emailDb, telefono, direccion, latitud, longitud);
        }
        cursor.close();
        return cliente;
    }
}