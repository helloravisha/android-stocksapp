package com.android.ravisha.nanodegree.stockhawk.util;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.Gravity;
import android.widget.Toast;

import com.android.ravisha.nanodegree.stockhawk.Constants.StockConstants;
import com.android.ravisha.nanodegree.stockhawk.R;
import com.android.ravisha.nanodegree.stockhawk.ui.MyStocksActivity;

public class MyQueryHandler extends AsyncQueryHandler {

    private MyStocksActivity myStocksActivity = null;
    private Intent mServiceIntent = null;
    private CharSequence input = null;

 
    public MyQueryHandler(ContentResolver cr,MyStocksActivity myStocksActivity,Intent mServiceIntent,CharSequence input)  {
        super(cr);
        this.myStocksActivity =  myStocksActivity;
        this.mServiceIntent = mServiceIntent;
        this.input = input;
    }

    private void showToasMessage(String message){
        Toast toast =
                Toast.makeText(myStocksActivity, message,
                        Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
        toast.show();
    }
 
    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        if (cursor.getCount() != 0) {
            showToasMessage(myStocksActivity.getResources().getString(R.string.stockAlreadySaved));
            return;
        } else {
            // Add the stock to DB
            if(!input.toString().trim().isEmpty()) {
                mServiceIntent.putExtra("tag", "add");
                mServiceIntent.putExtra("symbol", input.toString().toUpperCase());
                myStocksActivity.startService(mServiceIntent);
            }else{
                showToasMessage(myStocksActivity.getResources().getString(R.string.stockEmpty));
            }
        }
    }
 
    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        // insert() completed
    }
 
    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        // update() completed
    }
 
    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        // delete() completed
    }
}