package com.comprame.overview;

import java.io.Serializable;

public class Question implements Serializable {
    public String question;
    public String questioner;
    public String answer;
    public String responder;

    public Question (String question, String questioner, String answer, String responder) {
        this.question = question;
        this.questioner = questioner;
        this.answer = answer;
        this.responder = responder;
    }

    public Question (String question, String questioner) {
        this.question = question;
        this.questioner = questioner;
        this.answer = null;
        this.responder = null;
    }
}
