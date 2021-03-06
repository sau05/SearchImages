package com.saurabh.searchimages.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.saurabh.searchimages.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener {

    private String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        initViews();
    }

    private void initViews() {
        SearchView searchView=findViewById(R.id.searchView);
        Button btnSearch=findViewById(R.id.btnSearch);

        searchView.setOnQueryTextListener(this);
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (s!=null&&!"".equals(s)){
            Intent intent=new Intent(getApplicationContext(),ResultActivity.class);
            intent.putExtra("query",s);
            startActivity(intent);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String qry) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String qry) {
        s=qry;
        return false;
    }
}
