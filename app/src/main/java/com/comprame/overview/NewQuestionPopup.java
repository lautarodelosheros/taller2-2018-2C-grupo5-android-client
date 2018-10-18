package com.comprame.overview;

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
    private Fragment fragment;
    private Consumer<View> onSave;
    private PopupWindow popupWindow;

    public NewQuestionPopup(Fragment fragment, NewQuestionPopupViewModel newQuestionPopupViewModel
            , Consumer<View> onSave) {
        this.fragment = fragment;
        this.onSave = onSave;
        NewQuestionPopupBinding newQuestionPopupBinding
                = NewQuestionPopupBinding.inflate(LayoutInflater.from(fragment.getContext()));
        newQuestionPopupBinding.setOwner(this);
        newQuestionPopupBinding.setNewQuestionPopupViewModel(newQuestionPopupViewModel);
        newQuestionPopupBinding.setLifecycleOwner(fragment);
        popupWindow = new PopupWindow(newQuestionPopupBinding.getRoot()
                , LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.WRAP_CONTENT
                , true);
    }

    @NonNull
    public void show() {
        popupWindow.showAtLocation(fragment.getView(), Gravity.CENTER, 0, 0);
    }

    public void save(View view) {
        popupWindow.dismiss();
        onSave.accept(view);
    }
}
