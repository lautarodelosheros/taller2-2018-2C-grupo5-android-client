package com.comprame.overview;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.comprame.databinding.NewQuestionPopupBinding;
import com.comprame.library.fun.Consumer;

public class NewQuestionPopup {
    private Consumer<View> onSave;
    private AlertDialog popupWindow;

    public NewQuestionPopup(Fragment fragment, NewQuestionPopupViewModel newQuestionPopupViewModel
            , Consumer<View> onSave) {
        this.onSave = onSave;
        NewQuestionPopupBinding newQuestionPopupBinding
                = NewQuestionPopupBinding.inflate(LayoutInflater.from(fragment.getContext()));
        newQuestionPopupBinding.setOwner(this);
        newQuestionPopupBinding.setNewQuestionPopupViewModel(newQuestionPopupViewModel);
        newQuestionPopupBinding.setLifecycleOwner(fragment);

        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
        builder.setCancelable(true);
        builder.setView(newQuestionPopupBinding.getRoot());

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
