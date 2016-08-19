package com.android.ravisha.nanodegree.stockhawk.service;
import android.content.Context;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.ravisha.nanodegree.stockhawk.R;
import com.android.ravisha.nanodegree.stockhawk.data.QuoteColumns;
import com.android.ravisha.nanodegree.stockhawk.data.QuoteProvider;

/**
 * THis is the final class in the total   widget flow to populate the data into
 * widgets, ( within android framework, who ever calls this factory 1) First invokes the
 * listner method onDataSetChanged() through which its gets the data and 2)  then we can make
 * use of that collection from getCount() to get the count , 3) followed by that
 * it will call "getViewAt" for rendering the view ), the method getViewAt() will
 * be called n times where n is the size of the collection.
 */
public class WidgetRemoteViewServiceFactory implements RemoteViewsService.RemoteViewsFactory {


        private Cursor mCursor;
        private Context mContext;
        int mWidgetId;

      public WidgetRemoteViewServiceFactory(Context context) {
            mContext = context;

     }

        @Override
        public void onCreate() {
            // Nothing to do
        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.stock_widget_list_content);
            if (mCursor.moveToPosition(position)) {
                rv.setTextViewText(R.id.stock_symbol,
                        mCursor.getString(mCursor.getColumnIndex(QuoteColumns.SYMBOL)));
                rv.setTextViewText(R.id.bid_price,
                        mCursor.getString(mCursor.getColumnIndex(QuoteColumns.BIDPRICE)));
                rv.setTextViewText(R.id.stock_change,
                        mCursor.getString(mCursor.getColumnIndex(QuoteColumns.CHANGE)));
            }
            return rv;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onDataSetChanged() {
            if (mCursor != null) {
                mCursor.close();
            }
            mCursor = mContext.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                    new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                            QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                    QuoteColumns.ISCURRENT + " = ?",
                    new String[]{"1"},
                    null);
        }

        @Override
        public void onDestroy() {
            if (mCursor != null) {
                mCursor.close();
            }
        }

    }
