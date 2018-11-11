package seedu.saveit.logic.suggestions;

import static org.junit.Assert.assertEquals;
import static seedu.saveit.logic.commands.CommandTestUtil.C_SEGMENTATION_FAULT_DESCRIPTION;
import static seedu.saveit.logic.commands.CommandTestUtil.C_SEGMENTATION_FAULT_STATEMENT;
import static seedu.saveit.logic.commands.CommandTestUtil.JAVA_NULL_POINTER_DESCRIPTION;
import static seedu.saveit.logic.commands.CommandTestUtil.JAVA_NULL_POINTER_STATEMENT;
import static seedu.saveit.logic.parser.ArgumentTokenizer.END_MARKER;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_DESCRIPTION_STRING;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_REMARK_STRING;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_SOLUTION_LINK_STRING;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_STATEMENT_STRING;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_TAG_STRING;
import static seedu.saveit.logic.suggestion.CopyExistingSuggestion.COPY_EXISTING_PROMPT;
import static seedu.saveit.logic.suggestion.CopyExistingSuggestion.COPY_EXISTING_SUCCESS;
import static seedu.saveit.testutil.TypicalIssues.getTypicalSaveIt;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Test;

import seedu.saveit.commons.core.index.Index;
import seedu.saveit.logic.parser.Prefix;
import seedu.saveit.logic.suggestion.CopyExistingSuggestion;
import seedu.saveit.logic.suggestion.SuggestionResult;
import seedu.saveit.logic.suggestion.SuggestionValue;
import seedu.saveit.model.Model;
import seedu.saveit.model.ModelManager;
import seedu.saveit.model.UserPrefs;

public class CopyExistingSuggestionTest {

    private Model model = new ModelManager(getTypicalSaveIt(), new UserPrefs());

    @Test
    public void getValueFromIdentifier_invalidInputs() {
        // These values do not matter and are only used to initialize the Suggestion
        CopyExistingSuggestion suggestion = new CopyExistingSuggestion(
                model, Index.fromOneBased(1),
                new Prefix(PREFIX_TAG_STRING), new Prefix(PREFIX_DESCRIPTION_STRING));

        // Wrong identifiers
        String value = suggestion.getValueFromIdentifier(Index.fromOneBased(1), PREFIX_TAG_STRING);
        assertEquals("", value);

        value = suggestion.getValueFromIdentifier(Index.fromOneBased(1), PREFIX_SOLUTION_LINK_STRING);
        assertEquals("", value);

        value = suggestion.getValueFromIdentifier(Index.fromOneBased(1), PREFIX_REMARK_STRING);
        assertEquals("", value);

        // Correct identifiers but Index out of bounds of TypicalSaveIt
        value = suggestion.getValueFromIdentifier(Index.fromOneBased(10), PREFIX_DESCRIPTION_STRING);
        assertEquals("", value);

        value = suggestion.getValueFromIdentifier(Index.fromOneBased(10), PREFIX_STATEMENT_STRING);
        assertEquals("", value);
    }

    @Test
    public void getValueFromIdentifier_validInputs() {
        // These values do not matter and are only used to initialize the Suggestion
        CopyExistingSuggestion suggestion = new CopyExistingSuggestion(
                model, Index.fromOneBased(1),
                new Prefix(PREFIX_TAG_STRING), new Prefix(PREFIX_DESCRIPTION_STRING));

        // Wrong identifiers
        String value = suggestion.getValueFromIdentifier(Index.fromOneBased(1), PREFIX_DESCRIPTION_STRING);
        assertEquals(JAVA_NULL_POINTER_DESCRIPTION, value);

        value = suggestion.getValueFromIdentifier(Index.fromOneBased(1), PREFIX_STATEMENT_STRING);
        assertEquals(JAVA_NULL_POINTER_STATEMENT, value);

        value = suggestion.getValueFromIdentifier(Index.fromOneBased(2), PREFIX_DESCRIPTION_STRING);
        assertEquals(C_SEGMENTATION_FAULT_DESCRIPTION, value);

        value = suggestion.getValueFromIdentifier(Index.fromOneBased(2), PREFIX_STATEMENT_STRING);
        assertEquals(C_SEGMENTATION_FAULT_STATEMENT, value);
    }

    @Test
    public void evaluate() {
        Prefix startPrefix = new Prefix(PREFIX_DESCRIPTION_STRING, 5);
        Prefix startPrefix2 = new Prefix(PREFIX_STATEMENT_STRING, 5);
        Prefix endPrefix = new Prefix(PREFIX_TAG_STRING, 10);
        Prefix endMarker = new Prefix(END_MARKER, 10);

        // Valid Inputs with startPrefix in front of another prefix - /d .../t
        CopyExistingSuggestion suggestion = new CopyExistingSuggestion(
                model, Index.fromOneBased(1), startPrefix, endPrefix);

        SuggestionResult result = suggestion.evaluate();
        SuggestionResult expectedResult = new SuggestionResult(
                new LinkedList<>(Arrays.asList(
                        new SuggestionValue(COPY_EXISTING_PROMPT, JAVA_NULL_POINTER_DESCRIPTION))),
                COPY_EXISTING_SUCCESS,
                "",
                startPrefix.getPosition() + startPrefix.getPrefix().length(),
                endPrefix.getPosition() - 1);
        assertEquals(expectedResult, result);

        // Valid Inputs with startPrefix changed to Statement identifier
        suggestion = new CopyExistingSuggestion(
                model, Index.fromOneBased(1), startPrefix2, endPrefix);
        result = suggestion.evaluate();
        expectedResult = new SuggestionResult(
                new LinkedList<>(Arrays.asList(
                        new SuggestionValue(COPY_EXISTING_PROMPT, JAVA_NULL_POINTER_STATEMENT))),
                COPY_EXISTING_SUCCESS,
                "",
                startPrefix.getPosition() + startPrefix.getPrefix().length(),
                endPrefix.getPosition() - 1);
        assertEquals(expectedResult, result);

        // Valid Inputs except now the startPrefix is the last prefix
        suggestion = new CopyExistingSuggestion(
                model, Index.fromOneBased(1), startPrefix2, endMarker);
        result = suggestion.evaluate();
        expectedResult = new SuggestionResult(
                new LinkedList<>(Arrays.asList(
                        new SuggestionValue(COPY_EXISTING_PROMPT, JAVA_NULL_POINTER_STATEMENT))),
                COPY_EXISTING_SUCCESS,
                "",
                startPrefix.getPosition() + startPrefix.getPrefix().length(),
                endPrefix.getPosition());
        assertEquals(expectedResult, result);

        // Invalid Inputs - index out of bounds of list
        suggestion = new CopyExistingSuggestion(
                model, Index.fromOneBased(100), startPrefix2, endMarker);
        result = suggestion.evaluate();
        expectedResult = new SuggestionResult(
                new LinkedList<>(Arrays.asList(
                        new SuggestionValue(COPY_EXISTING_PROMPT, ""))),
                COPY_EXISTING_SUCCESS,
                "",
                startPrefix.getPosition() + startPrefix.getPrefix().length(),
                endPrefix.getPosition());
        assertEquals(expectedResult, result);
    }
}
