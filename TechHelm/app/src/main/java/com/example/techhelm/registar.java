package com.example.techhelm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class registar extends AppCompatActivity {

    private EditText editTextNome, editTextEmail, editTextTelemovel, editTextPassword;
    private Button buttonRegistar;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);

        editTextNome = findViewById(R.id.editTextNome);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextTelemovel = findViewById(R.id.editTextTelemovel);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegistar = findViewById(R.id.buttonRegistar);

        requestQueue = Volley.newRequestQueue(this);

        buttonRegistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String url = "https://rubenpassarinho.pt/registar.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int id = jsonObject.getInt("id");
                            Toast.makeText(registar.this, "Usuário registrado com sucesso. ID: " + id, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(registar.this, "Erro ao registrar usuário", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(registar.this, "Erro ao registrar usuário", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nome", editTextNome.getText().toString());
                params.put("email", editTextEmail.getText().toString());
                params.put("telemovel", editTextTelemovel.getText().toString());
                params.put("password", editTextPassword.getText().toString());
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
