package seedu.saveit.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.saveit.commons.core.Messages.MESSAGE_ISSUES_LISTED_OVERVIEW;
import static seedu.saveit.logic.commands.CommandTestUtil.C_SEGMENTATION_FAULT_STATEMENT;
import static seedu.saveit.logic.commands.CommandTestUtil.JAVA_NULL_POINTER_STATEMENT;
import static seedu.saveit.logic.commands.CommandTestUtil.RUBY_HASH_BUG_STATEMENT;
import static seedu.saveit.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.saveit.testutil.TypicalIssues.C_SEGMENTATION_FAULT;
import static seedu.saveit.testutil.TypicalIssues.JAVA_NULL_POINTER;
import static seedu.saveit.testutil.TypicalIssues.RUBY_HASH_BUG;
import static seedu.saveit.testutil.TypicalIssues.getTypicalSaveIt;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import seedu.saveit.logic.CommandHistory;
import seedu.saveit.model.Model;
import seedu.saveit.model.ModelManager;
import seedu.saveit.model.UserPrefs;
import seedu.saveit.model.issue.IssueContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalSaveIt(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalSaveIt(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void equals() {
        IssueContainsKeywordsPredicate firstPredicate =
                new IssueContainsKeywordsPredicate(Collections.singletonList("first"));
        IssueContainsKeywordsPredicate secondPredicate =
                new IssueContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different issue -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noIssueFound() {
        String expectedMessage = String.format(MESSAGE_ISSUES_LISTED_OVERVIEW, 0);
        IssueContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredIssueList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredAndSortedIssueList());
    }

    @Test
    public void execute_multipleKeywords_multipleIssuesFound() {
        String expectedMessage = String.format(MESSAGE_ISSUES_LISTED_OVERVIEW, 3);
        IssueContainsKeywordsPredicate predicate = preparePredicate(
                JAVA_NULL_POINTER_STATEMENT.split("\\s+")[0] + " "
                + C_SEGMENTATION_FAULT_STATEMENT.split("\\s+")[0] + " "
                + RUBY_HASH_BUG_STATEMENT.split("\\s+")[0]);
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredIssueList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(JAVA_NULL_POINTER, C_SEGMENTATION_FAULT, RUBY_HASH_BUG),
                model.getFilteredAndSortedIssueList());
    }

    /**
     * Parses {@code userInput} into a {@code IssueContainsKeywordsPredicate}.
     */
    private IssueContainsKeywordsPredicate preparePredicate(String userInput) {
        return new IssueContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
