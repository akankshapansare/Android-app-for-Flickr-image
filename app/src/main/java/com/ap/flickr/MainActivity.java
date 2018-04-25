package com.ap.flickr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ap.flickr.adapter.PhotListAdapter;
import com.ap.flickr.api.FlickrApi;
import com.ap.flickr.data.SearchResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private int pageNumber = 1;
    private PhotListAdapter photListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.flickr.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final FlickrApi flickrApi = retrofit.create(FlickrApi.class);

        final EditText editText = findViewById(R.id.edit_text_search);
        Button searchButton = findViewById(R.id.button_search);
        Button loadButton = findViewById(R.id.button_load_more);
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String query = editText.getText().toString();
                Call<SearchResponse> call = flickrApi.searchPhotos(query);
                call.enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        SearchResponse searchResponse = response.body();
                        photListAdapter = new PhotListAdapter(searchResponse.getPhotoDetails().getPhotos(), MainActivity.this);
                        recyclerView.setAdapter(photListAdapter);
                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {

                    }
                });
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = editText.getText().toString();
                Call<SearchResponse> call = flickrApi.loadPhotos(query, ++pageNumber);
                call.enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        SearchResponse searchResponse = response.body();
                        photListAdapter.appendPhotos(searchResponse.getPhotoDetails().getPhotos());
                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {

                    }
                });
            }
        });


    }
}
