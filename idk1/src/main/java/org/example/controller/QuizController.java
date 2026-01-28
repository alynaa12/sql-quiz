package org.example.controller;

import org.example.model.Question;
import org.example.service.QuizEngine;

public class QuizController {

    private final QuizEngine engine;

    public QuizController(String username) {
        this.engine = new QuizEngine(username);
    }

    public Question nextQuestion() {
        return engine.nextQuestion();
    }

    public boolean submitAnswer(int chosenIndex) {
        return engine.submitAnswer(chosenIndex);
    }

    public void submitDifficulty(boolean wasCorrect, String difficulty) {
        engine.submitDifficulty(wasCorrect, difficulty);
    }

    public int getRepetitionRoundPointsGiven() {
        return engine.getRepetitionRoundPointsGiven();
    }
}
