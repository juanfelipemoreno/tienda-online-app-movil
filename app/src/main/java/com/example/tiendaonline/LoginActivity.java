package com.example.tiendaonline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiendaonline.data.db.DatabaseHelper;
import com.example.tiendaonline.data.db.UsuariosDao;
import com.example.tiendaonline.data.model.Usuarios;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText usuario;
    private EditText password;
    private UsuariosDao usuariosDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verificar si ya hay sesión activa
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

        if (isLoggedIn) {
            // Si ya está logueado, ir directo a MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        usuariosDao = new UsuariosDao(this,db);

        usuario = findViewById(R.id.inputUsuario);
        password = findViewById(R.id.inputPassword);

        Button btnLogin = findViewById(R.id.btnRegister);
        btnLogin.setOnClickListener(v -> {
            validateLogin();
        });

        TextView btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        btnCrearCuenta.setOnClickListener(v->{
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void validateLogin(){
        String usuarioData = usuario.getText().toString().trim();
        String passwordData = password.getText().toString().trim();

        if (usuarioData.isEmpty() || passwordData.isEmpty() ) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Usuarios> u = usuariosDao.obtenerPorNro(usuarioData);

        if (u.isEmpty()) {
            Toast.makeText(this, "No hay usuarios registrado con este documento", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuarios user = usuariosDao.validarLogin(usuarioData,passwordData);

        if (user != null) {
            // Guardar sesión en SharedPreferences
            SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("is_logged_in", true);
            editor.putInt("user_id", user.getId());
            editor.putString("user_name", user.getNombre());
            editor.putString("user_identificacion", user.getIdentificacion());
            editor.apply();

            Toast.makeText(this, "✅ Bienvenido", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}