package com.example.g2_stockprediction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static java.security.AccessController.getContext;

public class StockPage extends AppCompatActivity {

    private ImageView logo;
    private TextView name;
    private TextView tomHigh;
    private TextView tomLow;
    private TextView recommender;
    private TextView currHigh;
    private TextView currLow;
    private TextView currOpen;
    private TextView currClose;
    private TextView comp1;
    private TextView comp2;
    private TextView comp1Price;
    private TextView comp2Price;
    private Button home;
    private Button save;
    private DatabaseReference myRef;
    private DatabaseReference usersRef;
    private String currentUserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_page);

        logo = (ImageView) findViewById(R.id.imgLogo);
        name = (TextView) findViewById(R.id.txtName);
        tomHigh = (TextView) findViewById(R.id.txtTomHigh);
        tomLow = (TextView) findViewById(R.id.txtTomLow);
        recommender = (TextView) findViewById(R.id.txtRecommender);
        currHigh = (TextView) findViewById(R.id.txtCurHigh);
        currLow = (TextView) findViewById(R.id.txtCurLow);
        currClose = (TextView) findViewById(R.id.txtCurClose);
        currOpen = (TextView) findViewById(R.id.txtCurOpen);
        comp1 = (TextView) findViewById(R.id.txtComp1);
        comp2 = (TextView) findViewById(R.id.txtComp2);
        comp1Price = (TextView) findViewById(R.id.txtComp1Price);
        comp2Price = (TextView) findViewById(R.id.txtComp2Price);
        home = (Button) findViewById(R.id.btnHome);
        save = (Button) findViewById(R.id.btnSave);
        myRef = FirebaseDatabase.getInstance().getReference().child("stocks");
        currentUserid = FirebaseAuth.getInstance().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserid);
        Intent intent = getIntent();
        final String stockname = intent.getStringExtra("stockname");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StockPage.this, HomePageNav.class);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String stocklist = dataSnapshot.child("stocks").getValue().toString();
                        if (!stocklist.contains(stockname)) {
                            stocklist = stocklist + ", " + stockname;
                            usersRef.child("stocks").setValue(stocklist);
                            Toast.makeText(StockPage.this, "Stock Saved.",
                                    Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(StockPage.this, "Error. Stock Already Saved.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String namein = dataSnapshot.child(stockname).child("name").getValue().toString();
                String logoin = dataSnapshot.child(stockname).child("logo").getValue().toString();
                String tomHighin = dataSnapshot.child(stockname).child("tomHigh").getValue().toString();
                String tomLowin = dataSnapshot.child(stockname).child("tomLow").getValue().toString();
                String currOpenin = dataSnapshot.child(stockname).child("open").getValue().toString();
                String currClosein = dataSnapshot.child(stockname).child("close").getValue().toString();
                String currHighin = dataSnapshot.child(stockname).child("currHigh").getValue().toString();
                String currLowin = dataSnapshot.child(stockname).child("currLow").getValue().toString();
                String comp1in = dataSnapshot.child(stockname).child("comp1").getValue().toString();
                String comp2in = dataSnapshot.child(stockname).child("comp2").getValue().toString();
                String comp1Pricein = dataSnapshot.child(stockname).child("comp1Price").getValue().toString();
                String comp2Pricein = dataSnapshot.child(stockname).child("comp2Price").getValue().toString();


                Picasso.with(StockPage.this)
                        .load(logoin)
                        .into(logo);

                name.setText(namein);
                tomHigh.setText(tomHighin);
                tomLow.setText(tomLowin);
                currOpen.setText(currOpenin);
                currClose.setText(currClosein);
                currLow.setText(currLowin);
                currHigh.setText(currHighin);
                comp1.setText(comp1in);
                comp2.setText(comp2in);
                comp1Price.setText(comp1Pricein);
                comp2Price.setText(comp2Pricein);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        transparentStatusAndNavigation();
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
