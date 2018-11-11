package seedu.saveit.logic;

import seedu.saveit.logic.suggestion.Suggestion;
import seedu.saveit.logic.suggestion.SuggestionResult;

/**
 * API of the SuggestionLogic component
 * used to handle the logic regarding the handling of suggestions
 */
public interface SuggestionLogic {

    void updateCaretPosition(int position);

    Suggestion parseUserInput(String input);

    SuggestionResult evaluate(String userInput);
}
