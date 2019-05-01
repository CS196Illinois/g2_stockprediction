package com.example.g2_stockprediction;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//This java file is used to show the stock of the day

public class TopStockOfTheDay extends AppCompatActivity {
    //Initializing the variables
    private DatabaseReference myRef;
    private DatabaseReference usersRef;
    private String currentUserid;
    private String stockname;
    private TextView sotd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_stock_of_the_day);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Assigning the variables
        myRef = FirebaseDatabase.getInstance().getReference().child("stocks");
        currentUserid = FirebaseAuth.getInstance().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserid);
        stockname = "GOOGL";
        sotd = (TextView) findViewById(R.id.txtSOTD);

        //Changes the name at the top
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(stockname);

        transparentStatusAndNavigation();

        //Shows the data for the specific stocks and competitors
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String bio = dataSnapshot.child(stockname).child("bio").getValue().toString();
                String close = dataSnapshot.child(stockname).child("close").getValue().toString();
                String open = dataSnapshot.child(stockname).child("open").getValue().toString();
                String high = dataSnapshot.child(stockname).child("currHigh").getValue().toString();
                String low = dataSnapshot.child(stockname).child("currLow").getValue().toString();
                String tomHigh = dataSnapshot.child(stockname).child("tomHigh").getValue().toString();
                String tomLow = dataSnapshot.child(stockname).child("tomLow").getValue().toString();
                String comp1 = dataSnapshot.child(stockname).child("comp1").getValue().toString();
                String comp2 = dataSnapshot.child(stockname).child("comp2").getValue().toString();

                String bio1 = dataSnapshot.child(comp1).child("bio").getValue().toString();
                String close1 = dataSnapshot.child(comp1).child("close").getValue().toString();
                String open1 = dataSnapshot.child(comp1).child("open").getValue().toString();
                String high1 = dataSnapshot.child(comp1).child("currHigh").getValue().toString();
                String low1 = dataSnapshot.child(comp1).child("currLow").getValue().toString();
                String tomHigh1 = dataSnapshot.child(comp1).child("tomHigh").getValue().toString();
                String tomLow1 = dataSnapshot.child(comp1).child("tomLow").getValue().toString();

                String bio2 = dataSnapshot.child(comp2).child("bio").getValue().toString();
                String close2 = dataSnapshot.child(comp2).child("close").getValue().toString();
                String open2 = dataSnapshot.child(comp2).child("open").getValue().toString();
                String high2 = dataSnapshot.child(comp2).child("currHigh").getValue().toString();
                String low2 = dataSnapshot.child(comp2).child("currLow").getValue().toString();
                String tomHigh2 = dataSnapshot.child(comp2).child("tomHigh").getValue().toString();
                String tomLow2 = dataSnapshot.child(comp2).child("tomLow").getValue().toString();

                String output = "\n\n\n" + bio + "\n\nClose: " + close + "\nOpen: " + open +
                        "\nHigh: " + high + "\nLow: " + low + "\nTomorrrow's High: " +
                        tomHigh + "\nTomorrow's Low: " + tomLow + "\n\nCompetitor 1: " + bio1 + "\n\nClose: " + close1 + "\nOpen: " + open1 +
                "\nHigh: " + high1 + "\nLow: " + low1 + "\nTomorrrow's High: " +
                        tomHigh1 + "\nTomorrow's Low: " + tomLow1 + "\n\nCompetitor 2: " + bio2 + "\n\nClose: " + close2 + "\nOpen: " + open2 +
                        "\nHigh: " + high2 + "\nLow: " + low2 + "\nTomorrrow's High: " +
                        tomHigh2 + "\nTomorrow's Low: " + tomLow2;
                sotd.setText(output);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //You can save the stock
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String stocklist = dataSnapshot.child("stocks").getValue().toString();
                        if (!stocklist.contains(stockname)) {
                            stocklist = stocklist + "," + stockname;
                            usersRef.child("stocks").setValue(stocklist);
                            Toast.makeText(TopStockOfTheDay.this, "Stock Saved.",
                                    Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(TopStockOfTheDay.this, "Error. Stock Already Saved.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void transparentStatusAndNavigation() {
        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(final int bits, boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
