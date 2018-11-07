package com.comprame.search;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comprame.R;
import com.comprame.databinding.SearchFilterBinding;
import com.comprame.library.fun.Consumer;

public class SearchFilterPopUp {
    private Consumer<View> onSearch;
    private AlertDialog popupWindow;
    private SearchFragment searchFragment;
    private SearchFilterBinding filterBinding;

    public SearchFilterPopUp(SearchFragment fragment, SearchViewModel searchViewModel
            ,
                             Consumer<View> onSearch) {
        this.searchFragment = fragment;
        this.onSearch = onSearch;
        filterBinding = SearchFilterBinding.inflate(LayoutInflater.from(fragment.getContext()));
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

    public void openPlacePicker(View view) {
        this.searchFragment.openPlacePicker(view);
    }

    public void search(View view) {
        popupWindow.dismiss();
        onSearch.accept(view);
    }

    public void addAddress(String placeName) {
        LinearLayout linearLayout = filterBinding.getRoot().findViewById(R.id.list_geolocation_filter);
        linearLayout.removeAllViews();
        TextView textView = new TextView(filterBinding.getRoot().getContext());
        textView.setText(placeName);
        linearLayout.addView(textView);
    }
}
