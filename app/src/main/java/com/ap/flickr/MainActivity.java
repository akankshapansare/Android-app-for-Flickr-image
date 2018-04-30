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
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private int pageNumber = 1;
    private PhotListAdapter photListAdapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.flickr.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        final FlickrApi flickrApi = retrofit.create(FlickrApi.class);

        final EditText editText = findViewById(R.id.edit_text_search);
        Button searchButton = findViewById(R.id.button_search);
        Button loadButton = findViewById(R.id.button_load_more);
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        compositeDisposable.add(
                RxView.clicks(searchButton)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(Schedulers.io())
                        .flatMapSingle((view) -> {
                            String query = editText.getText().toString();
                            return flickrApi.searchPhotos(query);
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((searchResponse) -> {
                            photListAdapter = new PhotListAdapter(searchResponse.getPhotoDetails().getPhotos(), MainActivity.this);
                            recyclerView.setAdapter(photListAdapter);
                        }));

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = editText.getText().toString();
                Single<SearchResponse> single = flickrApi.loadPhotos(query, ++pageNumber);
                Disposable disposable = single.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((searchResponse) -> photListAdapter.appendPhotos(searchResponse.getPhotoDetails().getPhotos()));

                compositeDisposable.add(disposable);

            }
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}