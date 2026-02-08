package org.example.model;

/**
 * represents a repetition entry for a user
 * a repetition item links a username with the iD of a question that needs to be repeated as part of the spaced repetition mechanism
 * Instance of this class are stored in JSON format
 */
public class RepetitionItem {
     /** username of the user who has to repeat the question */
    private String username;
    /** ID of the question that should be repeated */
    private int questionId;

    public RepetitionItem() {
    }

    /**
     * creates a new repetition item
     * @param username name of the user
     * @param questionId iD of the question to be repeated
     */

    public RepetitionItem(String username, int questionId) {
        this.username = username;
        this.questionId = questionId;
    }

    /**
     *
     * @return the username associated with thisrepetition item
     */

    public String getUsername() {
        return username;
    }

    /**
     *
     * @return the ID of the question to be repeated
     */

    public int getQuestionId() {
        return questionId;
    }
}

