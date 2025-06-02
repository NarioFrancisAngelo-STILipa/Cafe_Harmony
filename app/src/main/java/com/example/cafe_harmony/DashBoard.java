package com.example.cafe_harmony;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DashBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dash_board);

        ImageButton btnchat = findViewById(R.id.btn_chat);
        ImageButton btnreserve = findViewById(R.id.btn_reservation);
        ImageButton btnorder = findViewById(R.id.btn_order);
        ImageButton btnsetting = findViewById(R.id.btn_setting);
        String userEmail = getIntent().getStringExtra("userEmail");
        btnchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoard.this, Chat.class );
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
            }
        });

        btnreserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoard.this, Reservation.class);
                startActivity(intent);
            }
        });

        btnorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DashBoard.this, Order_Menu.class);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);

            }
        });

        btnsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoard.this, Settings.class);
                startActivity(intent);
            }
        });
    }
}