package com.android.ravisha.nanodegree.stockhawk.util;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.android.ravisha.nanodegree.stockhawk.model.StockModel;

import java.lang.reflect.Type;
import java.util.List;

public class StocksDeserializer implements JsonDeserializer<List<StockModel>> {

    @Override
    public List<StockModel> deserialize(JsonElement json, Type listType, JsonDeserializationContext context)
            throws JsonParseException {

        return new Gson().fromJson(

                json
                        .getAsJsonObject()
                        .get("query")
                        .getAsJsonObject()
                        .get("results")
                        .getAsJsonObject()
                        .get("quote").getAsJsonArray(), listType);

    }
}