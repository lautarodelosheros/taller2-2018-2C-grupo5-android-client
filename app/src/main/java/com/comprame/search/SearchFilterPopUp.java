package com.comprame.search;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.comprame.databinding.SearchFilterBinding;
import com.comprame.library.fun.Consumer;

public class SearchFilterPopUp {
    private Consumer<View> onSearch;
    private AlertDialog popupWindow;
    private Fragment fragment;

    public SearchFilterPopUp(Fragment fragment, SearchViewModel searchViewModel
            ,
                             Consumer<View> onSearch) {
        this.fragment = fragment;
        this.onSearch = onSearch;
        SearchFilterBinding filterBinding
                = SearchFilterBinding.inflate(LayoutInflater.from(fragment.getContext()));
        filterBinding.setOwner(this);
        filterBinding.setSearchViewModel(searchViewModel);
        filterBinding.setLifecycleOwner(fragment);

        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
        builder.setCancelable(true);
        builder.setView(filterBinding.getRoot());

        popupWindow = builder.create();
    }

    public void show() {
        popupWindow.show();
    }

    public void search(View view) {
        popupWindow.dismiss();
        onSearch.accept(view);
    }
}
