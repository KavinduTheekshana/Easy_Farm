package com.example.easyfarm;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.card.MaterialCardView;

public class menu extends AppCompatActivity {
    private MaterialCardView back_button;
    LinearLayout layout1;
    LinearLayout layout2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        back_button =  findViewById(R.id.btn_back);
        layout1 = findViewById(R.id.privous);
        layout2 = findViewById(R.id.logout);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, result.class);
                startActivity(intent);
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, login.class);
                startActivity(intent);
            }
        });
    }

    private void goToBack() {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }


}