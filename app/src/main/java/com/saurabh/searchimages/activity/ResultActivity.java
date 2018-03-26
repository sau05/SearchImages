package com.saurabh.searchimages.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.saurabh.searchimages.AppConstants;
import com.saurabh.searchimages.R;
import com.saurabh.searchimages.adapter.RecyclerAdapter;
import com.saurabh.searchimages.data.SQLiteDataHelper;
import com.saurabh.searchimages.data.SQLiteDataProvider;
import com.saurabh.searchimages.listener.OnLoadMoreListener;
import com.saurabh.searchimages.model.MainResponse;
import com.saurabh.searchimages.model.ResultsResponse;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity implements OnLoadMoreListener {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter recyclerAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<ResultsResponse> resultsResponses;
    private SQLiteDataProvider dataProvider;
    private MainResponse mainResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar mToolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();

        dataProvider=new SQLiteDataProvider(getApplicationContext(),new SQLiteDataHelper(getApplicationContext()));
        Intent intent=getIntent();
        String s=intent.getStringExtra("query");

        setTitle("image results of "+s);
        resultsResponses=new ArrayList<ResultsResponse>();
        getData(s);
    }

    private void initViews(){
        mRecyclerView=findViewById(R.id.recycleView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            if (mainResponse.getResults()!=null)
            dataProvider.saveResults(mainResponse.getResults());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void getData(String qry){
        String url = String.format(AppConstants.URL,"1", qry,getString(R.string.access_key));
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        mainResponse=gson.fromJson(response,MainResponse.class);
                        resultsResponses=mainResponse.getResults();
                        setAdapter();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void setAdapter() {
        // create an Object for Adapter
        recyclerAdapter=new RecyclerAdapter(getApplicationContext(),resultsResponses,mRecyclerView);
        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnLoadMoreListener(ResultActivity.this);
    }

    @Override
    public void onLoadMore() {
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                resultsResponses.add(null);
                recyclerAdapter.notifyItemInserted(resultsResponses.size() - 1);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resultsResponses.remove(resultsResponses.size()-1);
                recyclerAdapter.notifyItemRemoved(resultsResponses.size());
//                add items by one
                int start=resultsResponses.size();
//                int end=start+10;
                resultsResponses.addAll(dataProvider.getResults(start+1));
                recyclerAdapter.notifyItemInserted(resultsResponses.size());
                recyclerAdapter.setLoaded();
            }
        },2000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
