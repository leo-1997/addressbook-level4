package seedu.saveit.logic.suggestions;

import static org.junit.Assert.assertEquals;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_TAG_SYNTAX;
import static seedu.saveit.logic.parser.ArgumentTokenizer.END_MARKER;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_TAG_STRING;
import static seedu.saveit.logic.suggestion.TagNameSuggestion.TAG_SUCCESS;
import static seedu.saveit.testutil.TypicalIssues.getTypicalSaveIt;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Test;

import seedu.saveit.logic.parser.Prefix;
import seedu.saveit.logic.suggestion.SuggestionResult;
import seedu.saveit.logic.suggestion.SuggestionValue;
import seedu.saveit.logic.suggestion.TagNameSuggestion;
import seedu.saveit.model.Model;
import seedu.saveit.model.ModelManager;
import seedu.saveit.model.UserPrefs;


public class TagNameSuggestionTest {

    private Model model = new ModelManager(getTypicalSaveIt(), new UserPrefs());

    @Test
    public void evaluate() {
        Prefix startPrefix = new Prefix(PREFIX_TAG_STRING, 5);
        Prefix endPrefix = new Prefix(PREFIX_TAG_STRING, 10);
        Prefix endMarker = new Prefix(END_MARKER, 10);
        String argument = VALID_TAG_SYNTAX.substring(0, 2);

        // Valid argument, with /t identifier in front of another identifier
        TagNameSuggestion suggestion = new TagNameSuggestion(model, argument, startPrefix, endPrefix);

        SuggestionResult result = suggestion.evaluate();
        SuggestionResult expectedResult = new SuggestionResult(
                new LinkedList<>(Arrays.asList(
                        new SuggestionValue(VALID_TAG_SYNTAX, VALID_TAG_SYNTAX))),
                TAG_SUCCESS,
                argument,
                startPrefix.getPosition() + startPrefix.getPrefix().length(),
                endPrefix.getPosition() - 1);

        assertEquals(expectedResult, result);

        // Valid argument, with /t identifier being at the end of the input
        suggestion = new TagNameSuggestion(model, argument, startPrefix, endMarker);
        result = suggestion.evaluate();
        expectedResult = new SuggestionResult(
                new LinkedList<>(Arrays.asList(
                        new SuggestionValue(VALID_TAG_SYNTAX, VALID_TAG_SYNTAX))),
                TAG_SUCCESS,
                argument,
                startPrefix.getPosition() + startPrefix.getPrefix().length(),
                endPrefix.getPosition());

        assertEquals(expectedResult, result);

        // Argument not found, no results
        argument = "asdasdasda";
        suggestion = new TagNameSuggestion(model, argument, startPrefix, endMarker);
        result = suggestion.evaluate();
        expectedResult = new SuggestionResult(
                new LinkedList<>(Arrays.asList()),
                TAG_SUCCESS,
                argument,
                startPrefix.getPosition() + startPrefix.getPrefix().length(),
                endPrefix.getPosition());

        assertEquals(expectedResult, result);
    }
}
