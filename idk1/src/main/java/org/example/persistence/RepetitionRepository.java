package org.example.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.model.RepetitionItem;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RepetitionRepository {

    private static final String REP_FILE = "data/repetition.json";

    public List<RepetitionItem> loadAll() {
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<RepetitionItem>>() {}.getType();
            List<RepetitionItem> items = gson.fromJson(new FileReader(REP_FILE), listType);
            return items != null ? items : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void saveAll(List<RepetitionItem> items) {
        try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(REP_FILE);
            gson.toJson(items, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("Could not save repetition items", e);
        }
    }
}

