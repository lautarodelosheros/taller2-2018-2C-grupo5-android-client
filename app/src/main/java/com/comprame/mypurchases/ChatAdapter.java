package com.comprame.mypurchases;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.comprame.R;
import com.comprame.databinding.ChatMessageItemBinding;
import com.comprame.databinding.SearchItemBinding;
import com.comprame.search.SearchFragment;
import com.comprame.search.SearchItem;
import com.comprame.search.SearchItemViewModel;
import com.comprame.search.SearchViewModel;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatMessageItemViewHolder> {

    private ChatViewModel chatViewModel;
    private String userName;

    public ChatAdapter(ChatViewModel chatViewModel, String userName) {
        this.chatViewModel = chatViewModel;
        this.chatViewModel.observeForever(chatMessages -> this.notifyDataSetChanged());
        this.userName = userName;
    }

    @NonNull
    @Override
    public ChatMessageItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatMessageItemBinding chatBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                        , R.layout.chat_message_item
                        , parent
                        , false);

        return new ChatMessageItemViewHolder(chatBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageItemViewHolder holder, int position) {
        ChatMessage chatMessage = chatViewModel.chatMessages.getValue().get(position);
        holder.chatMessageItemBinding.setChatMessageViewModel(new ChatMessageItemViewModel(chatMessage, userName));
    }

    @Override
    public int getItemCount() {
        return chatViewModel.size();
    }

    class ChatMessageItemViewHolder extends RecyclerView.ViewHolder {

        private final ChatMessageItemBinding chatMessageItemBinding;

        ChatMessageItemViewHolder(final ChatMessageItemBinding chatMessageItemBinding) {
            super(chatMessageItemBinding.getRoot());
            this.chatMessageItemBinding = chatMessageItemBinding;
        }

    }
}

