package com.example.chatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<ChatMessage>

{

    public CustomAdapter(@NonNull Context context, ArrayList<ChatMessage> chatMessageArrayList) {
        super(context, 0, chatMessageArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ChatMessage chatMessage = getItem(position);

        View listItemView = convertView;

        if (user.getUid().equals(chatMessage.getMessageUserID())) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.my_list_item, parent, false);
        }  else if (!(user.getUid().equals(chatMessage.getMessageUserID()))) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView message = listItemView.findViewById(R.id.message);
        TextView time = listItemView.findViewById(R.id.time);
        TextView name = listItemView.findViewById(R.id.name);

        message.setText(chatMessage.getMessageText());

        time.setText(chatMessage.getMessageTime());

        name.setText(chatMessage.getMessageUser());

        return listItemView;
    }
}
