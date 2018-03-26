package com.saurabh.searchimages.model;

import java.util.ArrayList;

/**
 * Created by kiris on 3/25/2018.
 */

public class MainResponse {

    private ArrayList<ResultsResponse> results;

    public ArrayList<ResultsResponse> getResults() {
        return results;
    }

    public void setResults(ArrayList<ResultsResponse> results) {
        this.results = results;
    }
}
