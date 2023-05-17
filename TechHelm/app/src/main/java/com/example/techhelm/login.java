package com.example.techhelm;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.techhelm.downloadtasks.loginbackgroung;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;

public class login extends AppCompatActivity {
    EditText usernameField, passwordField;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = findViewById(R.id.inputUsername);
        passwordField = findViewById(R.id.inputPassword);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                loginbackgroung task = new loginbackgroung();
                JSONObject loginjson = null;
                try {
                    // Aqui suponho que a task.execute retorne algum JSONObject com as informações do usuário após o login.
                    // Você terá que substituir "url_to_login_endpoint" pela URL do endpoint da API que lida com o login.
                    loginjson = task.execute("url_to_login_endpoint", username, password).get();
                    if (loginjson != null) {
                        // Aqui você pode processar o JSONObject como quiser.
                        // Por exemplo, se o JSONObject contiver um campo "success" que indica se o login foi bem-sucedido:
                        if (loginjson.getBoolean("success")) {
                            Toast.makeText(login.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                            // Aqui você pode iniciar a próxima atividade, como a atividade Index.
                            // Intent intent = new Intent(login.this, Index.class);
                            // startActivity(intent);
                        } else {
                            Toast.makeText(login.this, "Falha no login.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (ExecutionException | InterruptedException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
