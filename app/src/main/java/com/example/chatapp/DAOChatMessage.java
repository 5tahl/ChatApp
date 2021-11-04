package com.example.chatapp;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DAOChatMessage {

    private final DatabaseReference databaseReference;

    public DAOChatMessage() {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://chatapp-fd0ce-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = db.getReference(ChatMessage.class.getSimpleName());
    }

    public Task<Void> add(ChatMessage message) {
        return databaseReference.push().setValue(message);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key) {
        return databaseReference.child(key).removeValue();
    }
}
