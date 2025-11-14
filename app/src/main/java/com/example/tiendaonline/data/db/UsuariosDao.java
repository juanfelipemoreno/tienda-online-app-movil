package com.example.tiendaonline.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.tiendaonline.data.model.Usuarios;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class UsuariosDao {
    private Context context;
    private SQLiteDatabase db;
    private String table;
    public UsuariosDao(Context context, SQLiteDatabase db) {
        this.context = context;
        this.db = db;
        this.table = "usuarios";
    }
    public Usuarios validarLogin(String nro_identificacion, String password){
        String passwordHash = hashPassword(password);
        Usuarios usuario = null;

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + table + " WHERE identificacion = ?",
                new String[]{nro_identificacion}
        );


        if(cursor.moveToFirst()){
            String storedHash = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            if (storedHash.equals(passwordHash)) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String identificacion = cursor.getString(cursor.getColumnIndexOrThrow("identificacion"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));

                usuario = new Usuarios();
                usuario.setId(id);
                usuario.setIdentificacion(identificacion);
                usuario.setNombre(nombre);
                usuario.setPassword(storedHash);

                Toast.makeText(context, "✅ Login exitoso", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(context, "❌ Contraseña incorrecta", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        return usuario;
    }

    public List<Usuarios> obtenerPorNro(String nro_identificacion){
        List<Usuarios> lista = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+this.table+" WHERE identificacion = ?",
                new String[]{nro_identificacion});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String identificacion = cursor.getString(cursor.getColumnIndexOrThrow("identificacion"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));

                Usuarios usuario = new Usuarios();
                usuario.setId(id);
                usuario.setIdentificacion(identificacion);
                usuario.setNombre(nombre);
                usuario.setPassword(password);

                lista.add(usuario);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public long insertar(Usuarios usuario) {
        ContentValues values = new ContentValues();
        values.put("identificacion", usuario.getIdentificacion());
        values.put("nombre", usuario.getNombre());
        values.put("password", usuario.getPassword());

        return db.insert(this.table, null, values);
    }
}
