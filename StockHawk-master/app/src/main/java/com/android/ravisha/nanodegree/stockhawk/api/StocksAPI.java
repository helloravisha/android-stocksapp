package com.android.ravisha.nanodegree.stockhawk.api;

/**
 * Created by ravisha on 7/22/16.
 */

import com.android.ravisha.nanodegree.stockhawk.Constants.StockConstants;
import com.android.ravisha.nanodegree.stockhawk.model.StockModel;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Using retrofit we  try to make rest call for retrieving the stocks data.
 * we are trying to define the  URL here, so that the suffix of this URL can
 * be added when we want explicit data from the webservice. and prefix(base URL:
 * in our case : "https://query.yahooapis.com/" )
 * will be added when we instantiate Retrofit.(APIClient.java will do instantiation.
 * DetailedActivity will make use of this interface for making calls for the detailed
 * stocks information.
 *
 * We can understand this a three step process,
 *
 * 1)  retrofit = ApiClient.getClient(); // [ URL Prefix , GSON, all magic done here ]
   2)  StocksAPI service = retrofit.create(StocksAPI.class);// sends the URL here, which we are doing in this class
   3)  Call< List<StockModel>> call = service.getDetailedStocks(quoteQuery);// URL suffice, query parameter
 *
 */
public interface StocksAPI {// the annotation value given for get is called endpoint.

    // Endpoint  should not begin with /, so that it will be relative
    // Endpoint  which contain a leading / are absolute., Removes any thing after host
    // Endpoint  may be a full URL, if // is used before end point
     @GET(StockConstants.END_POINT)
     Call< List<StockModel>> getDetailedStocks(@Query("q") String query);


}
