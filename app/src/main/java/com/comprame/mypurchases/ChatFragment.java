package com.comprame.mypurchases;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.comprame.App;
import com.comprame.R;
import com.comprame.library.rest.Headers;
import com.comprame.login.Session;
import com.comprame.login.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChatFragment extends Fragment {

    private CollectionReference chatCollection;
    private ChatViewModel chatViewModel;
    private RecyclerView recyclerView;

    private User user;
    private MyPurchase purchase;

    public void setPurchase(MyPurchase purchase) {
        this.purchase = purchase;
    }

    @Override
    public View onCreateView(LayoutInflater inflater
            , ViewGroup container
            , Bundle savedInstanceState) {
        chatViewModel = ViewModelProviders.of(this)
                .get(ChatViewModel.class);

        ViewDataBinding view =
                DataBindingUtil.inflate(inflater
                        , R.layout.chat_fragment
                        , container
                        , false);

        recyclerView = view.getRoot().findViewById(R.id.chat_messages);
        recyclerView.getItemAnimator().setAddDuration(1000);
        recyclerView.getItemAnimator().setChangeDuration(1000);
        recyclerView.getItemAnimator().setMoveDuration(1000);
        recyclerView.getItemAnimator().setRemoveDuration(1000);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        ImageView sendButton = view.getRoot().findViewById(R.id.send_button);
        sendButton.setOnClickListener((v) -> sendMessage(sendButton));

        chatCollection = FirebaseFirestore.getInstance().collection(purchase.id);

        chatCollection.addSnapshotListener((v, e) -> loadChat());

        loadProfile();

        return view.getRoot();
    }

    private void loadChat() {
        chatViewModel.removeAllMessages();
        chatCollection
                .orderBy("TIMESTAMP")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String userName = document.getData().get("USERNAME").toString();
                            String message = document.getData().get("MESSAGE").toString();
                            String timestamp = document.getData().get("TIMESTAMP").toString();
                            chatViewModel.addChatMessage(new ChatMessage(userName, message, timestamp));
                        }
                        recyclerView.smoothScrollToPosition(chatViewModel.size());
                    } else {
                        Toast.makeText(getContext(), "Error al leer", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadProfile() {
        App.appServer.get("/user/"
                , User.class
                , Headers.Authorization(Session.getInstance()))
                .run(
                        (User user) -> {
                            if (user.getName().isEmpty()) {
                                Toast.makeText(getContext()
                                        , "No se encuentra el usuario"
                                        , Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                this.user = user;
                                setAdapter();
                            }
                        }
                        , (Exception ex) -> {
                            Log.d("ChatProfileListener", "Error al recuperar el perfil", ex);
                            Toast.makeText(getContext()
                                    , "Error al cargar el perfil"
                                    , Toast.LENGTH_LONG)
                                    .show();
                        }
                );
    }

    private void setAdapter() {
        ChatAdapter chatAdapter = new ChatAdapter(chatViewModel, user.getName());
        recyclerView.setAdapter(chatAdapter);
    }

    public void sendMessage(View view) {
        EditText messageInput = view.getRootView().findViewById(R.id.message_input);
        Map<String, Object> newMessage = new HashMap<>();
        newMessage.put("USERNAME", this.user.getName());
        newMessage.put("MESSAGE", messageInput.getText().toString());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        newMessage.put("TIMESTAMP", format.format(Calendar.getInstance().getTime()));
        messageInput.setText("");
        chatCollection.document(Calendar.getInstance().getTime().toString()).set(newMessage)
                .addOnSuccessListener((v) -> {} )
                .addOnFailureListener((e) ->
                        Toast.makeText(getContext(), "Error enviando el mensaje. Intente nuevamente", Toast.LENGTH_SHORT).show()
                );
    }

}
