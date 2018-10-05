package com.comprame.search;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.comprame.databinding.SearchFilterBinding;
import com.comprame.library.fun.Consumer;

public class SearchFilterPopUp {
    private SearchViewModel searchViewModel;
    private Fragment fragment;
    private Consumer<View> onSearch;
    private PopupWindow popupWindow;

    public SearchFilterPopUp(Fragment fragment, SearchViewModel searchViewModel
            ,
                             Consumer<View> onSearch) {
        this.searchViewModel = searchViewModel;
        this.fragment = fragment;
        this.onSearch = onSearch;
        SearchFilterBinding filterBinding
                = SearchFilterBinding.inflate(LayoutInflater.from(fragment.getContext()));
        filterBinding.setOwner(this);
        filterBinding.setSearchViewModel(searchViewModel);
        filterBinding.setLifecycleOwner(fragment);
        popupWindow = new PopupWindow(filterBinding.getRoot()
                , LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.WRAP_CONTENT
                , true);
    }

    @NonNull
    public void show() {
        popupWindow.showAtLocation(fragment.getView(), Gravity.CENTER, 0, 0);
    }

    public void search(View view) {
        popupWindow.dismiss();
        onSearch.accept(view);
    }
}
