package seedu.saveit.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.saveit.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.saveit.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_DESCRIPTION_C;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_STATEMENT_C;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_TAG_UI;
import static seedu.saveit.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.saveit.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.saveit.logic.commands.CommandTestUtil.showIssueAtIndex;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_FIRST_ISSUE;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_SECOND_ISSUE;
import static seedu.saveit.testutil.TypicalIssues.getTypicalSaveIt;

import org.junit.Ignore;
import org.junit.Test;

import seedu.saveit.commons.core.Messages;
import seedu.saveit.commons.core.index.Index;
import seedu.saveit.logic.CommandHistory;
import seedu.saveit.logic.commands.EditCommand.EditIssueDescriptor;
import seedu.saveit.model.Issue;
import seedu.saveit.model.Model;
import seedu.saveit.model.ModelManager;
import seedu.saveit.model.SaveIt;
import seedu.saveit.model.UserPrefs;
import seedu.saveit.testutil.EditIssueDescriptorBuilder;
import seedu.saveit.testutil.IssueBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalSaveIt(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Issue editedIssue = new IssueBuilder().build();
        EditIssueDescriptor descriptor = new EditIssueDescriptorBuilder(editedIssue).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_ISSUE, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_ISSUE_SUCCESS, editedIssue);

        Model expectedModel = new ModelManager(new SaveIt(model.getSaveIt()), new UserPrefs());
        expectedModel.updateIssue(model.getFilteredAndSortedIssueList().get(0), editedIssue);
        expectedModel.commitSaveIt();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    @Ignore
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastIssue = Index.fromOneBased(model.getFilteredAndSortedIssueList().size());
        Issue lastIssue = model.getFilteredAndSortedIssueList().get(indexLastIssue.getZeroBased());

        IssueBuilder issueInList = new IssueBuilder(lastIssue);
        Issue editedIssue = issueInList.withStatement(VALID_STATEMENT_C).withDescription(VALID_DESCRIPTION_C)
                .withTags(VALID_TAG_UI).build();

        EditIssueDescriptor descriptor = new EditIssueDescriptorBuilder().withStatement(VALID_STATEMENT_C)
                .withDescription(VALID_DESCRIPTION_C).withTags(VALID_TAG_UI).build();
        EditCommand editCommand = new EditCommand(indexLastIssue, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_ISSUE_SUCCESS, editedIssue);

        Model expectedModel = new ModelManager(new SaveIt(model.getSaveIt()), new UserPrefs());
        expectedModel.updateIssue(lastIssue, editedIssue);
        expectedModel.commitSaveIt();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    @Ignore
    public void execute_filteredList_success() {
        showIssueAtIndex(model, INDEX_FIRST_ISSUE);


        Issue issueInFilteredList = model.getFilteredAndSortedIssueList().get(INDEX_FIRST_ISSUE.getZeroBased());
        Issue editedIssue = new IssueBuilder(issueInFilteredList).withStatement(VALID_STATEMENT_C).build();

        EditCommand editCommand = new EditCommand(INDEX_FIRST_ISSUE,
                new EditIssueDescriptorBuilder().withStatement(VALID_STATEMENT_C).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_ISSUE_SUCCESS, editedIssue);

        Model expectedModel = new ModelManager(new SaveIt(model.getSaveIt()), new UserPrefs());
        expectedModel.updateIssue(model.getFilteredAndSortedIssueList().get(0), editedIssue);
        expectedModel.commitSaveIt();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateIssueUnfilteredList_failure() {
        Issue firstIssue = model.getFilteredAndSortedIssueList().get(INDEX_FIRST_ISSUE.getZeroBased());
        EditIssueDescriptor descriptor = new EditIssueDescriptorBuilder(firstIssue).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_ISSUE, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_ISSUE);
    }

    @Test
    public void execute_duplicateIssueFilteredList_failure() {
        showIssueAtIndex(model, INDEX_FIRST_ISSUE);

        // edit issue in filtered list into a duplicate in saveit book
        Issue issueInList = model.getSaveIt().getIssueList().get(INDEX_SECOND_ISSUE.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_ISSUE,
                new EditIssueDescriptorBuilder(issueInList).build());

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_ISSUE);
    }

    @Test
    public void execute_invalidIssueIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredAndSortedIssueList().size() + 1);
        EditCommand.EditIssueDescriptor descriptor = new EditIssueDescriptorBuilder()
                .withStatement(VALID_STATEMENT_C).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of saveit book
     */
    @Test
    public void execute_invalidIssueIndexFilteredList_failure() {
        showIssueAtIndex(model, INDEX_FIRST_ISSUE);
        Index outOfBoundIndex = INDEX_SECOND_ISSUE;
        // ensures that outOfBoundIndex is still in bounds of saveit book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getSaveIt().getIssueList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditIssueDescriptorBuilder().withStatement(VALID_STATEMENT_C).build());

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Issue editedIssue = new IssueBuilder().build();
        Issue issueToEdit = model.getFilteredAndSortedIssueList().get(INDEX_FIRST_ISSUE.getZeroBased());
        EditCommand.EditIssueDescriptor descriptor = new EditIssueDescriptorBuilder(editedIssue).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_ISSUE, descriptor);
        Model expectedModel = new ModelManager(new SaveIt(model.getSaveIt()), new UserPrefs());
        expectedModel.updateIssue(issueToEdit, editedIssue);
        expectedModel.commitSaveIt();

        // edit -> first issue edited
        editCommand.execute(model, commandHistory);

        // undo -> reverts saveit back to previous state and filtered issue list to show all issues
        expectedModel.undoSaveIt();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first issue edited again
        expectedModel.redoSaveIt();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredAndSortedIssueList().size() + 1);
        EditIssueDescriptor descriptor = new EditIssueDescriptorBuilder().withStatement(VALID_STATEMENT_C).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        // execution failed -> saveit book state not added into model
        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_DISPLAYED_INDEX);

        // single saveit book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Edits a {@code Issue} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited issue in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the issue object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameIssueEdited() throws Exception {
        Issue editedIssue = new IssueBuilder().build();
        EditIssueDescriptor descriptor = new EditIssueDescriptorBuilder(editedIssue).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_ISSUE, descriptor);
        Model expectedModel = new ModelManager(new SaveIt(model.getSaveIt()), new UserPrefs());

        showIssueAtIndex(model, INDEX_SECOND_ISSUE);
        Issue issueToEdit = model.getFilteredAndSortedIssueList().get(INDEX_FIRST_ISSUE.getZeroBased());
        expectedModel.updateIssue(issueToEdit, editedIssue);
        expectedModel.commitSaveIt();

        // edit -> edits second issue in unfiltered issue list / first issue in filtered issue list
        editCommand.execute(model, commandHistory);

        // undo -> reverts saveit back to previous state and filtered issue list to show all issues
        expectedModel.undoSaveIt();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(model.getFilteredAndSortedIssueList().get(INDEX_FIRST_ISSUE.getZeroBased()), issueToEdit);
        // redo -> edits same second issue in unfiltered issue list
        expectedModel.redoSaveIt();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_ISSUE, DESC_AMY);

        // same values -> returns true
        EditIssueDescriptor copyDescriptor = new EditIssueDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_ISSUE, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_ISSUE, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_ISSUE, DESC_BOB)));
    }

}
