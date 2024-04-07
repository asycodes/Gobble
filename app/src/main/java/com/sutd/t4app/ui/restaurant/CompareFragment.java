package com.sutd.t4app.ui.restaurant;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sutd.t4app.BuildConfig;
import com.sutd.t4app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CompareFragment extends Fragment {

    private CompareViewModel mViewModel;

    public static CompareFragment newInstance() {
        return new CompareFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("openai", "onCreateView");
        View root = inflater.inflate(R.layout.fragment_compare, container, false);
        TextView llmText = root.findViewById(R.id.llmOutput);


        //call openAI API - add prompt "How are you"
        // Create OkHttpClient
        OkHttpClient client = new OkHttpClient();
        Log.d("openai", "request builder");

// Create request
        String apikey = "Bearer " + BuildConfig.OPENAI_API;
        Log.d("openai", apikey);
        // Create JSON body
        String jsonBody = "{"
        + "\"model\":\"gpt-3.5-turbo\","
        + "\"messages\":[{\"role\":\"user\",\"content\":\"how are you\"}],"
        + "\"temperature\":1,"
        + "\"max_tokens\":256,"
        + "\"top_p\":1,"
        + "\"frequency_penalty\":0,"
        + "\"presence_penalty\":0"
        + "}";
        
        Request request = new Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", apikey)
            .post(RequestBody.create(jsonBody, MediaType.parse("application/json; charset=utf-8")))
            .build();

// Make the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("openai", "onFailure");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("openai", "onResponse");
                if (!response.isSuccessful()) {
                    Log.d("openai", "response fail");
                    Log.d("openai", response.toString());
                    throw new IOException("Unexpected code " + response);

                } else {
                    // Get the response
                    String responseData = response.body().string();
                    Log.d("openai", responseData);
                    // Parse the response and get the text
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        JSONArray choicesArray = jsonObject.getJSONArray("choices");
                        JSONObject firstChoiceObject = choicesArray.getJSONObject(0);
                        JSONObject messageObject = firstChoiceObject.getJSONObject("message");
                        String content = messageObject.getString("content");

                        // Update the TextView on the main thread
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("openai", "update llm text");
                                TextView llmText = getView().findViewById(R.id.llmOutput);
                                llmText.setText(content);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return root;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CompareViewModel.class);
        // TODO: Use the ViewModel
    }

}