package org.example.controller;

import org.example.model.Question;
import org.example.persistence.QuestionRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for Question management use cases
 * This class only contains application logic for managing questions and delegates persistance to @link QuestionRepository
 * The GUI should call these methods instead of accessing repositories directly
 */
public class QuestionController {

    private final QuestionRepository questionRepo = new QuestionRepository();

    /**
     * loads and returns all questions from persistence
     * @return list of all stored questions
     */

    public List<Question> getAllQuestions() {
        return questionRepo.loadAllQuestions();
    }

    /**
     * adds a new question to the question catalog
     * a new unique iD is generated automatically
     * @param text QuestionText
     * @param options list of answer options (4)
     * @param correctIndex index of the correct option
     */

    public void addQuestion(String text, List<String> options, int correctIndex) {
        List<Question> questions = questionRepo.loadAllQuestions();

        int newId = getNextId(questions);
        Question newQuestion = new Question(newId, text, options, correctIndex);

        questions.add(newQuestion);
        questionRepo.saveAllQuestions(questions);
    }

    /**
     * updates an existing question by ID
     * @param id ID of the question to update
     * @param newText new question Text
     * @param newOptions new list of answer options
     * @param newCorrectIndex new correct answer index
     * @return {@code true} if the question was found and updated , otherwise {@code false}
     */

    public boolean updateQuestion(int id, String newText, List<String> newOptions, int newCorrectIndex) {
        List<Question> questions = questionRepo.loadAllQuestions();

        for (Question q : questions) {
            if (q.getId() == id) {
                q.setText(newText);
                q.setOptions(newOptions);
                q.setCorrectIndex(newCorrectIndex);
                questionRepo.saveAllQuestions(questions);
                return true;
            }
        }
        return false;
    }

    /**
     * delete a question by ID
     * @param id ID of the question to delete
     *
     * @return {@code true} if a question was deleted, otherwise {@code false}
     */

    public boolean deleteQuestion(int id) {
        List<Question> questions = questionRepo.loadAllQuestions();

        boolean removed = questions.removeIf(q -> q.getId() == id);
        if (removed) {
            questionRepo.saveAllQuestions(questions);
        }
        return removed;
    }

    /**
     * calculates the next free ID based on the maximum ID currently stored
     * @param questions list of existing questions
     * @return next ID
     */

    private int getNextId(List<Question> questions) {
        int max = 0;
        for (Question q : questions) {
            if (q.getId() > max) max = q.getId();
        }
        return max + 1;
    }



}
