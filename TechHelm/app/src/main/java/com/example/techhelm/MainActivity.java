package com.example.techhelm;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.techhelm.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String TAG = "HTTPDIR";

    public interface PostResponseListener {
        void onResponse(String response);
        void onError(String error);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkPermissions()) {
            requestPermissions();
        }

        if (!checkPermissions()) {
            Log.d(TAG, "Não foram dadas permissões");
        } else {
            Log.d(TAG, "Já há permissões de escrita");
        }

        File filesDir = getFilesDir();
        String dir = filesDir.getAbsolutePath();
        Log.d(TAG, dir);

        Context context = getApplicationContext();
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        @SuppressWarnings("deprecation")
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        Log.d(TAG, "IP: " + ip);

        criaIndex(filesDir, ip);

        //TinyWebServer.startServer("localhost",8080, dir);
        TinyWebServer.startServer(ip,6969, dir);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_profile, R.id.navigation_statistics)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Exemplo de uso da função sendPostRequest
        HashMap<String, String> params = new HashMap<>();
        params.put("ip", ip);

        String url = "https://rubenpassarinho.pt/savedata.php";

        sendPostRequest(context, url, params, new PostResponseListener() {
            @Override
            public void onResponse(String response) {
                // Faça algo com a resposta do servidor
                Log.d("Resposta", response);
            }

            @Override
            public void onError(String error) {
                // Faça algo com o erro
                Log.e("Erro", error);
            }
        });
    }
    
    private boolean checkPermissions() {
        Log.d(TAG, "checkPermissions");
        boolean perm = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                ;
        return  perm;
    }



    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                },
                1);
    }

    public void criaIndex(File filesDir, String ip) {
        File f = new File(filesDir + "/index.html");
        try {
            if (f.exists()) {
                Log.d(TAG, "Ficheiro index.html existe");
                f.delete();
                Log.d(TAG, "Vou abrir ficheiro para escrita");
                FileWriter out = new FileWriter(f, true);
                Log.d(TAG, "Abri ficheiro para escrita");
                out.append("<html><head><meta charset='utf-8'></head><body><h2>Android</h2><p>O meu IP é: " + ip + "</p></body></html>");
                Log.d(TAG, "Escrevi no ficheiro");
                out.flush();
                out.close();
            }

        } catch(Exception e){
            e.printStackTrace();
            Log.d(TAG, "Erro a criar o ficheiro: " + e.toString());
        }
    }

    public void sendPostRequest(Context context, String url, final HashMap<String, String> params, final PostResponseListener listener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (listener != null) {
                            listener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (listener != null) {
                            listener.onError(error.getMessage());
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

}


