package systemtests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.saveit.logic.commands.CommandTestUtil.DESCRIPTION_DESC_C;
import static seedu.saveit.logic.commands.CommandTestUtil.DESCRIPTION_DESC_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.saveit.logic.commands.CommandTestUtil.INVALID_STATEMENT_DESC;
import static seedu.saveit.logic.commands.CommandTestUtil.SOLUTION_DESC_C;
import static seedu.saveit.logic.commands.CommandTestUtil.SOLUTION_DESC_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.STATEMENT_DESC_C;
import static seedu.saveit.logic.commands.CommandTestUtil.STATEMENT_DESC_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.TAG_DESC_UI;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_DESCRIPTION_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_STATEMENT_C;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_STATEMENT_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_TAG_UI;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.saveit.model.Model.PREDICATE_SHOW_ALL_ISSUES;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_FIRST_ISSUE;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_SECOND_ISSUE;
import static seedu.saveit.testutil.TypicalIssues.KEYWORD_MATCHING_MYSQL;
import static seedu.saveit.testutil.TypicalIssues.VALID_C_ISSUE;
import static seedu.saveit.testutil.TypicalIssues.VALID_JAVA_ISSUE;

import org.junit.Ignore;
import org.junit.Test;

import seedu.saveit.commons.core.Messages;
import seedu.saveit.commons.core.index.Index;
import seedu.saveit.logic.commands.CommandTestUtil;
import seedu.saveit.logic.commands.EditCommand;
import seedu.saveit.logic.commands.RedoCommand;
import seedu.saveit.logic.commands.UndoCommand;
import seedu.saveit.model.Issue;
import seedu.saveit.model.Model;
import seedu.saveit.model.issue.Description;
import seedu.saveit.model.issue.IssueStatement;
import seedu.saveit.testutil.IssueBuilder;
import seedu.saveit.testutil.IssueUtil;

public class EditCommandSystemTest extends SaveItSystemTest {

    @Test
    @Ignore
    public void edit() {
        Model model = getModel();

        /* ----------------- Performing edit operation while an unfiltered list is being shown
        ---------------------- */

        /* Case: edit all fields, command with leading spaces, trailing spaces and multiple spaces between
        each field
         * -> edited
         */
        Index index = INDEX_FIRST_ISSUE;
        String command = " " + EditCommand.COMMAND_WORD + "  " + index.getOneBased() + "  "
                + STATEMENT_DESC_C + " " + DESCRIPTION_DESC_C + " "
                + SOLUTION_DESC_C + " " + TAG_DESC_UI + " ";
        Issue editedIssue = new IssueBuilder(VALID_C_ISSUE).withTags(VALID_TAG_UI).build();
        assertCommandSuccess(command, index, editedIssue);

        /* Case: undo editing the last issue in the list -> last issue restored */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo editing the last issue in the list -> last issue edited again */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        model.updateIssue(
            getModel().getFilteredAndSortedIssueList().get(INDEX_FIRST_ISSUE.getZeroBased()), editedIssue);
        assertCommandSuccess(command, model, expectedResultMessage);


        /* Case: edit a issue with new values same as existing values -> edited */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + STATEMENT_DESC_C + DESCRIPTION_DESC_C
                + SOLUTION_DESC_C + CommandTestUtil.TAG_DESC_UI + TAG_DESC_UI;
        assertCommandSuccess(command, index, VALID_C_ISSUE);

        /* Case: edit an issue with new values same as another issue's values but with different name ->
        edited */
        assertTrue(getModel().getSaveIt().getIssueList().contains(VALID_C_ISSUE));
        index = INDEX_SECOND_ISSUE;

        assertNotEquals(getModel().getFilteredAndSortedIssueList().get(index.getZeroBased()), VALID_C_ISSUE);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + STATEMENT_DESC_JAVA + DESCRIPTION_DESC_C
            + SOLUTION_DESC_C + CommandTestUtil.TAG_DESC_UI + TAG_DESC_UI;
        editedIssue = new IssueBuilder(VALID_C_ISSUE).withStatement(VALID_STATEMENT_JAVA).build();
        assertCommandSuccess(command, index, editedIssue);

        /* Case: edit an issue with new values same as another issue's values but with different description
         * -> edited
         */
        index = INDEX_SECOND_ISSUE;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + STATEMENT_DESC_C + DESCRIPTION_DESC_JAVA
                + SOLUTION_DESC_C + CommandTestUtil.TAG_DESC_UI + TAG_DESC_UI;
        editedIssue = new IssueBuilder(VALID_C_ISSUE).withDescription(VALID_DESCRIPTION_JAVA).build();
        assertCommandSuccess(command, index, editedIssue);

        /* Case: clear tags -> cleared */
        index = INDEX_FIRST_ISSUE;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + PREFIX_TAG.getPrefix();
        Issue issueToEdit = getModel().getFilteredAndSortedIssueList().get(index.getZeroBased());
        editedIssue = new IssueBuilder(issueToEdit).withTags().build();
        assertCommandSuccess(command, index, editedIssue);

        /* ------------------ Performing edit operation while a filtered list is being shown
        ------------------------ */

        /* Case: filtered issue list, edit index within bounds of saveit book and issue list -> edited */
        showIssuesWithName(KEYWORD_MATCHING_MYSQL);
        index = INDEX_FIRST_ISSUE;

        assertTrue(index.getZeroBased() < getModel().getFilteredAndSortedIssueList().size());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + STATEMENT_DESC_C;
        issueToEdit = getModel().getFilteredAndSortedIssueList().get(index.getZeroBased());
        editedIssue = new IssueBuilder(issueToEdit).withStatement(VALID_STATEMENT_C).build();
        assertCommandSuccess(command, index, editedIssue);

        /* Case: filtered issue list, edit index within bounds of saveit book but out of bounds of issue list
         * -> rejected
         */
        showIssuesWithName(KEYWORD_MATCHING_MYSQL);
        int invalidIndex = getModel().getSaveIt().getIssueList().size();

        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + STATEMENT_DESC_C,
            Messages.MESSAGE_INVALID_ISSUE_DISPLAYED_INDEX);

        /* --------------------- Performing edit operation while an issue card is selected
        -------------------------- */

        /* Case: selects first card in the issue list, edit an issue -> edited, card selection remains
        unchanged but
         * browser url changes
         */
        showAllIssues();
        index = INDEX_FIRST_ISSUE;
        selectIssue(index);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + STATEMENT_DESC_JAVA + DESCRIPTION_DESC_JAVA
            + SOLUTION_DESC_JAVA + CommandTestUtil.TAG_DESC_UI;
        // this can be misleading: card selection actually remains unchanged but the
        // browser's url is updated to reflect the new issue's name
        assertCommandSuccess(command, index, VALID_JAVA_ISSUE, index);

        /* --------------------------------- Performing invalid edit operation
        -------------------------------------- */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " 0" + STATEMENT_DESC_C,
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " -1" + STATEMENT_DESC_C,
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredAndSortedIssueList().size() + 1;

        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + STATEMENT_DESC_C,
            Messages.MESSAGE_INVALID_ISSUE_DISPLAYED_INDEX);

        /* Case: missing index -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + STATEMENT_DESC_C,
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: missing all fields -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_ISSUE.getOneBased(),
            EditCommand.MESSAGE_NOT_EDITED);

        /* Case: invalid name -> rejected */
        assertCommandFailure(
            EditCommand.COMMAND_WORD + " " + INDEX_FIRST_ISSUE.getOneBased() + INVALID_STATEMENT_DESC,
            IssueStatement.MESSAGE_ISSUE_STATEMENT_CONSTRAINTS);

        /* Case: invalid description -> rejected */
        assertCommandFailure(
            EditCommand.COMMAND_WORD + " " + INDEX_FIRST_ISSUE.getOneBased() + INVALID_DESCRIPTION_DESC,
            Description.MESSAGE_DESCRIPTION_CONSTRAINTS);

        /* Case: edit a issue with new values same as another issue's values -> rejected */
        executeCommand(IssueUtil.getAddCommand(VALID_C_ISSUE));
        assertTrue(getModel().getSaveIt().getIssueList().contains(VALID_C_ISSUE));
        index = INDEX_FIRST_ISSUE;
        assertFalse(getModel().getFilteredAndSortedIssueList().get(index.getZeroBased()).equals(VALID_C_ISSUE));
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + STATEMENT_DESC_C + DESCRIPTION_DESC_C
            + SOLUTION_DESC_C + CommandTestUtil.TAG_DESC_UI + TAG_DESC_UI;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_ISSUE);

        /* Case: edit a issue with new values same as another issue's values but with different tags -> rejected */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + STATEMENT_DESC_C + DESCRIPTION_DESC_C
            + SOLUTION_DESC_C + TAG_DESC_UI;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_ISSUE);

        /* Case: edit a issue with new values same as another issue's values but with different saveit -> rejected */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + STATEMENT_DESC_C + DESCRIPTION_DESC_C
            + SOLUTION_DESC_JAVA + CommandTestUtil.TAG_DESC_UI + TAG_DESC_UI;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_ISSUE);

        /* Case: edit an issue with new values same as another issue's values but with different description ->
         rejected */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + STATEMENT_DESC_C + DESCRIPTION_DESC_JAVA
            + SOLUTION_DESC_C + CommandTestUtil.TAG_DESC_UI + TAG_DESC_UI;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_ISSUE);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Index, Issue, Index)} except that the
     * browser url and selected card remain unchanged.
     *
     * @param toEdit the index of the current model's filtered list
     * @see EditCommandSystemTest#assertCommandSuccess(String, Index, Issue, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Issue editedIssue) {
        assertCommandSuccess(command, toEdit, editedIssue, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} and in addition,<br>
     * 1. Asserts that result display box displays the success message of executing {@code EditCommand}.<br> 2. Asserts
     * that the model related components are updated to reflect the issue at index {@code toEdit} being updated to
     * values specified {@code editedIssue}.<br>
     *
     * @param toEdit the index of the current model's filtered list.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Issue editedIssue,
        Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        expectedModel.updateIssue(expectedModel.getFilteredAndSortedIssueList().get(toEdit.getZeroBased()),
            editedIssue);
        expectedModel.updateFilteredIssueList(PREDICATE_SHOW_ALL_ISSUES);

        assertCommandSuccess(command, expectedModel,
            String.format(EditCommand.MESSAGE_EDIT_ISSUE_SUCCESS, editedIssue),
            expectedSelectedCardIndex);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} except that the
     * browser url and selected card remain unchanged.
     *
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br> 1. Asserts that the command box displays an empty string.<br> 2.
     * Asserts that the result display box displays {@code expectedResultMessage}.<br> 3. Asserts that the browser url
     * and selected card update accordingly depending on the card at {@code expectedSelectedCardIndex}.<br> 4. Asserts
     * that the status bar's sync status changes.<br> 5. Asserts that the command box has the default style class.<br>
     * Verifications 1 and 2 are performed by {@code SaveItSystemTest#assertApplicationDisplaysExpected(String, String,
     * Model)}.<br>
     *
     * @see SaveItSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see SaveItSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
        Index expectedSelectedCardIndex) {
        executeCommand(command);
        expectedModel.updateFilteredIssueList(PREDICATE_SHOW_ALL_ISSUES);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br> 1. Asserts that the command box displays {@code command}.<br> 2.
     * Asserts that result display box displays {@code expectedResultMessage}.<br> 3. Asserts that the browser url,
     * selected card and status bar remain unchanged.<br> 4. Asserts that the command box has the error style.<br>
     * Verifications 1 and 2 are performed by {@code SaveItSystemTest#assertApplicationDisplaysExpected(String, String,
     * Model)}.<br>
     *
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
