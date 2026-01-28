package org.example.model;

public class RepetitionItem {

    private String username;
    private int questionId;

    public RepetitionItem() {
    }

    public RepetitionItem(String username, int questionId) {
        this.username = username;
        this.questionId = questionId;
    }

    public String getUsername() {
        return username;
    }

    public int getQuestionId() {
        return questionId;
    }
}

