package com.comprame.overview;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comprame.R;
import com.comprame.databinding.QuestionItemBinding;
import com.comprame.search.SearchItem;

import java.util.ArrayList;
import java.util.List;

public class QuestionsList {

    private LinearLayout questionsList;
    private OverviewFragment overviewFragment;

    public QuestionsList(LinearLayout questionsList, OverviewFragment overviewFragment) {
        this.overviewFragment = overviewFragment;
        this.questionsList = questionsList;
    }

    public void setQuestions(List<Question> questions) {
        questionsList.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(questionsList.getContext());
        for (Question question : questions) {
            View view  = inflater.inflate(R.layout.question_item, questionsList, false);
            ((TextView) view.findViewById(R.id.question_id)).setText(question.id);
            ((TextView) view.findViewById(R.id.question)).setText(question.question);
            ((TextView) view.findViewById(R.id.questioner)).setText(question.questioner);
            view.findViewById(R.id.answer_button)
                    .setOnClickListener(v -> {
                        AnswerQuestionPopup answerQuestionPopup = new AnswerQuestionPopup(overviewFragment
                                , overviewFragment.answerQuestionPopupViewModel
                                , overviewFragment::editQuestion);
                        String id = ((TextView) view.findViewById(R.id.question_id)).getText().toString();
                        overviewFragment.answerQuestionPopupViewModel.id.setValue(id);
                        String questionStr = ((TextView) view.findViewById(R.id.question)).getText().toString();
                        overviewFragment.answerQuestionPopupViewModel.question.setValue(questionStr);
                        String questioner = ((TextView) view.findViewById(R.id.questioner)).getText().toString();
                        overviewFragment.answerQuestionPopupViewModel.questioner.setValue(questioner);
                        answerQuestionPopup.show();
                    });
            if (question.answer != null) {
                ((TextView) view.findViewById(R.id.answer)).setText(question.answer);
                ((TextView) view.findViewById(R.id.responder)).setText(question.responder);
                view.findViewById(R.id.answer).setVisibility(View.VISIBLE);
                view.findViewById(R.id.responder).setVisibility(View.VISIBLE);
                view.findViewById(R.id.answer_button).setVisibility(View.GONE);
            }
            questionsList.addView(view);
        }
    }

}
