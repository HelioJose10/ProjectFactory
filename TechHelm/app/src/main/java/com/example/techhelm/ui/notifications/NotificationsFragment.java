package com.example.techhelm.ui.notifications;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.techhelm.R;
import com.example.techhelm.databinding.FragmentNotificationsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private LinearLayout linearLayout;
    private LayoutInflater layoutInflater;
    private int userId = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        layoutInflater = inflater;
        linearLayout = root.findViewById(R.id.notification_linear_layout);

        new FetchDataTask().execute();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class FetchDataTask extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            String url = "https://rubenpassarinho.pt/notifications.php?id=" + userId;
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return new JSONArray(Objects.requireNonNull(response.body()).string());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject object = jsonArray.getJSONObject(i);

                        View cardView = layoutInflater.inflate(R.layout.card_view_layout, linearLayout, false);

                        TextView textViewValue = cardView.findViewById(R.id.text_value);
                        textViewValue.setText(object.getString("valor") + " cm");

                        TextView textViewDate = cardView.findViewById(R.id.text_date);
                        textViewDate.setText(object.getString("data"));

                        TextView textViewTime = cardView.findViewById(R.id.text_time);
                        textViewTime.setText(object.getString("hora"));

                        linearLayout.addView(cardView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
