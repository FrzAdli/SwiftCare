package com.example.swiftcare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.example.swiftcare.adapters.ChatAdapter;
import com.example.swiftcare.databinding.ActivityChatBinding;
import com.example.swiftcare.models.Chat;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.ImageLoader;
import com.example.swiftcare.utilities.PreferenceManager;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private PreferenceManager preferenceManager;
    private List<Chat> chatMessages;
    private ChatAdapter chatAdapter;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        listenMessages();
        setListeners();
    }

    private void init() {
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();

        if(getIntent().getExtras().getString(Constants.KEY_TARGET_ID) != null) {
            DocumentReference userDocument = database.collection("users").document(getIntent().getExtras().getString(Constants.KEY_TARGET_ID));
            userDocument.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    ImageLoader.loadCircleImage(documentSnapshot.getString(Constants.KEY_IMAGE_PROFILE), binding.headerPhotoImage);
                    binding.chatName.setText(documentSnapshot.getString(Constants.KEY_USERNAME));
                } else {
                    Log.d("Firestore", "Dokumen tidak ditemukan");
                }
            }).addOnFailureListener(e -> {
                Log.e("Firestore", "Gagal mengambil data", e);
            });
        }
    }

    private void listenMessages() {
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, getIntent().getExtras().getString(Constants.KEY_TARGET_ID))
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, getIntent().getExtras().getString(Constants.KEY_TARGET_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    @SuppressLint("NotifyDataSetChanged")
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null) {
            return;
        }
        if(value != null) {
            int count = chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if(documentChange.getType() == DocumentChange.Type.ADDED) {
                    Chat chats = new Chat();
                    chats.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chats.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chats.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chats.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chats.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);

                    chatMessages.add(chats);
                }
            }
            chatMessages.sort(Comparator.comparing(obj -> obj.dateObject));
            if (count == 0) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }
        }
    };

    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date);
    }

    private void setListeners() {
        binding.layoutSend.setOnClickListener(v -> sendMessage());

        binding.backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }

    private void sendMessage() {
        if (!binding.inputMessage.getText().toString().isEmpty()) {
            String messageValue = binding.inputMessage.getText().toString();
            binding.inputMessage.setText(null);
            String targetUserId = getIntent().getExtras().getString(Constants.KEY_TARGET_ID);
            String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);

            // Mencari dokumen dengan pasangan pengirim/penerima tertentu
            CollectionReference messagesCollection = database.collection(Constants.KEY_COLLECTION_MESSAGES);
            messagesCollection
                    .whereEqualTo(Constants.KEY_SENDER_ID, currentUserId)
                    .whereEqualTo(Constants.KEY_RECEIVER_ID, targetUserId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Dokumen sudah ada sebagai sender, lakukan update
                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                DocumentReference messageRef = messagesCollection.document(documentSnapshot.getId());
                                updateMessage(messageRef, messageValue);
                            } else {
                                // Dokumen belum ada sebagai sender, coba cari sebagai receiver
                                messagesCollection
                                        .whereEqualTo(Constants.KEY_SENDER_ID, targetUserId)
                                        .whereEqualTo(Constants.KEY_RECEIVER_ID, currentUserId)
                                        .get()
                                        .addOnCompleteListener(receiverTask -> {
                                            if (receiverTask.isSuccessful()) {
                                                QuerySnapshot receiverQuerySnapshot = receiverTask.getResult();
                                                if (receiverQuerySnapshot != null && !receiverQuerySnapshot.isEmpty()) {
                                                    // Dokumen sudah ada sebagai receiver, lakukan update
                                                    DocumentSnapshot receiverDocumentSnapshot = receiverQuerySnapshot.getDocuments().get(0);
                                                    DocumentReference receiverMessageRef = messagesCollection.document(receiverDocumentSnapshot.getId());
                                                    updateMessage(receiverMessageRef, messageValue);
                                                } else {
                                                    // Dokumen belum ada, buat dokumen baru
                                                    createNewMessage(messagesCollection, currentUserId, targetUserId, messageValue);
                                                }
                                            } else {
                                                Log.e("Firestore", "Gagal mengambil data (receiver)", receiverTask.getException());
                                            }
                                        });
                            }
                        } else {
                            Log.e("Firestore", "Gagal mengambil data (sender)", task.getException());
                        }
                    });
        }
    }


    private void createNewMessage(CollectionReference messagesCollection, String currentUserId, String targetUserId, String messageValue) {
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, currentUserId);
        message.put(Constants.KEY_RECEIVER_ID, targetUserId);
        message.put(Constants.KEY_LATEST_SENDER, currentUserId);
        message.put(Constants.KEY_LATEST_MESSAGE, messageValue);
        message.put(Constants.KEY_LATEST_TIMESTAMP, new Date());

        // Menambahkan dokumen baru ke koleksi "messages"
        messagesCollection.add(message).addOnSuccessListener(documentReference -> {
            // Menambahkan pesan ke koleksi "chat"
            addChatMessage(targetUserId, messageValue);
            binding.inputMessage.setText(null);
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Gagal menambahkan pesan", e);
        });
    }

    private void updateMessage(DocumentReference messageRef, String messageValue) {
        HashMap<String, Object> updateData = new HashMap<>();
        updateData.put(Constants.KEY_LATEST_SENDER, preferenceManager.getString(Constants.KEY_USER_ID));
        updateData.put(Constants.KEY_LATEST_MESSAGE, messageValue);
        updateData.put(Constants.KEY_LATEST_TIMESTAMP, new Date());

        // Melakukan update pada dokumen yang sudah ada di koleksi "messages"
        messageRef.update(updateData).addOnSuccessListener(aVoid -> {
            // Menambahkan pesan ke koleksi "chat"
            addChatMessage(getIntent().getExtras().getString(Constants.KEY_TARGET_ID), messageValue);
            binding.inputMessage.setText(null);
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Gagal mengupdate pesan", e);
        });
    }


    private void addChatMessage(String targetUserId, String messageValue) {
        HashMap<String, Object> chatMessage = new HashMap<>();
        chatMessage.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        chatMessage.put(Constants.KEY_RECEIVER_ID, targetUserId);
        chatMessage.put(Constants.KEY_MESSAGE, messageValue);
        chatMessage.put(Constants.KEY_TIMESTAMP, new Date());

        database.collection(Constants.KEY_COLLECTION_CHAT).add(chatMessage);
    }

}