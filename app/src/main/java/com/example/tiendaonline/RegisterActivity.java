package com.example.tiendaonline;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiendaonline.data.db.DatabaseHelper;
import com.example.tiendaonline.data.db.UsuariosDao;
import com.example.tiendaonline.data.model.Usuarios;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText nombre;
    private EditText nro_identificacion;
    private EditText password;
    private EditText password2;
    private Button btnRegister;
    private UsuariosDao usuariosDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        usuariosDao = new UsuariosDao(this,db);

        nombre = findViewById(R.id.inputUsuario);
        nro_identificacion = findViewById(R.id.inputNroIdentificacion);
        password = findViewById(R.id.inputPassword);
        password2 = findViewById(R.id.inputPassword2);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v->validateRegister());

        TextView btnLogin = findViewById(R.id.BtnLogin);

        btnLogin.setOnClickListener(v->{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void validateRegister(){
        String txtnombre = nombre.getText().toString().trim();
        String txtnroIdentificacion = nro_identificacion.getText().toString().trim();
        String txtpassword = password.getText().toString().trim();
        String txtpassword2 = password2.getText().toString().trim();

        if (txtnombre.isEmpty() || txtnroIdentificacion.isEmpty() || txtpassword.isEmpty() || txtpassword2.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!txtpassword.equals(txtpassword2)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Usuarios> u = usuariosDao.obtenerPorNro(txtnroIdentificacion);

        if (!u.isEmpty()) {
            Toast.makeText(this, "Ya hay un usuario registrado con este documento", Toast.LENGTH_SHORT).show();
            return;
        }
        String passwordhash = UsuariosDao.hashPassword(txtpassword);

        Usuarios nuevoUsuario = new Usuarios();
        nuevoUsuario.setIdentificacion(txtnroIdentificacion);
        nuevoUsuario.setNombre(txtnombre);
        nuevoUsuario.setPassword(passwordhash);

        long id = usuariosDao.insertar(nuevoUsuario);

        if (id > 0) {
            Toast.makeText(this, "✅ Registro exitoso", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "❌ Error al registrar", Toast.LENGTH_SHORT).show();
        }
    }
}