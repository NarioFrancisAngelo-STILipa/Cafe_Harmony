package com.example.cafe_harmony;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.List;


public class Order_Menu extends AppCompatActivity {

    List<CartItem> cart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_menu);
        findViewById(R.id.txt_caramel).setOnClickListener(v -> addToCart("Caramel Macchiato", 120));
        findViewById(R.id.txt_americano).setOnClickListener(v -> addToCart("Americano", 100));
        findViewById(R.id.txt_cappuccino).setOnClickListener(v -> addToCart("Cappuccino", 110));
        findViewById(R.id.txt_aglio).setOnClickListener(v -> addToCart("Aglio Olio", 130));
        findViewById(R.id.txt_carbonara).setOnClickListener(v -> addToCart("Carbonara", 150));
        findViewById(R.id.txt_spaghetti).setOnClickListener(v -> addToCart("Spaghetti", 140));

        TextView proceedBtn = findViewById(R.id.btn_payment);
        ImageButton btn_returntodb = findViewById(R.id.btn_returnto_db);

        proceedBtn.setOnClickListener(v -> {
            String userEmail = getIntent().getStringExtra("userEmail");
            Intent intent = new Intent(Order_Menu.this, CheckoutActivity.class);
            intent.putExtra("userEmail", userEmail);
            intent.putExtra("cart", new ArrayList<>(cart));
            startActivity(intent);

        });
        btn_returntodb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Order_Menu.this, DashBoard.class);
                startActivity(intent);
            }
        });

    }
    private void addToCart(String name, int price) {
        for (CartItem item : cart) {
            if (item.name.equals(name)) {
                item.quantity++;
                Toast.makeText(this, name + " quantity increased", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        cart.add(new CartItem(name, 1, price));
        Toast.makeText(this, name + " added to cart", Toast.LENGTH_SHORT).show();
    }


}