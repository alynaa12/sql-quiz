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
 * - Adds questions to repetition when wrong or rated "schwer"
 * - Removes questions from repetition when correct and rated "leicht/mittel"
 * - Awards +1 point after a full round of normal questions (all questions once)
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

    // points: +1 after ALL normal questions answered once (repetition does not count)
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
        if (allQuestions.isEmpty()) {
            throw new IllegalStateException("No questions available in questions.json");
        }

        // after 3 normal questions: ask repetition if available
        if (normalCounterSinceRepetition >= 3 && !repetitionQueue.isEmpty()) {
            int qId = repetitionQueue.removeFirst();
            currentQuestion = findById(qId);

            lastWasRepetition = true;
            normalCounterSinceRepetition = 0;

            return currentQuestion;
        }

        // otherwise normal question (loop through list)
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
     * Rule:
     *  - wrong OR difficulty == "schwer" => put into repetition pool
     *  - correct AND difficulty != "schwer" => remove from repetition pool (if present)
     *
     * Points:
     *  - +1 after all normal questions were answered once
     *  - repetition questions do not count towards the round
     */
    public void submitDifficulty(boolean wasCorrect, String difficulty) {
        int id = currentQuestion.getId();
        boolean isHard = "schwer".equalsIgnoreCase(difficulty);

        // 1) repetition pool logic
        if (!wasCorrect || isHard) {
            addToRepetitionIfMissing(id);
        } else {
            // correct AND not hard -> remove from repetition (fixes repeated Q1/Q2)
            repetitionQueue.removeIf(qId -> qId == id);
        }

        // always persist repetition changes
        saveRepetitionToFile();

        // 2) points logic (only NORMAL questions)
        if (!lastWasRepetition) {
            normalAnsweredThisRound++;

            if (normalAnsweredThisRound >= allQuestions.size()) {
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
     * Points are awarded per full normal round, not per repetition round.
     */
    public int getRepetitionRoundPointsGiven() {
        return 0;
    }
}