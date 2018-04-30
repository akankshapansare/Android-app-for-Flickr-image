package com.ap.flickr.api;

import com.ap.flickr.data.SearchResponse;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by akanksha on 4/21/2018.
 */

public interface FlickrApi {

    @GET("services/rest?method=flickr.photos.search&format=json&nojsoncallback=1&api_key=dcbd1fc222381baadd0e02f4406a7166")
    Single<SearchResponse> searchPhotos(@Query("text") String query);

    @GET("services/rest?method=flickr.photos.search&format=json&nojsoncallback=1&api_key=dcbd1fc222381baadd0e02f4406a7166")
    Single<SearchResponse> loadPhotos(@Query("text") String query, @Query("page") int pageNumber);
}
