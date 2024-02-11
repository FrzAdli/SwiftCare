package com.example.swiftcare.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swiftcare.R;
import com.example.swiftcare.databinding.ItemContainerReceivedMessageBinding;
import com.example.swiftcare.databinding.ItemContainerSentMessageBinding;
import com.example.swiftcare.models.Chat;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.ImageLoader;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SwiftbotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Chat> chatMessages;
    private final String senderId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public SwiftbotAdapter(List<Chat> chatMessages, String senderId) {
        this.chatMessages = chatMessages;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT) {
            return new SwiftbotAdapter.SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        } else {
            return new SwiftbotAdapter.ReceivedMessageViewHolder(
                    ItemContainerReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SENT) {
            SwiftbotAdapter.SentMessageViewHolder sentHolder = (SwiftbotAdapter.SentMessageViewHolder) holder;
            Chat chatMessage = chatMessages.get(position);
            sentHolder.setData(chatMessage);
        } else {
            SwiftbotAdapter.ReceivedMessageViewHolder receivedHolder = (SwiftbotAdapter.ReceivedMessageViewHolder) holder;
            Chat chatMessage = chatMessages.get(position);
            receivedHolder.setData(chatMessage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessages.get(position).senderId.equals(senderId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(Chat chatMessage) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            if(chatMessage.getSenderId() != null) {
                DocumentReference userDocument = database.collection("users").document(chatMessage.getSenderId());
                userDocument.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        ImageLoader.loadCircleImage(documentSnapshot.getString(Constants.KEY_IMAGE_PROFILE), binding.photoImage);
                        binding.textMessage.setText(chatMessage.message);
                        binding.textDateTime.setText(chatMessage.dateTime);
                    } else {
                        Log.d("Firestore", "Dokumen tidak ditemukan");
                    }
                }).addOnFailureListener(e -> {
                    Log.e("Firestore", "Gagal mengambil data", e);
                });
            }
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding) {
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;
        }

        void setData(Chat chatMessage) {
            binding.photoImage.setImageResource(R.drawable.ic_swiftbot);
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }
}
