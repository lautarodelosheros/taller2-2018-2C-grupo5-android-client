package com.comprame.mysellings;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.comprame.R;
import com.comprame.buy.BuyItem;
import com.comprame.databinding.MySellingsItemBinding;

public class MySellingsItemsAdapter extends RecyclerView.Adapter<MySellingsItemsAdapter.MySellingsItemViewHolder> {

    private final MySellingsFragment mySellingsFragment;
    private MySellingsViewModel items;

    public MySellingsItemsAdapter(MySellingsViewModel items, MySellingsFragment mySellingsFragment) {
        this.items = items;
        this.mySellingsFragment = mySellingsFragment;
        this.items.observeForever(itemsList -> this.notifyDataSetChanged());
    }

    @NonNull
    @Override
    public MySellingsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MySellingsItemBinding mySellingsItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                        , R.layout.my_sellings_item
                        , parent
                        , false);

        return new MySellingsItemViewHolder(mySellingsItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MySellingsItemViewHolder holder, int position) {
        BuyItem buyItem = items.items.getValue().get(position);
        holder.buyItem.setMySellingsItemModel(new MySellingsItemViewModel(buyItem));
        holder.buyItem
                .getRoot()
                .setOnClickListener((l) -> mySellingsFragment.overviewMySelling(buyItem));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MySellingsItemViewHolder extends RecyclerView.ViewHolder {

        private final MySellingsItemBinding buyItem;

        MySellingsItemViewHolder(final MySellingsItemBinding buyItem) {
            super(buyItem.getRoot());
            this.buyItem = buyItem;
        }

    }
}

