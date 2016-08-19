package com.android.ravisha.nanodegree.stockhawk.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by sam_chordas on 10/5/15.
 */
@Database(version = com.android.ravisha.nanodegree.stockhawk.data.QuoteDatabase.VERSION)
public class QuoteDatabase {
  private QuoteDatabase(){}

  public static final int VERSION = 7;

  @Table(com.android.ravisha.nanodegree.stockhawk.data.QuoteColumns.class) public static final String QUOTES = "quotes";
}
