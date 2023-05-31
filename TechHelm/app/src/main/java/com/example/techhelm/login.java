package com.example.techhelm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class login extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Fazer a chamada à API para verificar o login
                new LoginTask().execute(email, password);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, registar.class);
                startActivity(intent);
            }
        });
    }
    private class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String password = params[1];

            OkHttpClient client = new OkHttpClient();
            String url = "https://rubenpassarinho.pt/login.php?email=" + email + "&password=" + password;

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String responseData = Objects.requireNonNull(response.body()).string();

                // Verificar a resposta da API
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        int userId = jsonObject.getInt("id");


                        UserData.getInstance().setUserId(userId);

                        Intent intent = new Intent(login.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Login falhou, exibir mensagem de erro
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(login.this, "Login falhou. Verifique suas credenciais.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}

