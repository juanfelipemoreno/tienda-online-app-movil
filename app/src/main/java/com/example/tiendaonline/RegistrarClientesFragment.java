package com.example.tiendaonline;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiendaonline.data.db.ClientesDao;
import com.example.tiendaonline.data.db.DatabaseHelper;
import com.example.tiendaonline.data.model.Clientes;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class RegistrarClientesFragment extends Fragment {

    private EditText etNombre, etCorreo, etTelefono, etDireccion;
    private TextView tvUbicacion;
    private Button btnRegistrarCliente, btnObtenerUbicacion;
    private ClientesDao clientesDao;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitud = 0;
    private double longitud = 0;

    private static final int LOCATION_PERMISSION_CODE = 100;

    public RegistrarClientesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrar_clientes, container, false);

        // Inicializar vistas
        etNombre = view.findViewById(R.id.etNombre);
        etCorreo = view.findViewById(R.id.etCorreo);
        etTelefono = view.findViewById(R.id.etTelefono);
        etDireccion = view.findViewById(R.id.etDireccion);
        tvUbicacion = view.findViewById(R.id.tvUbicacion);
        btnRegistrarCliente = view.findViewById(R.id.btnRegistrarCliente);
        btnObtenerUbicacion = view.findViewById(R.id.btnObtenerUbicacion);

        DatabaseHelper helper = new DatabaseHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        clientesDao = new ClientesDao(getContext(), db);

        btnObtenerUbicacion.setOnClickListener(v -> obtenerUbicacion());

        btnRegistrarCliente.setOnClickListener(v -> registrarCliente());

        return view;
    }

    private void obtenerUbicacion() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Solicitar permisos
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitud = location.getLatitude();
                            longitud = location.getLongitude();
                            tvUbicacion.setText(String.format("📍 Ubicación: %.6f, %.6f", latitud, longitud));
                            Toast.makeText(getContext(), "✅ Ubicación obtenida", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void registrarCliente() {
        String nombre = etNombre.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();

        if (nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(getContext(), "Por favor completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!correo.contains("@")) {
            Toast.makeText(getContext(), "Por favor ingresa un correo válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si el email ya existe
        if (clientesDao.obtenerPorEmail(correo) != null) {
            Toast.makeText(getContext(), "Ya existe un cliente con este correo", Toast.LENGTH_SHORT).show();
            return;
        }

        Clientes nuevoCliente = new Clientes();
        nuevoCliente.setNombre(nombre);
        nuevoCliente.setEmail(correo);
        nuevoCliente.setTelefono(telefono);
        nuevoCliente.setDireccion(direccion);
        nuevoCliente.setLatitud(latitud);
        nuevoCliente.setLongitud(longitud);

        long id = clientesDao.insertar(nuevoCliente);

        if (id > 0) {
            Toast.makeText(getContext(), "✅ Cliente registrado exitosamente", Toast.LENGTH_LONG).show();
            limpiarCampos();
        } else {
            Toast.makeText(getContext(), "❌ Error al registrar cliente", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etCorreo.setText("");
        etTelefono.setText("");
        etDireccion.setText("");
        tvUbicacion.setText("📍 No hay ubicación registrada");
        latitud = 0;
        longitud = 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacion();
            } else {
                Toast.makeText(getContext(), "Permisos de ubicación denegados", Toast.LENGTH_SHORT).show();
            }
        }
    }
}