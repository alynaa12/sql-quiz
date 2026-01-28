package org.example.model;

import java.util.List;

public class Question {

    private int id;
    private String text;
    private List<String> options;
    private int correctIndex;

    // leerer Konstruktor für JSON
    public Question() {
    }

    public Question(int id, String text, List<String> options, int correctIndex) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }
    public void setText(String text) {
        this.text = text;
    }

    public void setOptions(java.util.List<String> options) {
        this.options = options;
    }

    public void setCorrectIndex(int correctIndex) {
        this.correctIndex = correctIndex;
    }

}

