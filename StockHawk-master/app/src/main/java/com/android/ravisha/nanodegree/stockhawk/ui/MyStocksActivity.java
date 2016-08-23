package com.android.ravisha.nanodegree.stockhawk.ui;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.ravisha.nanodegree.stockhawk.Constants.StockConstants;
import com.android.ravisha.nanodegree.stockhawk.R;
import com.android.ravisha.nanodegree.stockhawk.data.QuoteColumns;
import com.android.ravisha.nanodegree.stockhawk.data.QuoteProvider;
import com.android.ravisha.nanodegree.stockhawk.rest.QuoteCursorAdapter;
import com.android.ravisha.nanodegree.stockhawk.rest.RecyclerViewItemClickListener;
import com.android.ravisha.nanodegree.stockhawk.rest.Utils;
import com.android.ravisha.nanodegree.stockhawk.service.StockIntentService;
import com.android.ravisha.nanodegree.stockhawk.service.StockTaskService;
import com.android.ravisha.nanodegree.stockhawk.util.MyQueryHandler;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.melnykov.fab.FloatingActionButton;
import com.android.ravisha.nanodegree.stockhawk.touch_helper.SimpleItemTouchHelperCallback;
import com.android.ravisha.nanodegree.stockhawk.ui.*;

public class MyStocksActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

  /**
   * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
   */

  /**
   * Used to store the last screen title. For use in {@link #restoreActionBar()}.
   */
  private CharSequence mTitle;
  private Intent mServiceIntent;
  private ItemTouchHelper mItemTouchHelper;
  private static final int CURSOR_LOADER_ID = 0;
  private QuoteCursorAdapter mCursorAdapter;
  private Context mContext;
  private Cursor mCursor;
  boolean isConnected;
    private TextView txtNetworkStatus;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mContext = this;
    ConnectivityManager cm =
        (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

     // txtNetworkStatus =   (TextView) findViewById(R.id.txtConnectivity);

    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    isConnected = activeNetwork != null &&
        activeNetwork.isConnectedOrConnecting();
    setContentView(R.layout.activity_my_stocks);
    // The intent service is for executing immediate pulls from the Yahoo API
    // GCMTaskService can only schedule tasks, they cannot execute immediately
    mServiceIntent = new Intent(this, StockIntentService.class);
    if (savedInstanceState == null){
      // Run the initialize task service so that some stocks appear upon an empty database
      mServiceIntent.putExtra("tag", "init");
      if (isConnected){
        startService(mServiceIntent);
      } else{
        networkToast();
          alertUserForNoConnectivity();

       }
    }
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

    mCursorAdapter = new QuoteCursorAdapter(this, null);
    recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this,
            new RecyclerViewItemClickListener.OnItemClickListener() {
              @Override public void onItemClick(View v, int position) {
                //TODO:


                mCursor.moveToPosition(position);
                //Log.i("position..", new String("" + position));
                //Log.i("Cursor Size..",""+mCursor.getCount());
                // mCursor.move(position);
               String stock = mCursor.getString(1);
                Log.i("Stock Symbol", stock);
                Intent intent = new Intent(com.android.ravisha.nanodegree.stockhawk.ui.MyStocksActivity.this, com.android.ravisha.nanodegree.stockhawk.ui.DetailedActivity.class);
                intent.putExtra(StockConstants.STOCK_NAME,stock);
                startActivity(intent);

               // System.out.println("Clicked on button.."+mCursor.getColumnName(position));
                // do something on item click
              }
            }));
    recyclerView.setAdapter(mCursorAdapter);


    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.attachToRecyclerView(recyclerView);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (isConnected){
          new MaterialDialog.Builder(mContext).title(R.string.symbol_search)
              .content(R.string.content_test)
              .inputType(InputType.TYPE_CLASS_TEXT)
              .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                @Override public void onInput(MaterialDialog dialog, CharSequence input) {
                  // On FAB click, receive user input. Make sure the stock doesn't already exist
                  // in the DB and proceed accordingly
                    addStock(input);
                }
              })
              .show();
        } else {
          networkToast();
        }

      }
    });

    ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mCursorAdapter);
    mItemTouchHelper = new ItemTouchHelper(callback);
    mItemTouchHelper.attachToRecyclerView(recyclerView);

    mTitle = getTitle();
    if (isConnected){
      long period = 3600L;
      long flex = 10L;
      String periodicTag = "periodic";

      // create a periodic task to pull stocks once every hour after the app has been opened. This
      // is so Widget data stays up to date.
      PeriodicTask periodicTask = new PeriodicTask.Builder()
          .setService(StockTaskService.class)
          .setPeriod(period)
          .setFlex(flex)
          .setTag(periodicTag)
          .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
          .setRequiresCharging(false)
          .build();
      // Schedule task with tag "periodic." This ensure that only the stocks present in the DB
      // are updated.
      GcmNetworkManager.getInstance(this).schedule(periodicTask);
    }
  }

    /**
     * Trying to add the stock asyncronously on a different thread  so that load can be dspatched form the main
     * thead.
     * @param input
     */
  private void addStock(CharSequence input){
      MyQueryHandler myQueryHander = new MyQueryHandler(getContentResolver(),this,mServiceIntent,input);
      myQueryHander.startQuery(1,null,QuoteProvider.Quotes.CONTENT_URI, new String[] { QuoteColumns.SYMBOL },
             QuoteColumns.SYMBOL + "= ?", new String[] { input.toString().toUpperCase() }, null);

/*
      Cursor c = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
              new String[] { QuoteColumns.SYMBOL }, QuoteColumns.SYMBOL + "= ?",
              new String[] { input.toString().toUpperCase() }, null);
      if (c.getCount() != 0) {
          showToasMessage(getResources().getString(R.string.stockAlreadySaved));
          return;
      } else {
          // Add the stock to DB
          if(!input.toString().trim().isEmpty()) {
              mServiceIntent.putExtra("tag", "add");
              mServiceIntent.putExtra("symbol", input.toString().toUpperCase());
              startService(mServiceIntent);
          }else{
              showToasMessage(getResources().getString(R.string.stockEmpty));
          }
      } */
  }


  private void showToasMessage(String message){
      Toast toast =
              Toast.makeText(com.android.ravisha.nanodegree.stockhawk.ui.MyStocksActivity.this, message,
                      Toast.LENGTH_LONG);
      toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
      toast.show();
  }


  @Override
  public void onResume() {
    super.onResume();
    getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
  }

  public void networkToast(){
    Toast.makeText(mContext, getString(R.string.network_toast), Toast.LENGTH_SHORT).show();
  }

  public void restoreActionBar() {
    ActionBar actionBar = getSupportActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    actionBar.setDisplayShowTitleEnabled(true);
    actionBar.setTitle(mTitle);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.my_stocks, menu);
      restoreActionBar();
      return true;
  }

  private void alertUserForNoConnectivity(){

      new AlertDialog.Builder(MyStocksActivity.this)
              .setTitle(getResources().getString(R.string.alert))
              .setMessage(getResources().getString(R.string.no_internet))
              .setPositiveButton(getResources().getString(R.string.okButton), null)
              .show();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    if (id == R.id.action_change_units){
      // this is for changing stock changes from percent value to dollar value
      Utils.showPercent = !Utils.showPercent;
      this.getContentResolver().notifyChange(QuoteProvider.Quotes.CONTENT_URI, null);
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args){
    // This narrows the return to only the stocks that are most current.
    return new CursorLoader(this, QuoteProvider.Quotes.CONTENT_URI,
        new String[]{ QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
            QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
        QuoteColumns.ISCURRENT + " = ?",
        new String[]{"1"},
        null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data){
    mCursorAdapter.swapCursor(data);
    mCursor = data;
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader){
    mCursorAdapter.swapCursor(null);
  }

}
