package com.android.ravisha.nanodegree.stockhawk.ui;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.reflect.TypeToken;
import com.android.ravisha.nanodegree.stockhawk.Constants.StockConstants;
import com.android.ravisha.nanodegree.stockhawk.R;
import com.android.ravisha.nanodegree.stockhawk.api.StocksAPI;
import com.android.ravisha.nanodegree.stockhawk.model.StockModel;
import com.android.ravisha.nanodegree.stockhawk.util.ApiClient;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import com.github.mikephil.charting.data.Entry;

/**
 * This Activity is used for displaying the stock data over time.
 */
public class DetailedActivity extends Activity {
    private String symbol;
    private OkHttpClient client = new OkHttpClient();
      private LineChart lineChart = null;
    private List<StockModel> stockList = null;
    private String stockSelected = null;


    String fetchData(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private void initUIComponents(){
        lineChart = (LineChart) findViewById(R.id.chart);

    }

    private String getCurrentDate(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dateobj = new Date();
        return df.format(dateobj);
    }

    /**
     * rerturns the past date from the given number of months ( month has to be passed in negative value)
     * @param months
     * @return
     */
    private String getPasteDate(int months){
        Date referenceDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(referenceDate);
        c.add(Calendar.MONTH, months);
        Date date = c.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    /**
     * Hard coded the dates as the yahoo rest api is not returning the data for random
     * dates.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        initUIComponents();
        this.stockSelected = getIntent().getExtras().getString(StockConstants.STOCK_NAME);
        String  startDate="2009-09-11";
        String  endDate="2010-01-11";
        Type listType = new TypeToken<List<StockModel>>() {}.getType();
        Retrofit retrofit = null;
        try {


            String quoteQuery = "select * from yahoo.finance.historicaldata where symbol='"+stockSelected +"' and startDate = '"+startDate+"' and endDate = '"+endDate+"'";
            retrofit = ApiClient.getClient();
            StocksAPI service = retrofit.create(StocksAPI.class);


                    Call< List<StockModel>> call = service.getDetailedStocks(quoteQuery);
                    call.enqueue(new Callback< List<StockModel>>() {
                        @Override
                        public void onResponse(Call< List<StockModel>> call, retrofit2.Response< List<StockModel>> response) {
                            stockList = response.body();
                            displayChart();
                        }

                        @Override
                        public void onFailure(Call< List<StockModel>> call, Throwable t) {
                            t.printStackTrace();

                        }
                    });


                } catch (Exception exception) {
                    Log.i("Error ", exception.getMessage());
                }



    }

    private void displayChart() {
        initUIComponents();
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextSize(11f);

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<>();

        Collections.reverse(stockList);

        for (int i = 0; i < stockList.size(); i++) {
            xVals.add(i, stockList.get(i).getDate());
            yVals.add(new Entry(Float.valueOf(stockList.get(i).getClose()), i));
        }
        LineDataSet dataSet = new LineDataSet(yVals,stockSelected);
        LineData lineData = new LineData(xVals, dataSet);
        lineChart.setData(lineData);
        lineChart.setDescription(getResources().getString(R.string.stock_description));
        lineChart.setDescriptionTextSize(15f);
        lineChart.getLegend().setTextSize(12f);
        lineChart.setPinchZoom(false);
        lineChart.invalidate();

    }

}
