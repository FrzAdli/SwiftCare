package com.example.swiftcare.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swiftcare.R;
import com.example.swiftcare.activities.ChatActivity;
import com.example.swiftcare.databinding.MessageSingleViewBinding;
import com.example.swiftcare.models.Chat;
import com.example.swiftcare.models.Donation;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.ImageLoader;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageSingleViewholder> {
    private List<Chat> chatList;
    private Context context;
    private String senderName, latestSenderName;

    public MessageAdapter(List<Chat> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    public void setChatList(List<Chat> chatList) {
        this.chatList = chatList;
        notifyDataSetChanged();
    }

    public void clear() {
        chatList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageSingleViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageSingleViewholder(
                MessageSingleViewBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MessageSingleViewholder holder, int position) {
        Chat chat = chatList.get(position);

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Load data for sender
        if (chat.getSenderId() != null) {
            DocumentReference userDocument = database.collection("users").document(chat.getSenderId());
            userDocument.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    senderName = documentSnapshot.getString(Constants.KEY_USERNAME);
                    ImageLoader.loadCircleImage(documentSnapshot.getString(Constants.KEY_IMAGE_PROFILE), holder.binding.accountImage);

                    // Load data for latest sender only after loading data for sender
                    loadLatestSenderData(holder, chat, senderName);
                } else {
                    Log.d("Firestore", "Dokumen tidak ditemukan");
                }
            }).addOnFailureListener(e -> {
                Log.e("Firestore", "Gagal mengambil data", e);
            });
        }

        holder.binding.layoutMessage.setOnClickListener(v -> {
            Intent i = new Intent(context, ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_TARGET_ID, chat.getSenderId());
            i.putExtras(bundle);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        });
    }

    private void loadLatestSenderData(MessageSingleViewholder holder, Chat chat, String senderName) {
        if (chat.getLatestSender() != null) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            DocumentReference userDocument = database.collection("users").document(chat.getLatestSender());
            userDocument.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    latestSenderName = documentSnapshot.getString(Constants.KEY_USERNAME);
                    String latestSender = latestSenderName + ": ";

                    // Update UI or anything you want with the loaded data
                    holder.binding.accountName.setText(senderName);
                    holder.binding.senderName.setText(latestSender);
                    holder.binding.message.setText(chat.getMessage());
                    holder.binding.time.setText(chat.getDateTime());
                } else {
                    Log.d("Firestore", "Dokumen tidak ditemukan");
                }
            }).addOnFailureListener(e -> {
                Log.e("Firestore", "Gagal mengambil data", e);
            });
        }
    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    static class MessageSingleViewholder extends RecyclerView.ViewHolder {
        private final MessageSingleViewBinding binding;

        MessageSingleViewholder(@NonNull MessageSingleViewBinding messageSingleViewBinding) {
            super(messageSingleViewBinding.getRoot());
            binding = messageSingleViewBinding;
        }
    }
}
