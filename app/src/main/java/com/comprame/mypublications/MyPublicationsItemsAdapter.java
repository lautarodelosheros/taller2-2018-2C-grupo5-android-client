package com.comprame.mypublications;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.comprame.R;
import com.comprame.databinding.MyPublicationsItemBinding;

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
        Publication publication = items.publications.getValue().get(position);
        holder.publication.setMyPublicationsItemModel(new MyPublicationsItemViewModel(publication));
        holder.publication
                .getRoot()
                .setOnClickListener((l) -> myPublicationsFragment.overviewMyPublication(publication));
    }

    public void removeAllItems() {
        this.items.publications.getValue().clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyPublicationsItemViewHolder extends RecyclerView.ViewHolder {

        private final MyPublicationsItemBinding publication;

        MyPublicationsItemViewHolder(final MyPublicationsItemBinding publication) {
            super(publication.getRoot());
            this.publication = publication;
        }

    }
}

