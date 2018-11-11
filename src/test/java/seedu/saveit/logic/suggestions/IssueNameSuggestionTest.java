package seedu.saveit.logic.suggestions;

import static org.junit.Assert.assertEquals;
import static seedu.saveit.logic.commands.CommandTestUtil.JAVA_NULL_POINTER_STATEMENT;
import static seedu.saveit.logic.parser.ArgumentTokenizer.END_MARKER;
import static seedu.saveit.logic.parser.ArgumentTokenizer.START_MARKER;
import static seedu.saveit.testutil.TypicalIssues.getTypicalSaveIt;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Test;

import seedu.saveit.logic.parser.Prefix;
import seedu.saveit.logic.suggestion.IssueNameSuggestion;
import seedu.saveit.logic.suggestion.SuggestionResult;
import seedu.saveit.logic.suggestion.SuggestionValue;
import seedu.saveit.model.Model;
import seedu.saveit.model.ModelManager;
import seedu.saveit.model.UserPrefs;



public class IssueNameSuggestionTest {

    private Model model = new ModelManager(getTypicalSaveIt(), new UserPrefs());

    @Test
    public void evaluate() {
        Prefix startMarker = new Prefix(START_MARKER, 5);
        Prefix endMarker = new Prefix(END_MARKER, 10);
        String argument = JAVA_NULL_POINTER_STATEMENT.split("//s+")[0];

        IssueNameSuggestion suggestion = new IssueNameSuggestion(
                model, argument, startMarker, endMarker);

        SuggestionResult result = suggestion.evaluate();
        SuggestionResult expectedResult = new SuggestionResult(
                new LinkedList<>(Arrays.asList(
                        new SuggestionValue(JAVA_NULL_POINTER_STATEMENT, JAVA_NULL_POINTER_STATEMENT))),
                IssueNameSuggestion.STATEMENT_SUCCESS,
                argument,
                startMarker.getPosition() + startMarker.getPrefix().length() + IssueNameSuggestion.WHITE_SPACE_OFFSET,
                endMarker.getPosition());

        assertEquals(expectedResult, result);


        // Argument not found - no values
        argument = "asdhasdhoaisdhok";
        suggestion = new IssueNameSuggestion(model, argument, startMarker, endMarker);

        result = suggestion.evaluate();
        expectedResult = new SuggestionResult(
                new LinkedList<>(Arrays.asList()),
                IssueNameSuggestion.STATEMENT_SUCCESS,
                argument,
                startMarker.getPosition() + startMarker.getPrefix().length() + IssueNameSuggestion.WHITE_SPACE_OFFSET,
                endMarker.getPosition());

        assertEquals(expectedResult, result);
    }
}
