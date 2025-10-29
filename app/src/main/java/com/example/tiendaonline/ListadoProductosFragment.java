package com.example.tiendaonline;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class ListadoProductosFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private List<Producto> listaProductos;

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

        listaProductos = new ArrayList<>();
        listaProductos.add(new Producto("Camiseta Nike", "Deportiva y cómoda", 120000, R.drawable.ic_launcher_foreground));
        listaProductos.add(new Producto("Pantalón Adidas", "Original, color negro", 180000, R.drawable.ic_launcher_foreground));
        listaProductos.add(new Producto("Zapatos Puma", "Para correr", 250000, R.drawable.ic_launcher_foreground));

        // Creamos y asignamos el adaptador
        adapter = new ProductoAdapter(listaProductos);
        recyclerView.setAdapter(adapter);

        return view;
    }
}