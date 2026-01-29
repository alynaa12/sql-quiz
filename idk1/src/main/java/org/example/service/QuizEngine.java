package org.example.service;

import org.example.model.Question;
import org.example.model.RepetitionItem;
import org.example.persistence.QuestionRepository;
import org.example.persistence.RepetitionRepository;
import org.example.persistence.UserRepository;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Core quiz logic (no GUI):
 * - Provides next questions
 * - Manages repetition pool (spaced repetition)
 * - Awards +1 point after a full round of normal questions
 */
public class QuizEngine {

    private final String username;

    private final QuestionRepository questionRepo = new QuestionRepository();
    private final RepetitionRepository repetitionRepo = new RepetitionRepository();
    private final UserRepository userRepo = new UserRepository();

    private final List<Question> allQuestions;
    private int normalIndex = 0;

    // repetition queue contains question IDs
    private final Deque<Integer> repetitionQueue = new ArrayDeque<>();
    private int normalCounterSinceRepetition = 0;

    // tracks whether the CURRENT question came from repetition
    private boolean lastWasRepetition = false;

    // round/points: +1 after ALL normal questions answered once
    private int normalAnsweredThisRound = 0;
    private int pointsAwardedCount = 0;

    private Question currentQuestion;

    public QuizEngine(String username) {
        this.username = username;
        this.allQuestions = questionRepo.loadAllQuestions();

        // load repetition items for this user
        for (RepetitionItem item : repetitionRepo.loadAll()) {
            if (item.getUsername().equals(username)) {
                repetitionQueue.addLast(item.getQuestionId());
            }
        }
    }

    /**
     * Returns the next question.
     * After 3 normal questions, a repetition question is asked if available.
     */
    public Question nextQuestion() {
        // after 3 normal questions: ask repetition if available
        if (normalCounterSinceRepetition >= 3 && !repetitionQueue.isEmpty()) {
            int qId = repetitionQueue.removeFirst();
            currentQuestion = findById(qId);

            lastWasRepetition = true;
            normalCounterSinceRepetition = 0;

            return currentQuestion;
        }

        // otherwise normal question (loop through list)
        if (allQuestions.isEmpty()) {
            throw new IllegalStateException("No questions available in questions.json");
        }

        if (normalIndex >= allQuestions.size()) {
            normalIndex = 0;
        }

        currentQuestion = allQuestions.get(normalIndex);
        normalIndex++;

        lastWasRepetition = false;
        normalCounterSinceRepetition++;

        return currentQuestion;
    }

    /**
     * Checks the selected answer index against the correct index.
     */
    public boolean submitAnswer(int chosenIndex) {
        return chosenIndex == currentQuestion.getCorrectIndex();
    }

    /**
     * Stores repetition decision and awards points after a full normal round.
     * Rule: wrong OR difficulty == "schwer" => put into repetition pool.
     * Points: +1 after all normal questions answered once (repetition does not count).
     */
    public void submitDifficulty(boolean wasCorrect, String difficulty) {
        // 1) repetition rule
        if (!wasCorrect || "schwer".equalsIgnoreCase(difficulty)) {
            addToRepetitionIfMissing(currentQuestion.getId());
            saveRepetitionToFile();
        }

        // 2) point rule: only count NORMAL questions (not repetition)
        if (!lastWasRepetition) {
            normalAnsweredThisRound++;

            if (normalAnsweredThisRound >= allQuestions.size()) { // e.g. 11
                normalAnsweredThisRound = 0;
                userRepo.increaseHighscore(username, 1);
                pointsAwardedCount++;
            }
        }
    }

    private void addToRepetitionIfMissing(int questionId) {
        if (!repetitionQueue.contains(questionId)) {
            repetitionQueue.addLast(questionId);
        }
    }

    private void saveRepetitionToFile() {
        List<RepetitionItem> all = repetitionRepo.loadAll();

        // remove old items of this user
        all.removeIf(item -> item.getUsername().equals(username));

        // write current queue
        for (Integer qId : repetitionQueue) {
            all.add(new RepetitionItem(username, qId));
        }

        repetitionRepo.saveAll(all);
    }

    private Question findById(int id) {
        for (Question q : allQuestions) {
            if (q.getId() == id) return q;
        }
        // fallback
        return allQuestions.get(0);
    }

    /**
     * Used by GUI to show the "+1 point" message exactly when a point was awarded.
     */
    public int getPointsAwardedCount() {
        return pointsAwardedCount;
    }

    /**
     * Keep this for compatibility if your controller/view still calls it.
     * Since points are now awarded per full round, this will stay at 0.
     */
    public int getRepetitionRoundPointsGiven() {
        return 0;
    }
}
