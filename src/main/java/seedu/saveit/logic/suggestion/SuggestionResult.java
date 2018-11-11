package seedu.saveit.logic.suggestion;

import java.util.LinkedList;

/**
 * Represents the result of a {@code Suggestion}
 */
public class SuggestionResult {

    private final LinkedList<SuggestionValue> values;

    // start & end positions between which to replace the String with a selected option
    private final int startPosition;
    private final int endPosition;
    private final String feedbackToUser;
    private final String oldValue;

    public SuggestionResult(LinkedList<SuggestionValue> values, String feedbackToUser, String oldValue,
            int startPosition, int endPosition) {
        this.values = values;
        this.oldValue = oldValue;
        this.feedbackToUser = feedbackToUser;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public LinkedList<SuggestionValue> getSuggestionValue() {
        return values;
    }

    public String getOldValue() {
        return oldValue;
    }

    /**
     * A valid result should have suggestion labels
     */
    public boolean validResult() {
        return values.size() > 0;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof SuggestionResult)) {
            return false;
        }

        SuggestionResult otherResult = (SuggestionResult) other;
        return otherResult.values.equals(values)
                && otherResult.oldValue.equals(oldValue)
                && otherResult.startPosition == startPosition
                && otherResult.endPosition == endPosition;
    }
}
