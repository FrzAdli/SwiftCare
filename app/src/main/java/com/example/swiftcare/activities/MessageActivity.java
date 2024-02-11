package com.example.swiftcare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.swiftcare.adapters.MessageAdapter;
import com.example.swiftcare.databinding.ActivityMessageBinding;
import com.example.swiftcare.models.Chat;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageActivity extends AppCompatActivity {
    private ActivityMessageBinding binding;
    private PreferenceManager preferenceManager;
    private List<Chat> chatMessages;
    private MessageAdapter messageAdapter;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        loadMessages();
        setListeners();
    }

    private void init() {
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages = new ArrayList<>();
        messageAdapter = new MessageAdapter(chatMessages, getApplicationContext());
        binding.messageRecyclerView.setAdapter(messageAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void loadMessages() {
        database.collection(Constants.KEY_COLLECTION_MESSAGES)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_MESSAGES)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null) {
            return;
        }
        if(value != null) {
            int count = chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if(documentChange.getType() == DocumentChange.Type.ADDED) {
                    Chat chat = new Chat();
                    if(documentChange.getDocument().getString(Constants.KEY_SENDER_ID).equals(preferenceManager.getString(Constants.KEY_USER_ID))) {
                        chat.senderId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        chat.receiverId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    } else {
                        chat.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        chat.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    }
                    chat.latestSender = documentChange.getDocument().getString(Constants.KEY_LATEST_SENDER);
                    chat.message = documentChange.getDocument().getString(Constants.KEY_LATEST_MESSAGE);
                    chat.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_LATEST_TIMESTAMP));
                    chat.dateObject = documentChange.getDocument().getDate(Constants.KEY_LATEST_TIMESTAMP);
                    chatMessages.add(chat);
                }
            }
            chatMessages.sort(Comparator.comparing(obj -> obj.dateObject));
            Collections.reverse(chatMessages);

            int addedItemCount = chatMessages.size() - count;
            if (addedItemCount > 0) {
                messageAdapter.notifyItemRangeInserted(count, addedItemCount);
                binding.messageRecyclerView.smoothScrollToPosition(count + addedItemCount - 1);
            }
        }
    };


    private String getReadableDateTime(Date date) {
        long currentTime = System.currentTimeMillis();
        long messageTime = date.getTime();
        long differenceInMillis = currentTime - messageTime;

        if (differenceInMillis < 24 * 60 * 60 * 1000) {
            return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date);
        } else {
            return new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(date);
        }
    }

    private void setListeners() {
        binding.backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }

}