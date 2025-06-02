package com.example.cafe_harmony;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TextView signupclickable = findViewById(R.id.txt_signup);
        EditText loginemail = findViewById(R.id.login_email);
        EditText passlogin = findViewById(R.id.login_password);
        Button btn = findViewById(R.id.btn_signin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_login = loginemail.getText().toString();
                String password_login = passlogin.getText().toString();

                if (email_login.isEmpty() || password_login.isEmpty()){
                    Toast.makeText(LogIn.this,"These field cannot be empty.", Toast.LENGTH_SHORT).show();
                }

                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User");
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean found = false;
                        for (DataSnapshot userSnap : snapshot.getChildren()){
                            User user = userSnap.getValue(User.class);
                            if (user != null && user.email.equals(email_login) && user.password.equals(password_login)){
                                found = true;
                                break;
                            }
                        }

                        if (found){
                            Toast.makeText(LogIn.this,"Signed In Successfully!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LogIn.this, DashBoard.class);
                            intent.putExtra("userEmail", email_login);
                            startActivity(intent);
                        } else{
                            Toast.makeText(LogIn.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(LogIn.this, "Database Error: "+ error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        signupclickable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(LogIn.this, SignUp.class);
                startActivity(intent);
            }
        });

    }
}