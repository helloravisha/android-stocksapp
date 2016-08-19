package com.android.ravisha.nanodegree.stockhawk.util;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.android.ravisha.nanodegree.stockhawk.R;
import com.android.ravisha.nanodegree.stockhawk.service.WidgetRemoteService;


/**
 * This class is called as per the widget configuration , we can term this class as
 * reciever class, this is first class that gets triggered for getting widget into action.
 */
public class StockWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager, i);
        }
    }

    /**
     * Defining whats our remote view by mentioning the layout file of the widget , and then
     * making use of that reference(rv) of remote view for setting the remoteAdapter and
     * calling updateAppWidget along with id associated with the widget.
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     */
    private void updateWidget(Context context, AppWidgetManager appWidgetManager,
                      int appWidgetId) {
        RemoteViews rv = new RemoteViews(context.getPackageName(),
                R.layout.stock_widget);
        setList(rv, context, appWidgetId);// mapping remote view to the adapter along with widget id.
        appWidgetManager.updateAppWidget(appWidgetId, rv);//mapping remote view  to the widget id.
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,
                R.id.stock_list);// this triggers the adapter class method onDataChanged.
    }

    /**
     * passing the adapter "WisdgetRemoteService"  in the form of an intent, as
     * the setRemoteAdapter()  expects the extra information like id of the widget
     * and  context , to make the syntax of setRemoteAdapter generic intent
     * is used as an argument of the method.
     * @param rv
     * @param context
     * @param appWidgetId
     */
    private void setList(RemoteViews rv, Context context, int appWidgetId) {
        Intent adapter = new Intent(context, WidgetRemoteService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        rv.setRemoteAdapter(R.id.stock_list, adapter);
    }
}