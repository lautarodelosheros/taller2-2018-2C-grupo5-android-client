package com.comprame.sell;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.comprame.databinding.CategoryPopupBinding;
import com.comprame.library.fun.Consumer;

public class CategoryPopup {
    private Consumer<View> onSave;
    private AlertDialog popupWindow;

    public CategoryPopup(Fragment fragment, CategoryPopupViewModel categoryPopupViewModel
            , Consumer<View> onSave) {
        this.onSave = onSave;
        CategoryPopupBinding categoryPopupBinding
                = CategoryPopupBinding.inflate(LayoutInflater.from(fragment.getContext()));
        categoryPopupBinding.setOwner(this);
        categoryPopupBinding.setModel(categoryPopupViewModel);
        categoryPopupBinding.setLifecycleOwner(fragment);

        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
        builder.setCancelable(true);
        builder.setView(categoryPopupBinding.getRoot());

        popupWindow = builder.create();
    }

    public void show() {
        popupWindow.show();
    }

    public void save(View view) {
        popupWindow.dismiss();
        onSave.accept(view);
    }
}
