package seedu.saveit.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.saveit.testutil.TypicalIssues.getTypicalSaveIt;

import org.junit.Test;

import seedu.saveit.logic.suggestion.Suggestion;
import seedu.saveit.model.Model;
import seedu.saveit.model.ModelManager;
import seedu.saveit.model.UserPrefs;


public class SuggestionLogicManagerTest {

    private Model model = new ModelManager(getTypicalSaveIt(), new UserPrefs());
    private SuggestionLogic suggestionLogic = new SuggestionLogicManager(model);

    // ========================= TagNameSuggestion =================================

    /**
     * Invalid inputs for {@code TagNameSuggestion}
     */
    @Test
    public void parseUserInput_nonValidInputTagNameSuggestion() {
        // Input does not invoke any Suggestions
        assertParseFailure("add i/IssueName d/Description", 29);
        // Input is valid but the caret position is wrong
        assertParseFailure("add i/IssueName d/Description t/c", 29);

        assertParseFailure("edit 1 i/IssueName d/Description t/", 35);
        assertParseFailure("edit 1 i/IssueName d/Description t/s", 30);
        // Correct format but invalid index
        assertParseFailure("edit coolIndex i/IssueName t/so", 23);
    }

    /**
     * Valid inputs for {@code TagNameSuggestion}
     */
    @Test
    public void evaluate_validSuggestionInputTagNameSuggestion() {
        assertParseSuccess("add i/issueName d/Description t/c", 33);
        assertParseSuccess("add i/issueName t/j d/Description", 19);
        // Tags do not need to exist to generate a suggestion
        assertParseSuccess("add i/issueName t/c++ t/longtag d/Description", 31);
        assertParseSuccess("add i/issueName t/c++ t/longtag d/Description", 29);

        assertParseSuccess("edit 1 i/IssueName t/so", 23);
        assertParseSuccess("edit 1 i/IssueName t/so d/description", 23);

        assertParseSuccess("findtag s", 9);
    }

    // ========================= CopyExistingSuggestion =================================

    /**
     * Invalid Inputs for {@code CopyExistingSuggestion}
     */
    @Test
    public void parseUserInput_nonValidInputCopyExistingSuggestion() {
        assertParseFailure("edit 1 i/IssueName t/", 21);
        // Valid input but wrong caret position
        assertParseFailure("edit 2 i/issueName d/", 10);
        assertParseFailure("edit 2 i/ d/description", 12);
        // Correct format but invalid index
        assertParseFailure("edit coolIndex i/issueName d/", 21);
    }

    /**
     * Valid Inputs for {@code CopyExistingSuggestion}
     */
    @Test
    public void parseUserInput_validInputCopyExistingSuggestion() {
        assertParseSuccess("edit 1 i/issueName d/", 21);
        assertParseSuccess("edit 1 i/ d/description", 9);
    }

    // ========================= IssueNameSuggestion =================================

    /**
     * Invalid Inputs for {@code IssueNameSuggestion}
     */
    @Test
    public void parseUserInput_nonValidInputIssueNameSuggestion() {
        assertParseFailure("find ", 5);
        // Valid user input but wrong caret position
        assertParseFailure("find s", 2);
    }

    /**
     * Invalid Inputs for {@code IssueNameSuggestion}
     */
    @Test
    public void parseUserInput_validInputIssueNameSuggestion() {
        assertParseSuccess("find s", 6);
        assertParseSuccess("find issueName", 8);
        assertParseSuccess("find issueName and some more", 24);
    }

    // Helpers

    /**
     * Takes the {@code userInput} and {@code caretPosition} and
     * asserts that the given inputs will return a {@code Suggestion} object
     */
    private void assertParseSuccess(String userInput, int caretPosition) {
        suggestionLogic.updateCaretPosition(caretPosition);
        Suggestion suggestion = suggestionLogic.parseUserInput(userInput);

        // parseUserInput should return a Suggestion object if successful
        assertNotEquals(null, suggestion);
    }

    /**
     * Takes the {@code userInput} and {@code caretPosition} and
     * asserts that the given inputs will not return a {@code Suggestion} object
     * and should return null instead
     */
    private void assertParseFailure(String userInput, int caretPosition) {
        suggestionLogic.updateCaretPosition(caretPosition);
        Suggestion suggestion = suggestionLogic.parseUserInput(userInput);

        // parseUserInput should return null if unsuccessful
        assertEquals(null, suggestion);
    }
}
