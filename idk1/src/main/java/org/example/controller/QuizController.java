package org.example.controller;

import org.example.model.Question;
import org.example.service.QuizEngine;

/**
 * Controller for the quiz use cases.
 * GUI (QuizView) calls this controller; all logic stays in QuizEngine.
 */
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

    /**
     * How many points were awarded so far (after completing full rounds).
     */
    public int getPointsAwardedCount() {
        return engine.getPointsAwardedCount();
    }

    // (Optional) keep this only if you still use it anywhere.
    // If not used, you can delete it.
    public int getRepetitionRoundPointsGiven() {
        return engine.getRepetitionRoundPointsGiven();
    }
}
