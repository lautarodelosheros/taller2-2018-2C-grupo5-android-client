package com.comprame.mypublications;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.comprame.R;
import com.comprame.buy.BuyItem;
import com.comprame.databinding.MyPublicationsItemBinding;
import com.comprame.databinding.MySellingsItemBinding;
import com.comprame.mysellings.MySellingsFragment;
import com.comprame.mysellings.MySellingsItemViewModel;
import com.comprame.mysellings.MySellingsViewModel;
import com.comprame.sell.SellItem;

public class MyPublicationsItemsAdapter extends RecyclerView.Adapter<MyPublicationsItemsAdapter.MyPublicationsItemViewHolder> {

    private final MyPublicationsFragment myPublicationsFragment;
    private MyPublicationsViewModel items;

    public MyPublicationsItemsAdapter(MyPublicationsViewModel items, MyPublicationsFragment myPublicationsFragment) {
        this.items = items;
        this.myPublicationsFragment = myPublicationsFragment;
        this.items.observeForever(itemsList -> this.notifyDataSetChanged());
    }

    @NonNull
    @Override
    public MyPublicationsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyPublicationsItemBinding myPublicationsItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                        , R.layout.my_publications_item
                        , parent
                        , false);

        return new MyPublicationsItemViewHolder(myPublicationsItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPublicationsItemViewHolder holder, int position) {
        SellItem sellItem = items.items.getValue().get(position);
        holder.sellItem.setMyPublicationsItemModel(new MyPublicationsItemViewModel(sellItem));
        holder.sellItem
                .getRoot()
                .setOnClickListener((l) -> myPublicationsFragment.overviewMyPublication(sellItem));
    }

    public void removeAllItems() {
        this.items.items.getValue().clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyPublicationsItemViewHolder extends RecyclerView.ViewHolder {

        private final MyPublicationsItemBinding sellItem;

        MyPublicationsItemViewHolder(final MyPublicationsItemBinding sellItem) {
            super(sellItem.getRoot());
            this.sellItem = sellItem;
        }

    }
}

