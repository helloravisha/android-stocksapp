package com.android.ravisha.nanodegree.stockhawk.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.gcm.TaskParams;
import com.android.ravisha.nanodegree.stockhawk.service.*;

/**
 * Created by sam_chordas on 10/1/15.
 */
public class StockIntentService extends IntentService {

  public StockIntentService(){
    super(com.android.ravisha.nanodegree.stockhawk.service.StockIntentService.class.getName());
  }

  public StockIntentService(String name) {
    super(name);
  }

  @Override protected void onHandleIntent(Intent intent) {
    Log.d(com.android.ravisha.nanodegree.stockhawk.service.StockIntentService.class.getSimpleName(), "Stock Intent Service");
    com.android.ravisha.nanodegree.stockhawk.service.StockTaskService stockTaskService = new com.android.ravisha.nanodegree.stockhawk.service.StockTaskService(this);
    Bundle args = new Bundle();
    if (intent.getStringExtra("tag").equals("add")){
      args.putString("symbol", intent.getStringExtra("symbol"));
    }
    // We can call OnRunTask from the intent service to force it to run immediately instead of
    // scheduling a task.
    stockTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args));
  }
}
