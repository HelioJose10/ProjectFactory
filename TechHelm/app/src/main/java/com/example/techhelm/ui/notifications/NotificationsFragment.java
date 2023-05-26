package com.example.techhelm.ui.notifications;

import android.graphics.Color;
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

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private LinearLayout linearLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        linearLayout = root.findViewById(R.id.linearLayout);

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
            Request request = new Request.Builder()
                    .url("https://rubenpassarinho.pt/notifications.php")
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
                        // Create a new LinearLayout to hold each value
                        LinearLayout itemLayout = new LinearLayout(getActivity());
                        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));
                        itemLayout.setOrientation(LinearLayout.VERTICAL);
                        itemLayout.setPadding(16,16,16,16);

                        // Alternate background colors for each layout
                        int drawableResId = i % 2 == 0 ? R.drawable.rounded_rectangle_white1 : R.drawable.rounded_rectangle_white2;
                        itemLayout.setBackgroundResource(drawableResId);

                        // Add a title
                        TextView title = new TextView(getActivity());
                        title.setText("Distância");
                        itemLayout.addView(title);

                        // Add the value
                        TextView textView = new TextView(getActivity());
                        textView.setText(jsonArray.getJSONObject(i).getString("valor") + " cm");

                        itemLayout.addView(textView);
                        linearLayout.addView(itemLayout);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
