package com.example.easyfarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.card.MaterialCardView;

public class menu extends AppCompatActivity {
    private MaterialCardView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        back_button =  findViewById(R.id.btn_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });
    }

    private void goToBack() {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }


}