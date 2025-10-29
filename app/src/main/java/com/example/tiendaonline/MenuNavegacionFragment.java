package com.example.tiendaonline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MenuNavegacionFragment extends Fragment {

    Button btnListarProductos, btnCarrito, btnRegistroClientes;

    public MenuNavegacionFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_navegacion, container, false);

        btnListarProductos = view.findViewById(R.id.btnListarProductos);
        btnCarrito = view.findViewById(R.id.btnCarrito);
        btnRegistroClientes = view.findViewById(R.id.btnRegistroClientes);

        // Navegar al fragment de ListarProductos
        btnListarProductos.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.ListadoProductosFragment);
        });

        // Si también quieres que los otros botones naveguen a otros fragments:
        btnCarrito.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.carritoFragment);
        });

        btnRegistroClientes.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.registrarClientesFragment);
        });

        return view;
    }
}
