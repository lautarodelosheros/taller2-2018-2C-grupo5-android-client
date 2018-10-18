package com.comprame.overview;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
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

    private List<Question> questions;

    private LinearLayout questionsList;

    public QuestionsList(LinearLayout questionsList) {
        this.questions = new ArrayList<>();
        this.questionsList = questionsList;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        questionsList.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(questionsList.getContext());
        for (Question question : questions) {
            View view  = inflater.inflate(R.layout.question_item, questionsList, false);
            ((TextView) view.findViewById(R.id.question)).setText(question.question);
            ((TextView) view.findViewById(R.id.questioner)).setText(question.questioner);
            ((TextView) view.findViewById(R.id.answer)).setText(question.answer);
            ((TextView) view.findViewById(R.id.responder)).setText(question.responder);
            questionsList.addView(view);
        }
    }

}
