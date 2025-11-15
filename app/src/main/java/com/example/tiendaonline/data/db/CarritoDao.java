package com.example.tiendaonline.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tiendaonline.data.model.ItemCarrito;
import com.example.tiendaonline.data.model.Productos;

import java.util.ArrayList;
import java.util.List;

public class CarritoDao {
    private Context context;
    private SQLiteDatabase db;
    private String table = "carrito";

    public CarritoDao(Context context, SQLiteDatabase db) {
        this.context = context;
        this.db = db;
    }

    public long agregarProducto(int idUsuario, int idProducto, int cantidad) {
        ContentValues values = new ContentValues();
        values.put("id_usuario", idUsuario);
        values.put("id_producto", idProducto);
        values.put("cantidad", cantidad);
        return db.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public List<ItemCarrito> obtenerCarrito(int idUsuario) {
        List<ItemCarrito> items = new ArrayList<>();
        String query = "SELECT c.id, c.cantidad, p.id as producto_id, p.nombre, p.descripcion, p.precio, p.imagenUrl " +
                "FROM carrito c INNER JOIN productos p ON c.id_producto = p.id " +
                "WHERE c.id_usuario = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idUsuario)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"));

                int productoId = cursor.getInt(cursor.getColumnIndexOrThrow("producto_id"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
                double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"));
                String imagenUrl = cursor.getString(cursor.getColumnIndexOrThrow("imagenUrl"));

                Productos producto = new Productos(productoId, nombre, precio, descripcion, imagenUrl);
                ItemCarrito item = new ItemCarrito(id, producto, cantidad);
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public int actualizarCantidad(int idCarrito, int nuevaCantidad) {
        ContentValues values = new ContentValues();
        values.put("cantidad", nuevaCantidad);
        return db.update(table, values, "id = ?", new String[]{String.valueOf(idCarrito)});
    }

    public int eliminarItem(int idCarrito) {
        return db.delete(table, "id = ?", new String[]{String.valueOf(idCarrito)});
    }

    public int limpiarCarrito(int idUsuario) {
        return db.delete(table, "id_usuario = ?", new String[]{String.valueOf(idUsuario)});
    }

    public double calcularTotal(int idUsuario) {
        double total = 0;
        String query = "SELECT SUM(c.cantidad * p.precio) as total " +
                "FROM carrito c INNER JOIN productos p ON c.id_producto = p.id " +
                "WHERE c.id_usuario = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idUsuario)});
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
        }
        cursor.close();
        return total;
    }
}