package com.justice.imageshareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MainAdapter adapter;
    private TextInputLayout searchTxtInput;
    private Button searchBtn;

    private ArrayList<Picture> pictureArrayList;

    private RequestQueue mRequestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestWritePermissions();
        initWidgets();
        setOnClickListeners();
        parseJSON();
    }

    private void requestWritePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

    }

    private void setOnClickListeners() {

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = searchTxtInput.getEditText().getText().toString().trim();
                if (data.isEmpty()) {
                    searchTxtInput.setError("Please Fill Field");
                    searchTxtInput.requestFocus();
                    return;
                }

                searchForPicture(data+"&image");

            }
        });
    }

    private void searchForPicture(String data) {
        parseJSON(data);


    }

    private void parseJSON() {
        pictureArrayList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);
        String url = "https://pixabay.com/api/?key=5303976-fd6581ad4ac165d1b75cc15b3&q=kitten&image_type=photo&pretty=true";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("hits");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                String imageUrl = hit.getString("webformatURL");
                                int likeCount = hit.getInt("likes");
                                pictureArrayList.add(new Picture(imageUrl, likeCount));
                            }
                            adapter = new MainAdapter(MainActivity.this, pictureArrayList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }
    private void parseJSON(String data) {
        pictureArrayList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);
        String url = "https://pixabay.com/api/?key=5303976-fd6581ad4ac165d1b75cc15b3&q="+data+"_type=photo&pretty=true";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("hits");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                String imageUrl = hit.getString("webformatURL");
                                int likeCount = hit.getInt("likes");
                                pictureArrayList.add(new Picture(imageUrl, likeCount));
                            }
                            adapter = new MainAdapter(MainActivity.this, pictureArrayList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }

    private void initWidgets() {
        recyclerView = findViewById(R.id.recyclerView);
        searchTxtInput = findViewById(R.id.searchTxtInput);
        searchBtn = findViewById(R.id.searchBtn);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
