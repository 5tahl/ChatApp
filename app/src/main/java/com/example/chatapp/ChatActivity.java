package com.example.chatapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonSend, buttonLogout;
    EditText editTextMessage;
    ListView listView;
    DAOChatMessage dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(this);

        editTextMessage = findViewById(R.id.editTextMessage);

        listView = findViewById(R.id.listView);

        dao = new DAOChatMessage();

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {

            // Sign in/sign up activity
            ActivityResultLauncher<Intent> loginResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Toast.makeText(ChatActivity.this,
                                        "Signed in.",
                                        Toast.LENGTH_LONG)
                                        .show();
                            } else {
                                Toast.makeText(ChatActivity.this,
                                        "We couldn't sign you in.",
                                        Toast.LENGTH_LONG)
                                        .show();
                                finish();
                            }
                        }
                    });

            loginResultLauncher.launch(AuthUI.getInstance().createSignInIntentBuilder().setTheme(R.style.Theme_ChatApp).build());

        } else {
            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Retrieve data.
        ArrayList<ChatMessage> chatList = new ArrayList<>();
        CustomAdapter customAdapter = new CustomAdapter(ChatActivity.this, chatList);
        listView.setAdapter(customAdapter);

        DatabaseReference reference = FirebaseDatabase
                .getInstance("https://chatapp-fd0ce-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference().child("ChatMessage");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage message = snapshot.getValue(ChatMessage.class);
                    chatList.add(message);
                }
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonLogout) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // Close activity
                            finish();
                        }
                    });
        } else if (v.getId() == R.id.buttonSend) {

            EditText input = findViewById(R.id.editTextMessage);
            if (!(input.getText().toString().equals(""))) {
                ChatMessage message = new ChatMessage(input.getText().toString(), Objects.requireNonNull(FirebaseAuth.getInstance()
                        .getCurrentUser())
                        .getDisplayName());

                dao.add(message);

                editTextMessage.setText("");
            }
        }
    }
}