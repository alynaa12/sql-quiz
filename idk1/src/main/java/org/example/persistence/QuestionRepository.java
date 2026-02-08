package org.example.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.model.Question;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * repository responsible for persisting quiz questions
 * This class loads and stores objects from and to a JSON file
 * It abstracts file access from the rest of the application
 */

public class QuestionRepository {
/** path to the JSON file containing all quiz questions */
    private static final String QUESTION_FILE = "data/questions.json";

    /**
     * loads all questions from the JSON file
     * @return list of all stored questions, or an empty list if none exist
     */
    public List<Question> loadAllQuestions() {
        try {
            Gson gson = new Gson();
            Type questionListType = new TypeToken<List<Question>>() {}.getType();
            List<Question> questions = gson.fromJson(new FileReader(QUESTION_FILE), questionListType);
            return questions != null ? questions : new ArrayList<>();
        } catch (Exception e) {
            throw new RuntimeException("Could not load questions", e);
        }
    }

    /**
     * saves all given questions to the JSON file
     * @param questions list of question to persist
     */

    public void saveAllQuestions(List<Question> questions) {
        try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(QUESTION_FILE);
            gson.toJson(questions, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("Could not save questions", e);
        }
    }
}


