package com.comprame.mypurchases;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.comprame.R;
import com.comprame.buy.BuyItem;
import com.comprame.databinding.MyPurchasesItemBinding;

public class MyPurchasesItemsAdapter extends RecyclerView.Adapter<MyPurchasesItemsAdapter.MyPurchasesItemViewHolder> {

    private final MyPurchasesFragment myPurchasesFragment;
    private MyPurchasesViewModel items;

    public MyPurchasesItemsAdapter(MyPurchasesViewModel items, MyPurchasesFragment myPurchasesFragment) {
        this.items = items;
        this.myPurchasesFragment = myPurchasesFragment;
        this.items.observeForever(itemsList -> this.notifyDataSetChanged());
    }

    @NonNull
    @Override
    public MyPurchasesItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyPurchasesItemBinding myPurchasesItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                        , R.layout.my_purchases_item
                        , parent
                        , false);

        return new MyPurchasesItemViewHolder(myPurchasesItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPurchasesItemViewHolder holder, int position) {
        BuyItem buyItem = items.items.getValue().get(position);
        holder.buyItem.setMyPurchasesItemModel(new MyPurchasesItemViewModel(buyItem));
        holder.buyItem
                .getRoot()
                .setOnClickListener((l) -> myPurchasesFragment.overviewMyPurchase(buyItem));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void removeAllItems() {
        this.items.items.getValue().clear();
        this.notifyDataSetChanged();
    }

    class MyPurchasesItemViewHolder extends RecyclerView.ViewHolder {

        private final MyPurchasesItemBinding buyItem;

        MyPurchasesItemViewHolder(final MyPurchasesItemBinding buyItem) {
            super(buyItem.getRoot());
            this.buyItem = buyItem;
        }

    }
}

