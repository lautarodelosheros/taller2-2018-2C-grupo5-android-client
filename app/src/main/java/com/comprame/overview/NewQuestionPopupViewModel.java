package com.comprame.overview;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.comprame.MainActivity;
import com.comprame.R;

public class NewQuestionPopupViewModel extends AndroidViewModel {

    public final MutableLiveData<String> question;
    public final MutableLiveData<String> questioner;
    public final MutableLiveData<Boolean> enabled;

    public NewQuestionPopupViewModel(Application application) {
        super(application);
        question = new MutableLiveData<>();
        questioner = new MutableLiveData<>();
        enabled = new MutableLiveData<>();
        question.observeForever((s) -> enabled.setValue(this.isEnabled()));
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
