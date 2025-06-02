package com.example.cafe_harmony;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatRoom extends AppCompatActivity {

    String senderEmail, recipientEmail, chatRoomId;
    EditText messageInput;
    LinearLayout messageContainer;
    ScrollView scrollView;
    DatabaseReference chatRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        // Get data
        recipientEmail = getIntent().getStringExtra("recipientEmail");
        senderEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // Firebase path uses safe keys
        chatRoomId = getChatRoomId(senderEmail, recipientEmail);
        chatRef = FirebaseDatabase.getInstance().getReference("Messages").child(chatRoomId);

        messageInput = findViewById(R.id.messageInput);
        messageContainer = findViewById(R.id.messageContainer);
        scrollView = findViewById(R.id.scrollView);
        Button sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(v -> sendMessage());

        loadMessages();
    }

    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        if (text.isEmpty()) return;

        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        Message msg = new Message(senderEmail, text, time);
        chatRef.push().setValue(msg);
        messageInput.setText("");
    }

    private void loadMessages() {
        chatRef.addChildEventListener(new ChildEventListener() {
            @Override public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                Message msg = snapshot.getValue(Message.class);
                addMessageToLayout(msg);
            }
            @Override public void onChildChanged(DataSnapshot snapshot, String previousChildName) {}
            @Override public void onChildRemoved(DataSnapshot snapshot) {}
            @Override public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}
            @Override public void onCancelled(DatabaseError error) {}
        });
    }

    private void addMessageToLayout(Message msg) {
        TextView bubble = new TextView(this);
        bubble.setText(msg.sender + ":\n" + msg.text + " (" + msg.time + ")");
        bubble.setBackgroundResource(R.drawable.whiteroundbg);
        bubble.setPadding(20, 12, 20, 12);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 0);

        if (msg.sender.equals(senderEmail)) {
            params.gravity = Gravity.END;
        } else {
            params.gravity = Gravity.START;
        }

        bubble.setLayoutParams(params);
        messageContainer.addView(bubble);

        // Scroll to bottom
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private String getChatRoomId(String user1, String user2) {
        String u1 = user1.replace(".", "_");
        String u2 = user2.replace(".", "_");
        return u1.compareTo(u2) < 0 ? u1 + "_" + u2 : u2 + "_" + u1;
    }
}
