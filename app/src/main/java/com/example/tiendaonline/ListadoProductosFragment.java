package com.example.tiendaonline;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tiendaonline.data.db.DatabaseHelper;
import com.example.tiendaonline.data.db.ProductosDao;
import com.example.tiendaonline.data.db.UsuariosDao;
import com.example.tiendaonline.data.model.Productos;

import java.util.ArrayList;
import java.util.List;

public class ListadoProductosFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private List<Producto> listaProductos;
    private ProductosDao productosDao;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseHelper helper = new DatabaseHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        productosDao = new ProductosDao(getContext(),db);

        List<Productos> listaProductos = productosDao.listar();
        System.out.println(listaProductos);
        // Creamos y asignamos el adaptador
        adapter = new ProductoAdapter(listaProductos);
        recyclerView.setAdapter(adapter);

        return view;
    }
}