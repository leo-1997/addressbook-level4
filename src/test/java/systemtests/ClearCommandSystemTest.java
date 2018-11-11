package systemtests;

import static seedu.saveit.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.saveit.testutil.TypicalIssues.KEYWORD_MATCHING_MYSQL;

import org.junit.Ignore;
import org.junit.Test;

import seedu.saveit.commons.core.index.Index;
import seedu.saveit.logic.commands.ClearCommand;
import seedu.saveit.logic.commands.RedoCommand;
import seedu.saveit.logic.commands.UndoCommand;
import seedu.saveit.model.Model;
import seedu.saveit.model.ModelManager;

public class ClearCommandSystemTest extends SaveItSystemTest {

    @Test
    @Ignore
    public void clear() {
        final Model defaultModel = getModel();

        /* Case: clear non-empty saveit book, command with leading spaces and trailing alphanumeric characters and
         * spaces -> cleared
         */
        executeCommand("   " + ClearCommand.COMMAND_WORD + " ab12   ");
        executeCommand("Yes");
        assertCommandSuccess("   " + ClearCommand.COMMAND_WORD + " ab12   ");
        assertSelectedCardUnchanged();

        /* Case: undo clearing saveit book -> original saveit book restored */
        String command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, expectedResultMessage, defaultModel);
        assertSelectedCardUnchanged();

        /* Case: redo clearing saveit book -> cleared */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, expectedResultMessage, new ModelManager());
        assertSelectedCardUnchanged();

        /* Case: selects first card in issue list and clears saveit book -> cleared and no card selected */
        executeCommand(UndoCommand.COMMAND_WORD); // restores the original saveit book
        selectIssue(Index.fromOneBased(1));
        assertCommandSuccess(ClearCommand.COMMAND_WORD);
        assertSelectedCardDeselected();

        /* Case: filters the issue list before clearing -> entire saveit book cleared */
        executeCommand(UndoCommand.COMMAND_WORD); // restores the original saveit book
        showIssuesWithName(KEYWORD_MATCHING_MYSQL);
        assertCommandSuccess(ClearCommand.COMMAND_WORD);
        assertSelectedCardUnchanged();

        /* Case: clear empty saveit book -> cleared */
        assertCommandSuccess(ClearCommand.COMMAND_WORD);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("ClEaR", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code ClearCommand#MESSAGE_ISSUE_SUCCESS} and the model related components equal to an empty model.
     * These verifications are done by
     * {@code SaveItSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the command box has the default style class and the status bar's sync status changes.
     * @see SaveItSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command) {
        assertCommandSuccess(command, ClearCommand.MESSAGE_SUCCESS, new ModelManager());
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String)} except that the result box displays
     * {@code expectedResultMessage} and the model related components equal to {@code expectedModel}.
     * @see ClearCommandSystemTest#assertCommandSuccess(String)
     */
    private void assertCommandSuccess(String command, String expectedResultMessage, Model expectedModel) {
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
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
