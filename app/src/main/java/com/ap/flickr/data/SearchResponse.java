package com.ap.flickr.data;

import java.util.List;

/**
 * Created by anike on 4/21/2018.
 */

public class SearchResponse {
    PhotosDetails photos;
    String stat;

    public PhotosDetails getPhotoDetails() {
        return photos;
    }

    public static class PhotosDetails {
        int page;
        int pages;
        int perpage;
        String total;
        List<Photo> photo;

        public List<Photo> getPhotos() {
            return photo;
        }
    }

    public static class Photo {
        String id;
        String owner;
        String secret;
        String server;
        int farm;
        String title;

        public int getFarm() {
            return farm;
        }

        public String getServer() {
            return server;
        }

        public String getOwner() {
            return owner;
        }

        public String getSecret() {
            return secret;
        }

        public String getId() {
            return id;
        }
    }
}
