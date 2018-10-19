package com.comprame.overview;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.comprame.databinding.AnswerQuestionPopupBinding;
import com.comprame.library.fun.Consumer;

public class AnswerQuestionPopup {
    private Consumer<View> onAnswer;
    private AlertDialog popupWindow;

    public AnswerQuestionPopup(Fragment fragment, AnswerQuestionPopupViewModel answerQuestionPopupViewModel
            , Consumer<View> onAnswer) {
        this.onAnswer = onAnswer;
        AnswerQuestionPopupBinding answerQuestionPopupBinding
                = AnswerQuestionPopupBinding.inflate(LayoutInflater.from(fragment.getContext()));
        answerQuestionPopupBinding.setOwner(this);
        answerQuestionPopupBinding.setAnswerQuestionPopupViewModel(answerQuestionPopupViewModel);
        answerQuestionPopupBinding.setLifecycleOwner(fragment);

        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
        builder.setCancelable(true);
        builder.setView(answerQuestionPopupBinding.getRoot());

        popupWindow = builder.create();
    }

    public void show() {
        popupWindow.show();
    }

    public void answer(View view) {
        popupWindow.dismiss();
        onAnswer.accept(view);
    }
}
