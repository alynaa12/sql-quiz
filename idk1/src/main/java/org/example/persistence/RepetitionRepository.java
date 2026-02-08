package org.example.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.model.RepetitionItem;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for persisting repetition data
 * This class stores and loads {@link RepetitionItem} objects from a JSON File each  entry links a user to a question that should be repeated as part of the space repetition mechanism
 */

public class RepetitionRepository {

    private static final String REP_FILE = "data/repetition.json";

    /**
     * loads all repetition items from the JSON file
     * @return list of repetition itema, or an empty list if none exist
     */
    public List<RepetitionItem> loadAll() {
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<RepetitionItem>>() {}.getType();
            List<RepetitionItem> items = gson.fromJson(new FileReader(REP_FILE), listType);
            return items != null ? items : new ArrayList<>();
        } catch (Exception e) {
            // return empty list if file does not exist or cannot be read
            return new ArrayList<>();
        }
    }

    /**
     * saves all repetition items to the JSON file
     * @param items list of repetition items to persist
     */

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

