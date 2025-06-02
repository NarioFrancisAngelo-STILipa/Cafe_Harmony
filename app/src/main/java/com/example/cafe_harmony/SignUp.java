package com.example.cafe_harmony;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUp extends AppCompatActivity {

    @Override
    protected void  onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User");

        EditText names = findViewById(R.id.signupname);
        EditText emails = findViewById(R.id.signupemail);
        EditText pass = findViewById(R.id.signuppassword);
        EditText confirmationpass = findViewById(R.id.confirmationsignuppassword);
        TextView returntosignin = findViewById(R.id.txt_gobacklogin);
        Button btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                String username = names.getText().toString();
                String email = emails.getText().toString();
                String password = pass.getText().toString();
                String confirmpassword = confirmationpass.getText().toString();

                if (password.isEmpty() || confirmpassword.isEmpty() || username.isEmpty()|| email.isEmpty()){
                    Toast.makeText(SignUp.this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                }
                else if (password.equals(confirmpassword)) {
                    String userId = dbRef.push().getKey();
                    User user = new User(username, email, password);
                    dbRef.child(userId).setValue(user);
                    Toast.makeText(SignUp.this, "Signed Up Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this, LogIn.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(SignUp.this, "Passwords does not match!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        returntosignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, LogIn.class);
                startActivity(intent);
            }
        });
    }
}
