package com.comprame.categories;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.comprame.R;
import com.comprame.databinding.CategoryItemBinding;
import com.comprame.search.SearchFragment;
import com.comprame.search.SearchItem;
import com.comprame.search.SearchItemViewModel;
import com.comprame.search.SearchViewModel;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private final CategoriesFragment categoriesFragment;
    private CategoriesViewModel categoriesViewModel;

    public CategoriesAdapter(CategoriesViewModel categoriesViewModel, CategoriesFragment categoriesFragment) {
        this.categoriesViewModel = categoriesViewModel;
        this.categoriesFragment = categoriesFragment;
        this.categoriesViewModel.observeForever(itemsList -> this.notifyDataSetChanged());
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryItemBinding categoryBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                        , R.layout.category_item
                        , parent
                        , false);

        return new CategoryViewHolder(categoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String category = categoriesViewModel.at(position);
        holder.categoryBinding.setCategoryModel(new CategoryViewModel(category));
        holder.categoryBinding
                .getRoot()
                .setOnClickListener((l) -> categoriesFragment.goSearch(category));
    }

    @Override
    public int getItemCount() {
        return categoriesViewModel.size();
    }

    public void removeAllItems() {
        this.categoriesViewModel.removeAllItems();
        this.notifyDataSetChanged();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final CategoryItemBinding categoryBinding;

        CategoryViewHolder(final CategoryItemBinding categoryBinding) {
            super(categoryBinding.getRoot());
            this.categoryBinding = categoryBinding;
        }

    }
}

