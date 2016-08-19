package com.android.ravisha.nanodegree.stockhawk.model;

import com.google.gson.annotations.SerializedName;
import com.android.ravisha.nanodegree.stockhawk.model.*;

import java.util.List;

/**
 * Created by ravisha on 7/27/16.
 */
public class StockResponse {
    public List<com.android.ravisha.nanodegree.stockhawk.model.StockModel> getResults() {
        return results;
    }

    public void setResults(List<com.android.ravisha.nanodegree.stockhawk.model.StockModel> results) {
        this.results = results;
    }

    @SerializedName("results")
    private List<com.android.ravisha.nanodegree.stockhawk.model.StockModel> results;


}
