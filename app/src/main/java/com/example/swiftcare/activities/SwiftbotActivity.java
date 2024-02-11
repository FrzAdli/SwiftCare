package com.example.swiftcare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.swiftcare.R;
import com.example.swiftcare.adapters.ChatAdapter;
import com.example.swiftcare.adapters.SwiftbotAdapter;
import com.example.swiftcare.databinding.ActivitySwiftbotBinding;
import com.example.swiftcare.models.Chat;
import com.example.swiftcare.models.PromptMessage;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.ImageLoader;
import com.example.swiftcare.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SwiftbotActivity extends AppCompatActivity {
    private ActivitySwiftbotBinding binding;
    private PreferenceManager preferenceManager;
    private List<Chat> chatMessages;
    private SwiftbotAdapter swiftbotAdapter;
    private FirebaseFirestore database;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    List<PromptMessage> userConversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySwiftbotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        listenMessages();
        setListeners();
    }

    private void init() {
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages = new ArrayList<>();
        swiftbotAdapter = new SwiftbotAdapter(
                chatMessages,
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        binding.swiftbotRecyclerView.setAdapter(swiftbotAdapter);
        database = FirebaseFirestore.getInstance();

        String userConversationJson = preferenceManager.getString(Constants.KEY_USER_CONVERSATION);
        if (!TextUtils.isEmpty(userConversationJson)) {
            try {
                JSONArray jsonArray = new JSONArray(userConversationJson);
                userConversation = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String role = jsonObject.getString("role");
                    String content = jsonObject.getString("content");
                    userConversation.add(new PromptMessage(role, content));
                }
                saveUserConversation();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            String aboutAI = readAboutAI();
            userConversation = new ArrayList<>();
            userConversation.add(new PromptMessage("system", aboutAI));
            saveUserConversation();
        }
    }

    private void listenMessages() {
        database.collection(Constants.KEY_COLLECTION_SWIFTBOT_CHATS)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, Constants.KEY_BOT_ID)
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_SWIFTBOT_CHATS)
                .whereEqualTo(Constants.KEY_SENDER_ID, Constants.KEY_BOT_ID)
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
                swiftbotAdapter.notifyDataSetChanged();
            } else {
                swiftbotAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.swiftbotRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
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
        if(!binding.inputMessage.getText().toString().isEmpty()) {
            String question = binding.inputMessage.getText().toString();
            binding.inputMessage.setText(null);
            HashMap<String, Object> message = new HashMap<>();
            message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
            message.put(Constants.KEY_RECEIVER_ID, Constants.KEY_BOT_ID);
            message.put(Constants.KEY_MESSAGE, question);
            message.put(Constants.KEY_TIMESTAMP, new Date());
            database.collection(Constants.KEY_COLLECTION_SWIFTBOT_CHATS).add(message);
            getAnswer(question);
        }
    }

    private String readAboutAI() {
        Resources resources = getResources();
        InputStream inputStream = resources.openRawResource(R.raw.about);
        StringBuilder aboutAI = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                aboutAI.append(line);
                aboutAI.append("\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return aboutAI.toString();
    }

    private void saveUserConversation() {
        JSONArray jsonArray = new JSONArray();
        for(PromptMessage message : userConversation) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("role", message.getRole());
                jsonObject.put("content", message.getContent());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        String json = jsonArray.toString();
        preferenceManager.putString(Constants.KEY_USER_CONVERSATION, json);
    }

    void checkAndRemoveMessages() {
        int totalCharacters = 0;
        for (PromptMessage message : userConversation) {
            totalCharacters += message.getContent().length();
        }
        if (totalCharacters > 4000 && userConversation.size() > 1) {
            userConversation.remove(1);
            saveUserConversation();
        }
    }

    private void getAnswer(String question) {
        JSONObject jsonBody;
        try {
            jsonBody = new JSONObject();
            jsonBody.put("model", "gpt-3.5-turbo-0613");
            JSONArray messagesArray = new JSONArray();
            for (PromptMessage message : userConversation) {
                JSONObject userMessage = new JSONObject();
                userMessage.put("role", message.getRole());
                userMessage.put("content", message.getContent());
                messagesArray.put(userMessage);
            }
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", question);
            messagesArray.put(userMessage);
            jsonBody.put("messages", messagesArray);

            jsonBody.put("max_tokens", 700);
            jsonBody.put("top_p", 1);
            jsonBody.put("presence_penalty", 1);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer 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")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    JSONObject jsonObject;
                    try {
                        assert response.body() != null;
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        JSONObject responseMessage = jsonArray.getJSONObject(0).getJSONObject("message");
                        String responseContent = responseMessage.getString("content");

                        sendAnswer(responseContent.trim());
                        userConversation.add(new PromptMessage("assistant", responseContent.trim()));
                        saveUserConversation();
                        checkAndRemoveMessages();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    if (response.code() == 401) {
                        sendAnswer("Terjadi kesalahan karena API yang dimasukkan salah");
                    } else {
                        assert response.body() != null;
                        sendAnswer("Terjadi kesalahan karena " + response.body().toString());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                sendAnswer("Terjadi kesalahan karena " + e.getMessage());
            }

        });
    }

    private void sendAnswer(String answer) {
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, Constants.KEY_BOT_ID);
        message.put(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_MESSAGE, answer);
        message.put(Constants.KEY_TIMESTAMP, new Date());
        database.collection(Constants.KEY_COLLECTION_SWIFTBOT_CHATS).add(message);
    }
}