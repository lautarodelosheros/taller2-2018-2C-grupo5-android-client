package com.comprame.overview;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.comprame.R;

public class AnswerQuestionPopupViewModel extends AndroidViewModel {

    public final MutableLiveData<String> id;
    public final MutableLiveData<String> question;
    public final MutableLiveData<String> questioner;
    public final MutableLiveData<String> answer;
    public final MutableLiveData<String> responder;
    public final MutableLiveData<Boolean> enabled;

    public AnswerQuestionPopupViewModel(Application application) {
        super(application);
        id = new MutableLiveData<>();
        question = new MutableLiveData<>();
        questioner = new MutableLiveData<>();
        answer = new MutableLiveData<>();
        responder = new MutableLiveData<>();
        enabled = new MutableLiveData<>();
        answer.observeForever((s) -> enabled.setValue(this.isEnabled()));
    }

    public String answerError() {
        String answerValue = answer.getValue();
        if (answerValue == null
                || answerValue.length() < 2) {
            return getApplication().getString(R.string.answer_error);
        }
        return null;
    }

    private boolean isEnabled() {
        return answerError() == null;
    }

    public Question asQuestion() {
        return new Question(
                this.id.getValue(),
                this.question.getValue(),
                this.questioner.getValue(),
                this.answer.getValue(),
                this.responder.getValue()
        );
    }
}
