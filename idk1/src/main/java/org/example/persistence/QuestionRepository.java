package org.example.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.model.Question;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class QuestionRepository {

    private static final String QUESTION_FILE = "data/questions.json";

    public List<Question> loadAllQuestions() {
        try {
            Gson gson = new Gson();
            Type questionListType = new TypeToken<List<Question>>() {}.getType();
            return gson.fromJson(new FileReader(QUESTION_FILE), questionListType);
        } catch (Exception e) {
            throw new RuntimeException("Could not load questions", e);
        }
    }
}

