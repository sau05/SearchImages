package com.saurabh.searchimages.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saurabh.searchimages.model.ResultsResponse;
import com.saurabh.searchimages.model.UrlResponse;

import java.util.ArrayList;

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

public class SQLiteDataProvider {

    private Context mContext;
    private SQLiteDataHelper helper;

    public SQLiteDataProvider(Context context, SQLiteDataHelper sqLiteDataHelper) {
        this.mContext = context;
        this.helper = sqLiteDataHelper;
    }

    public void saveResults(ArrayList<ResultsResponse> resultsResponses) {
        SQLiteDatabase db = helper.getWritableDatabase();
        for (int i = 0; i < resultsResponses.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(COL_RESULT_ID, resultsResponses.get(i).getId());
            cv.put(COL_FULL, resultsResponses.get(i).getUrls().getFull());
            cv.put(COL_RAW, resultsResponses.get(i).getUrls().getRaw());
            cv.put(COL_REGULAR, resultsResponses.get(i).getUrls().getRegular());
            cv.put(COL_SMALL, resultsResponses.get(i).getUrls().getSmall());
            cv.put(COL_THUMB, resultsResponses.get(i).getUrls().getThumb());
            db.insertOrThrow(TBL_RESULT, null, cv);
        }
    }

    public ArrayList<ResultsResponse> getResults(int start) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = null;
        ArrayList<ResultsResponse> results = new ArrayList<>();
        try {

            c = db.query(TBL_RESULT, new String[]{COL_RESULT_ID,COL_FULL,COL_RAW,COL_REGULAR,COL_SMALL,COL_THUMB}, COL_ID+" > ?", new String[]{String.valueOf(start)}, null, null, "id LIMIT "+String.valueOf(start+10));
            while (c.moveToNext()) {
                ResultsResponse response = new ResultsResponse();
                UrlResponse urlResponse = new UrlResponse();
                response.setId(c.getColumnName(c.getColumnIndex(COL_RESULT_ID)));
                urlResponse.setFull(c.getColumnName(c.getColumnIndex(COL_FULL)));
                urlResponse.setRaw(c.getColumnName(c.getColumnIndex(COL_RAW)));
                urlResponse.setRegular(c.getColumnName(c.getColumnIndex(COL_REGULAR)));
                urlResponse.setSmall(c.getColumnName(c.getColumnIndex(COL_SMALL)));
                urlResponse.setThumb(c.getColumnName(c.getColumnIndex(COL_THUMB)));
                response.setUrls(urlResponse);
                results.add(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        Log.d("getresult",""+results.size());
        return results;
    }
}
