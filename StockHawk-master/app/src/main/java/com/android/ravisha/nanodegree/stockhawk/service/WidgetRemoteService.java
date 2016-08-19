package com.android.ravisha.nanodegree.stockhawk.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.android.ravisha.nanodegree.stockhawk.service.WidgetRemoteViewServiceFactory;
import com.android.ravisha.nanodegree.stockhawk.service.*;
import com.android.ravisha.nanodegree.stockhawk.service.WidgetRemoteViewServiceFactory;
import com.android.ravisha.nanodegree.stockhawk.service.WidgetRemoteViewServiceFactory;

/**
 * Instantiates the class that implements RemoteViewsFactory, in our project  that class is
 * WidgetRemoteViewServiceFactory.
 */
public class WidgetRemoteService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent){
            return new WidgetRemoteViewServiceFactory(this);
    }
}