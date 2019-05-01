package com.example.g2_stockprediction;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Settings extends AppCompatActivity {
    private DatabaseReference usersRef;
    private String currentUserid;
    private TextView list;
    private Button unsave;
    private Button update;
    private EditText unsavestock;
    private EditText updatename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        currentUserid = FirebaseAuth.getInstance().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserid);
        list = (TextView) findViewById(R.id.txtSavedList);
        unsave = (Button) findViewById(R.id.btnUnsaveStock);
        update = (Button) findViewById(R.id.btnUpdateName);
        unsavestock = (EditText) findViewById(R.id.etUnsaveStock);
        updatename = (EditText) findViewById(R.id.etChangeName);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = updatename.getText().toString();
                usersRef.child("name").setValue(name);
            }
        });

        unsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String remove = unsavestock.getText().toString();
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String current = dataSnapshot.child("stocks").getValue().toString();
                        current.replaceAll(remove, "");
                        current.replaceAll(",,", ",");
                        usersRef.child("stocks").setValue(current);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String stocklist = dataSnapshot.child("stocks").getValue().toString();
                list.setText(stocklist);
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
