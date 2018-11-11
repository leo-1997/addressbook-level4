package systemtests;

import static org.junit.Assert.assertFalse;
import static seedu.saveit.commons.core.Messages.MESSAGE_ISSUES_LISTED_OVERVIEW;
import static seedu.saveit.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.saveit.testutil.TypicalIssues.C_SEGMENTATION_FAULT;
import static seedu.saveit.testutil.TypicalIssues.KEYWORD_MATCHING_MYSQL;
import static seedu.saveit.testutil.TypicalIssues.RUBY_HASH_BUG;
import static seedu.saveit.testutil.TypicalIssues.TRAVIS_BUILD;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import seedu.saveit.commons.core.index.Index;
import seedu.saveit.logic.commands.DeleteCommand;
import seedu.saveit.logic.commands.FindCommand;
import seedu.saveit.logic.commands.RedoCommand;
import seedu.saveit.logic.commands.UndoCommand;
import seedu.saveit.model.Model;
import seedu.saveit.model.issue.Tag;

public class FindCommandSystemTest extends SaveItSystemTest {

    @Test
    @Ignore
    public void find() {
        /* Case: find multiple issues in saveit book, command with leading spaces and trailing spaces
         * -> 2 issues found
         */
        String command = "   " + FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MYSQL + "   ";
        Model expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, C_SEGMENTATION_FAULT, TRAVIS_BUILD);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where issue list is displaying the issues we are finding
         * -> 2 issues found
         */
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MYSQL;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find issue where issue list is not displaying the issue we are finding -> 1 issue found */
        command = FindCommand.COMMAND_WORD + " Carl";
        ModelHelper.setFilteredList(expectedModel, RUBY_HASH_BUG);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple issues in saveit book, 2 keywords -> 2 issues found */
        command = FindCommand.COMMAND_WORD + " Benson Daniel";
        ModelHelper.setFilteredList(expectedModel, C_SEGMENTATION_FAULT, TRAVIS_BUILD);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple issues in saveit book, 2 keywords in reversed order -> 2 issues found */
        command = FindCommand.COMMAND_WORD + " Daniel Benson";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple issues in saveit book, 2 keywords with 1 repeat -> 2 issues found */
        command = FindCommand.COMMAND_WORD + " Daniel Benson Daniel";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple issues in saveit book, 2 matching keywords and 1 non-matching keyword
         * -> 2 issues found
         */
        command = FindCommand.COMMAND_WORD + " Daniel Benson NonMatchingKeyWord";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: undo previous find command -> rejected */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo previous find command -> rejected */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: find same issues in saveit book after deleting 1 of them -> 1 issue found */
        executeCommand(DeleteCommand.COMMAND_WORD + " 1");
        assertFalse(getModel().getSaveIt().getIssueList().contains(C_SEGMENTATION_FAULT));
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MYSQL;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, TRAVIS_BUILD);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find issue in saveit book, keyword is same as name but of different case -> 1 issue found */
        command = FindCommand.COMMAND_WORD + " MeIeR";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find issue in saveit book, keyword is substring of name -> 0 issues found */
        command = FindCommand.COMMAND_WORD + " Mei";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find issue in saveit book, name is substring of keyword -> 0 issues found */
        command = FindCommand.COMMAND_WORD + " Meiers";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find issue not in saveit book -> 0 issues found */
        command = FindCommand.COMMAND_WORD + " Mark";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find description number of issue in saveit book -> 0 issues found */
        command = FindCommand.COMMAND_WORD + " " + TRAVIS_BUILD.getDescription().getValue();
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find tags of issue in saveit book -> 0 issues found */
        List<Tag> tags = new ArrayList<>(TRAVIS_BUILD.getTags());
        command = FindCommand.COMMAND_WORD + " " + tags.get(0).tagName;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find while an issue is selected -> selected card deselected */
        showAllIssues();
        selectIssue(Index.fromOneBased(1));
        assertFalse(getIssueListPanel().getHandleToSelectedCard().getStatement()
                .equals(TRAVIS_BUILD.getStatement().getValue()));
        command = FindCommand.COMMAND_WORD + " Daniel";
        ModelHelper.setFilteredList(expectedModel, TRAVIS_BUILD);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardDeselected();

        /* Case: find issue in empty saveit book -> 0 issues found */
        deleteAllIssues();
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MYSQL;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, TRAVIS_BUILD);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        command = "FiNd Meier";
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the resultdisplay
     * box displays {@code Messages#MESSAGE_ISSUES_LISTED_OVERVIEW} with the number of people in thefiltered list,
     * and the model related components equal to {@code expectedModel}.
     * These verifications are done by
     * {@code SaveItSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the status bar remains unchanged, and the command box has the default styleclass, and the
     * selected card updated accordingly, depending on {@code cardStatus}.
     * @see SaveItSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel) {
        String expectedResultMessage = String.format(
            MESSAGE_ISSUES_LISTED_OVERVIEW, expectedModel.getFilteredAndSortedIssueList().size());

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code SaveItSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
     * @see SaveItSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
