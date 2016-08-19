package com.android.ravisha.nanodegree.stockhawk.util;

/**
 * Created by ravisha on 7/22/16.
 */
import com.android.ravisha.nanodegree.stockhawk.util.StocksDeserializer;
import com.android.ravisha.nanodegree.stockhawk.util.StocksDeserializer;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.android.ravisha.nanodegree.stockhawk.Constants.StockConstants;
import com.android.ravisha.nanodegree.stockhawk.model.StockModel;
import com.android.ravisha.nanodegree.stockhawk.util.StocksDeserializer;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class makes use of retrofit and gson libraries. retrofit(for rest calls ) and gson( Json conversions )
 * goes hand and hand to give you the data in java understandable format or we can also say
 * return the data in terms of objects.
 *
 */
public class ApiClient {
    private static Retrofit retrofit = null;
    private static Retrofit.Builder builder = null;
    /**
     * Gson is a Java library that can be used to convert Java Objects into their JSON representation. It can also be used to convert a
     * JSON string to an equivalent Java object
     *
     * Making use of GSON  library
     * eg:
     * Gson gson = new Gson();
     * Type token = new TypeToken<List<MyType>>(){}.getType();
     * return gson.fromJson(json, token);
     *
     * Following declaration  is for declaring the type we expect
     */
    static Type listType = new TypeToken<List<StockModel>>() {}.getType();


    /**
     *
     * @return
     */
    public static Retrofit getClient() {
        if (retrofit==null) {
           builder =      new Retrofit.Builder()
                            .baseUrl(StockConstants.BASE_URL)//Base URLs should always end in /
                            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(
                                    listType, new com.android.ravisha.nanodegree.stockhawk.util.StocksDeserializer()).create()));

            retrofit = builder.build();
        }
        return retrofit;
    }
}