package com.comprame.overview;

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
    private Fragment fragment;
    private Consumer<View> onAnswer;
    private PopupWindow popupWindow;

    public AnswerQuestionPopup(Fragment fragment, AnswerQuestionPopupViewModel answerQuestionPopupViewModel
            , Consumer<View> onAnswer) {
        this.fragment = fragment;
        this.onAnswer = onAnswer;
        AnswerQuestionPopupBinding answerQuestionPopupBinding
                = AnswerQuestionPopupBinding.inflate(LayoutInflater.from(fragment.getContext()));
        answerQuestionPopupBinding.setOwner(this);
        answerQuestionPopupBinding.setAnswerQuestionPopupViewModel(answerQuestionPopupViewModel);
        answerQuestionPopupBinding.setLifecycleOwner(fragment);
        popupWindow = new PopupWindow(answerQuestionPopupBinding.getRoot()
                , LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.WRAP_CONTENT
                , true);
    }

    @NonNull
    public void show() {
        popupWindow.showAtLocation(fragment.getView(), Gravity.CENTER, 0, 0);
    }

    public void answer(View view) {
        popupWindow.dismiss();
        onAnswer.accept(view);
    }
}
