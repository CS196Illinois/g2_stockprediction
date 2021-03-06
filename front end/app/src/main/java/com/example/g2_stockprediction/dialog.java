package com.example.g2_stockprediction;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * handle the search bar of the homepage.
 */

//This java file creates a dialogue box to search for a stock

public class dialog extends Dialog {

    //Instantiatng variables
    private HomePageNav activity;
    private Button search;
    private Button cancel;
    private EditText text;
    private dialog thisDialog;

    public dialog(HomePageNav context) {
        super(context);
        this.activity = context;
        this.thisDialog = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchable);
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        initalize();
    }

    private void initalize() {
        //Code to search for a stock
        text = (EditText) findViewById(R.id.text);
        search = (Button) findViewById(R.id.search);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                thisDialog.cancel();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String textString = text.getText().toString();
                activity.searchStock(textString);
            }
        });
    }

}

