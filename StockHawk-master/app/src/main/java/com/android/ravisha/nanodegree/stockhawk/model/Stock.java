package com.android.ravisha.nanodegree.stockhawk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ravisha on 7/21/16.
 */
public class Stock
{
     @SerializedName("label")
     private String label;

    @SerializedName("value")
     private String value;

    public Stock(String label,String value){
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
