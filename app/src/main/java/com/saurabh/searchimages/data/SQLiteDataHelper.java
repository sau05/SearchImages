package com.saurabh.searchimages.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.saurabh.searchimages.data.DataMeta.COL_FULL;
import static com.saurabh.searchimages.data.DataMeta.COL_ID;
import static com.saurabh.searchimages.data.DataMeta.COL_RAW;
import static com.saurabh.searchimages.data.DataMeta.COL_REGULAR;
import static com.saurabh.searchimages.data.DataMeta.COL_RESULT_ID;
import static com.saurabh.searchimages.data.DataMeta.COL_SMALL;
import static com.saurabh.searchimages.data.DataMeta.COL_THUMB;
import static com.saurabh.searchimages.data.DataMeta.TBL_RESULT;

/**
 * Created by kiris on 3/26/2018.
 */

public class SQLiteDataHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String TABLE_RESULT_CREATE_SQL = "CREATE TABLE " + TBL_RESULT + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_RESULT_ID + " TEXT UNIQUE, "
            + COL_RAW + " TEXT NOT NULL, "
            + COL_REGULAR + " TEXT NOT NULL, "
            + COL_FULL + " TEXT NOT NULL, "
            + COL_SMALL + " TEXT NOT NULL, "
            + COL_THUMB + " TEXT NOT NULL)";

    public SQLiteDataHelper(Context context) {
        super(context, "searchImages.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_RESULT_CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
