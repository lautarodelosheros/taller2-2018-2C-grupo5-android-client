package com.comprame.search;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.comprame.R;
import com.comprame.databinding.SearchItemBinding;

public class SearchItemsAdapter extends RecyclerView.Adapter<SearchItemsAdapter.SearchItemViewHolder> {

    private SearchViewModel items;

    public SearchItemsAdapter(SearchViewModel items) {
        this.items = items;
        this.items.observeForever(itemsList -> this.notifyDataSetChanged());
    }

    @NonNull
    @Override
    public SearchItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchItemBinding searchItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                        , R.layout.search_item
                        , parent
                        , false);
        return new SearchItemViewHolder(searchItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemViewHolder holder, int position) {
        holder.searchItem.setSearchItemModel(new SearchItemViewModel(items.at(position)));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class SearchItemViewHolder extends RecyclerView.ViewHolder {

        private final SearchItemBinding searchItem;

        SearchItemViewHolder(final SearchItemBinding searchItem) {
            super(searchItem.getRoot());
            this.searchItem = searchItem;
        }

    }
}

