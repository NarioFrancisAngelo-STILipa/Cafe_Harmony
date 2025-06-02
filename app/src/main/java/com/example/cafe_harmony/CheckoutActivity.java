package com.example.cafe_harmony;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.*;

public class CheckoutActivity extends AppCompatActivity {

    LinearLayout cartLayout;
    TextView totalText;
    Spinner paymentSpinner;
    Button confirmButton, clearButton;
    ArrayList<CartItem> cart;
    int totalPrice = 0;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        cartLayout = findViewById(R.id.cartLayout);
        totalText = findViewById(R.id.totalText);
        paymentSpinner = findViewById(R.id.paymentSpinner);
        confirmButton = findViewById(R.id.confirmButton);
        clearButton = findViewById(R.id.clearCart);

        userEmail = getIntent().getStringExtra("userEmail");
        cart = (ArrayList<CartItem>) getIntent().getSerializableExtra("cart");

        setupCartUI();
        setupPaymentSpinner();

        confirmButton.setOnClickListener(v -> showReceipt());
        clearButton.setOnClickListener(v -> {
            cart.clear();
            setupCartUI();
            Toast.makeText(this, "Cart cleared", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupCartUI() {
        totalPrice = 0;

        cartLayout.removeViews(1, cartLayout.getChildCount() - 4); // keep template, total, spinner, buttons

        for (CartItem item : cart) {

            View template = findViewById(R.id.exampleItem);


            View itemRow = LayoutInflater.from(this).inflate(R.layout.activity_checkout, cartLayout, false)
                    .findViewById(R.id.exampleItem);
            itemRow = duplicateView(template);

            itemRow.setVisibility(View.VISIBLE);

            TextView name = itemRow.findViewById(R.id.itemName);
            TextView qty = itemRow.findViewById(R.id.itemQty);
            TextView price = itemRow.findViewById(R.id.itemPrice);
            Button plus = itemRow.findViewById(R.id.btnPlus);
            Button minus = itemRow.findViewById(R.id.btnMinus);
            Button remove = itemRow.findViewById(R.id.btnRemove);

            name.setText(item.name);
            qty.setText("Qty: " + item.quantity);
            price.setText("₱" + (item.price * item.quantity));

            plus.setOnClickListener(v -> {
                item.quantity++;
                setupCartUI();
            });

            minus.setOnClickListener(v -> {
                if (item.quantity > 1) item.quantity--;
                setupCartUI();
            });

            remove.setOnClickListener(v -> {
                cart.remove(item);
                setupCartUI();
                Toast.makeText(this, item.name + " removed", Toast.LENGTH_SHORT).show();
            });

            cartLayout.addView(itemRow, cartLayout.getChildCount() - 3);
            totalPrice += item.price * item.quantity;
        }

        totalText.setText("Total: ₱" + totalPrice);
    }
    private View duplicateView(View original) {
        LayoutInflater inflater = LayoutInflater.from(this);
        return inflater.inflate(R.layout.item_checkout_row, cartLayout, false);
    }



    private void setupPaymentSpinner() {
        String[] options = {"Pay Online", "Pay at Counter"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
        paymentSpinner.setAdapter(adapter);
    }

    private void showReceipt() {
        StringBuilder receipt = new StringBuilder("🧾 Digital Receipt\n\n");
        for (CartItem item : cart) {
            receipt.append(item.name).append(" x").append(item.quantity)
                    .append(" = ₱").append(item.price * item.quantity).append("\n");
        }

        receipt.append("\nTotal: ₱").append(totalPrice);
        String payment = paymentSpinner.getSelectedItem().toString();
        receipt.append("\nPayment: ").append(payment);

        String time = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(new Date());


        String encodedEmail = userEmail.replace(".", "_");
        Order order = new Order(cart, totalPrice, payment, time);
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
        String orderId = ordersRef.child(encodedEmail).push().getKey();
        ordersRef.child(encodedEmail).child(orderId).setValue(order);


        new AlertDialog.Builder(this)
                .setTitle("Order Confirmed")
                .setMessage(receipt.toString())
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent(CheckoutActivity.this, DashBoard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

}
