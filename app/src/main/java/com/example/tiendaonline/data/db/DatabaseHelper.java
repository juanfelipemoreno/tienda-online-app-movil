package com.example.tiendaonline.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "marketplace.db";
    private static final int DB_VERSION = 4;

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db){
        db.execSQL("CREATE TABLE usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, identificacion TEXT UNIQUE NOT NULL, nombre TEXT NOT NULL, password TEXT NOT NULL)");
        db.execSQL("CREATE TABLE clientes (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, email TEXT UNIQUE, telefono TEXT, direccion TEXT, latitud REAL, longitud REAL)");
        db.execSQL("CREATE TABLE productos (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, descripcion TEXT, precio REAL, imagenUrl TEXT)");
        db.execSQL("CREATE TABLE pedidos (id INTEGER PRIMARY KEY AUTOINCREMENT, id_cliente INTEGER, fecha TEXT)");
        db.execSQL("CREATE TABLE detalle_pedido (id INTEGER PRIMARY KEY AUTOINCREMENT, id_pedido INTEGER, id_producto INTEGER, cantidad INTEGER)");
        db.execSQL("CREATE TABLE carrito (id INTEGER PRIMARY KEY AUTOINCREMENT, id_usuario INTEGER, id_producto INTEGER, cantidad INTEGER, UNIQUE(id_usuario, id_producto))");

        insertarProductosIniciales(db);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS clientes");
        db.execSQL("DROP TABLE IF EXISTS productos");
        db.execSQL("DROP TABLE IF EXISTS pedidos");
        db.execSQL("DROP TABLE IF EXISTS detalle_pedido");
        db.execSQL("DROP TABLE IF EXISTS carrito");
        onCreate(db);
    }

    private void insertarProductosIniciales(SQLiteDatabase db) {

        db.execSQL("INSERT INTO productos (nombre, descripcion, precio, imagenUrl) VALUES" +
                "('Laptop Gamer', 'Laptop con RTX 4060 y 16GB RAM', 5200.00, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8')," +
                "('Audífonos Over-Ear', 'Audífonos de alta fidelidad con cancelación', 350.00, 'https://images.unsplash.com/photo-1510936111840-65e151ad71bb')," +
                "('Smartwatch Deportivo', 'Reloj inteligente con GPS y sensores avanzados', 420.00, 'https://images.unsplash.com/photo-1516574187841-cb9cc2ca948b')," +
                "('Mouse Gamer', 'Mouse RGB con sensor de 16000 DPI', 160.00, 'https://images.unsplash.com/photo-1584270354949-1a57f1baf8ef')," +
                "('Teclado Mecánico RGB', 'Switches Blue con iluminación ajustable', 280.00, 'https://images.unsplash.com/photo-1587202372775-a09c60a595a3')," +
                "('Cámara Profesional', 'Cámara mirrorless 24MP con lente 50mm', 3500.00, 'https://images.unsplash.com/photo-1519183071298-a2962be96d33')," +
                "('Silla Gamer', 'Silla ergonómica reclinable 180°', 999.90, 'https://images.unsplash.com/photo-1628265803937-6aaf0cb7a438')," +
                "('Parlante Bluetooth', 'Bocina 360° resistente al agua', 230.00, 'https://images.unsplash.com/photo-1585386959984-a4155224a1ad')" +
                ";");
    }

}
