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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.sutd.t4app.BuildConfig;
import com.sutd.t4app.R;
import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.ui.ProfileQuestions.QuestionFragment;
import com.sutd.t4app.ui.ProfileQuestions.UserProfile;
import com.sutd.t4app.ui.home.HomeFragmentViewModel;
import com.sutd.t4app.utility.RealmUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
//import io.realm.mongodb.UserProfile;
import io.realm.mongodb.sync.SyncConfiguration;
import io.realm.RealmChangeListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CompareFragment extends Fragment {

    private CompareViewModel mViewModel;
    private HomeFragmentViewModel hViewModel;

    @Inject
    App realmApp;
    private Realm realm;
    private RealmResults<UserProfile> realmResults;
    private RealmResults<Restaurant> realmRestaurantResults;

    public static CompareFragment newInstance() {
        return new CompareFragment();
    }
    View root;
    TextInputLayout textInputLayout;
    MaterialAutoCompleteTextView autoCompleteTextView;
    MaterialButton btnStartComparing;
    String[] restaurant = {"Restaurant1", "Restaurant2", "Restaurant3", "Restaurant4", "Restaurant5"};
    ArrayAdapter<String> adapterItems;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_compare, container, false);
        initializeRealm();

        textInputLayout = root.findViewById(R.id.compareInputLayout);
        autoCompleteTextView = root.findViewById(R.id.inputTV);
        btnStartComparing = root.findViewById(R.id.restaurantInputButton);
        adapterItems = new ArrayAdapter<String>(getActivity(), R.layout.restaurant_options, restaurant);

        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String restaurant = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getActivity(), "restaurant:" + restaurant, Toast.LENGTH_SHORT).show();
            }
        });
        btnStartComparing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoCompleteTextView.getText().toString().isEmpty()){
                    textInputLayout.setError("Please select a restaurant");
                }else {

                    Toast.makeText(getActivity()  , autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });



        Log.d("openai", "onCreateView");
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
    
    
    private void initializeRealm() {
        RealmUtility.getDefaultSyncConfig(realmApp, new RealmUtility.ConfigCallback() {
            @Override
            public void onConfigReady(SyncConfiguration configuration) {
                Realm.getInstanceAsync(configuration, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        Log.v("Cf", "we have initialiase realm " + realm);
                        CompareFragment.this.realm = realm;
                        observeUserProfile();
                        observeRestaurant();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e("QuestionFragment", "Error obtaining Realm configuration", e);
            }
        });
    }

    private void observeUserProfile() {
        Log.v("Cf", "we have called observerUserProfile ");

        // Perform your Realm query
        realmResults = realm.where(UserProfile.class).findAllAsync();
        Log.v("Cf", "we have received results realm " + realmResults);

        realmResults.addChangeListener(new RealmChangeListener<RealmResults<UserProfile>>() {
            @Override
            public void onChange(RealmResults<UserProfile> results) {
                Log.v("Cf", "we have received updated results realm " + results);

                // Handle changes
                Log.d("QuestionFragment", "UserProfile data updated: " + results.size());
            }
        });
    }

    private void observeRestaurant() {
        Log.v("Cf", "we have called observeRestaurant");

        // Perform your Realm query
        realmRestaurantResults = realm.where(Restaurant.class).findAllAsync();
        Log.v("Cf", "we have received results realm " + realmResults);

        realmRestaurantResults.addChangeListener(new RealmChangeListener<RealmResults<Restaurant>>() {
            @Override
            public void onChange(RealmResults<Restaurant> results) {
                Log.v("Cf", "we have received updated restaurant results realm " + results);

                // Handle changes
                Log.d("QuestionFragment", "Restaurant data updated: " + results.size());
            }
        });
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CompareViewModel.class);
        // TODO: Use the ViewModel
    }

}