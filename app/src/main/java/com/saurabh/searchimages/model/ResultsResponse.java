package com.saurabh.searchimages.model;

/**
 * Created by kiris on 3/25/2018.
 */

public class ResultsResponse {
    private String id;
    private UrlResponse urls;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UrlResponse getUrls() {
        return urls;
    }

    public void setUrls(UrlResponse urls) {
        this.urls = urls;
    }
}
