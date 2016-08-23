package com.android.ravisha.nanodegree.stockhawk.Constants;

/**
 * Used for holding the stock constants
 * Created by ravisha on 7/22/16.
 */
public interface StockConstants {
    String BASE_URL = "https://query.yahooapis.com/";
    String END_POINT ="v1/public/yql?&format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=";
    String STOCK_NAME = "symbol";
    String AUTHORITY = "com.android.ravisha.nanodegree.stockhawk.data.QuoteProvider";
}
