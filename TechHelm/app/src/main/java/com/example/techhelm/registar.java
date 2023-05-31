package com.example.techhelm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class registar extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextEmail;
    private EditText editTextTelemovel;
    private EditText editTextPassword;
    private Button buttonRegistar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);

        editTextNome = findViewById(R.id.editTextNome);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextTelemovel = findViewById(R.id.editTextTelemovel);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegistar = findViewById(R.id.buttonRegistar);

        buttonRegistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = editTextNome.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String telemovel = editTextTelemovel.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Fazer a chamada à API para registrar o usuário
                new RegistarTask().execute(nome, email, telemovel, password);
            }
        });
    }

    private class RegistarTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String nome = params[0];
            String email = params[1];
            String telemovel = params[2];
            String password = params[3];

            OkHttpClient client = new OkHttpClient();
            String url = "https://rubenpassarinho.pt/registar.php";

            try {
                // Codificar os parâmetros na URL
                String encodedUrl = url + "?nome=" + URLEncoder.encode(nome, "UTF-8") +
                        "&email=" + URLEncoder.encode(email, "UTF-8") +
                        "&telemovel=" + URLEncoder.encode(telemovel, "UTF-8") +
                        "&password=" + URLEncoder.encode(password, "UTF-8");

                Request request = new Request.Builder()
                        .url(encodedUrl)
                        .post(new FormBody.Builder().build())
                        .build();

                Response response = client.newCall(request).execute();
                String responseData = Objects.requireNonNull(response.body()).string();

                // Verificar a resposta da API
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        int id = jsonObject.getInt("id");
                        return String.valueOf(id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    Log.e("RegistarActivity", "Erro ao registrar usuário: " + response.code());
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    int id = Integer.parseInt(result);

                    Toast.makeText(registar.this, "Registrado com sucesso", Toast.LENGTH_SHORT).show();
                    UserData.getInstance().setUserId(id);

                    Intent intent = new Intent(registar.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(registar.this, "Erro ao registrar usuário", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(registar.this, "Erro ao registrar usuário", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
