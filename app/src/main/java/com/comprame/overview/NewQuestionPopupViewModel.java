package com.comprame.overview;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.comprame.R;
import com.comprame.login.User;
import com.comprame.search.SearchItem;

import java.util.ArrayList;
import java.util.List;

public class NewQuestionPopupViewModel extends ViewModel {

    private final MutableLiveData<String> question;
    private final MutableLiveData<String> questioner;
    public final MutableLiveData<Boolean> enabled;

    public NewQuestionPopupViewModel() {
        question = new MutableLiveData<>();
        questioner = new MutableLiveData<>();
        enabled = new MutableLiveData<>();
        question.observeForever((s) -> enabled.setValue(this.isEnabled()));
        questioner.observeForever((s) -> enabled.setValue(this.isEnabled()));
    }

    public String questionError() {
        String questionValue = question.getValue();
        if (questionValue == null
                || questionValue.length() < 2) {
            return getApplication().getString(R.string.question_error);
        }
        return null;
    }

    private boolean isEnabled() {
        return questionError() == null;
    }

    public Question asQuestion() {
        return new Question(
                this.question.getValue(),
                this.questioner.getValue()
        );
    }
}
