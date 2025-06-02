package com.example.cafe_harmony;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

    EditText editTextSearch;
    RecyclerView userResults;
    UserAdapter adapter;
    List<String> allUsers = new ArrayList<>();
    List<String> filteredUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        String userEmail = getIntent().getStringExtra("userEmail");
        editTextSearch = findViewById(R.id.editTextSearch);
        userResults = findViewById(R.id.userResults);
        adapter = new UserAdapter(this, filteredUsers);
        userResults.setLayoutManager(new LinearLayoutManager(this));
        userResults.setAdapter(adapter);

        loadAllEmailsFromFirebase();

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterEmails(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void loadAllEmailsFromFirebase() {
        FirebaseDatabase.getInstance().getReference("User").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        allUsers.clear();
                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            String email = userSnap.child("email").getValue(String.class);
                            String currentUser = getIntent().getStringExtra("userEmail");
                            if (email != null && !email.equals(currentUser)) {
                                allUsers.add(email);

                            }

                        }
                        filterEmails(editTextSearch.getText().toString());

                    }
                    @Override public void onCancelled(DatabaseError error) {}
                });
    }

    private void filterEmails(String query) {
        filteredUsers.clear();
        for (String email : allUsers) {
            if (email.toLowerCase().contains(query.toLowerCase())) {
                filteredUsers.add(email);
                Toast.makeText(Chat.this, "Filtered: " + filteredUsers.size(), Toast.LENGTH_SHORT).show();
            }
        }
        adapter.updateList(filteredUsers);
    }
}
