package com.example.cuahangdidongonline.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.cuahangdidongonline.R;

public class LienHeActivity extends AppCompatActivity {
    Toolbar toolbarlienhe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lien_he);
        toolbarlienhe = (Toolbar) findViewById(R.id.toolbarlienhe);
        ActionToolbar();
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarlienhe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarlienhe.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
