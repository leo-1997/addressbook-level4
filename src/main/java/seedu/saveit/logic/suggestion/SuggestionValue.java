package seedu.saveit.logic.suggestion;

/**
 * Used to represent a CustomMenuItem which should have
 *  -> a display {@code label}
 *  -> a {@code result} (to be inserted into the {@code CommandBox.commandTextArea})
 */
public class SuggestionValue {

    private final String label;
    private final String result;

    public SuggestionValue(String label, String result) {
        this.label = label;
        this.result = result;
    }

    public String getLabel() {
        return label;
    }

    public String getResult() {
        return result;
    }

    @Override
    public boolean equals(Object other) {
        SuggestionValue otherValue = (SuggestionValue) other;

        return otherValue.label.equals(label) && otherValue.result.equals(result);
    }
}
