package com.example.techhelm.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.techhelm.databinding.FragmentProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private int userId = 3;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView textViewName = binding.textViewname;
        TextView textViewEmail = binding.textViewemail;
        TextView textViewCell = binding.textViewcell;

        fetchUserData(textViewName, textViewEmail, textViewCell);

        return root;
    }

    private void fetchUserData(TextView textViewName, TextView textViewEmail, TextView textViewCell) {
        String apiUrl = "https://rubenpassarinho.pt/utilizador.php?id=" + userId;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type", "application/json");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }
                        bufferedReader.close();


                        JSONObject jsonObject = new JSONObject(response.toString());

                        String name = jsonObject.getString("Nome");
                        String email = jsonObject.getString("Email");
                        String cell = jsonObject.getString("Telemovel");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textViewName.setText(name);
                                textViewEmail.setText(email);
                                textViewCell.setText(cell);
                            }
                        });
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
