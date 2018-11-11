package seedu.saveit.logic.commands;

import static seedu.saveit.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.saveit.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_FIRST_SOLUTION;
import static seedu.saveit.testutil.TypicalIssues.getTypicalSaveIt;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import seedu.saveit.commons.core.Messages;
import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.commons.core.index.Index;
import seedu.saveit.logic.CommandHistory;
import seedu.saveit.model.Issue;
import seedu.saveit.model.Model;
import seedu.saveit.model.ModelManager;
import seedu.saveit.model.SaveIt;
import seedu.saveit.model.UserPrefs;
import seedu.saveit.model.issue.Solution;
import seedu.saveit.testutil.DirectoryBuilder;

public class SetPrimaryCommandTest {
    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory;
    private Issue issueSelected;
    private List<Solution> solutionList;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalSaveIt(), new UserPrefs());
        expectedModel = new ModelManager(new SaveIt(model.getSaveIt()), new UserPrefs());
        commandHistory = new CommandHistory();
    }

    @Test
    public void execute_validIndexRootLevel_failure() {
        setUpRootLevel();
        assertExecutionFailure(INDEX_FIRST_SOLUTION, Messages.MESSAGE_WRONG_DIRECTORY);
    }

    @Test
    public void execute_validIndexIssueLevel_success() {
        setUpIssueLevel();
        int lastIssueIndex = solutionList.size();
        assertExecutionSuccess(INDEX_FIRST_SOLUTION);
        assertExecutionSuccess(Index.fromOneBased(lastIssueIndex));
    }

    @Test
    public void execute_invalidIndexIssueLevel_failure() {
        setUpIssueLevel();
        int lastSolutionIndex = solutionList.size();
        assertExecutionFailure(Index.fromZeroBased(lastSolutionIndex),
                Messages.MESSAGE_INVALID_SOLUTION_DISPLAYED_INDEX);
    }

    /**
     * Reset the directory of model and expected to Issue Level.
     */
    private void setUpIssueLevel() {
        Index selectedIssueOneBasedIndex = Index.fromOneBased(4);
        Directory newDirectory = new DirectoryBuilder().withIssueIndex(selectedIssueOneBasedIndex).build();
        model.resetDirectory(newDirectory);
        expectedModel.resetDirectory(newDirectory);
        issueSelected = expectedModel.getFilteredAndSortedIssueList().get(selectedIssueOneBasedIndex.getZeroBased());
        solutionList = expectedModel.getFilteredAndSortedSolutionList();
    }

    /**
     * Reset the directory of model and expected to Root Level.
     */
    private void setUpRootLevel() {
        Directory rootDirectory = new DirectoryBuilder().build();
        model.resetDirectory(rootDirectory);
        expectedModel.resetDirectory(rootDirectory);
        issueSelected = null;
        solutionList = null;
    }

    /**
     * Executes a {@code SetPrimaryCommand} with the given {@code index}.
     */
    private void assertExecutionSuccess(Index index) {
        SetPrimaryCommand setPrimaryCommand = new SetPrimaryCommand(index);
        Solution staredSolution = solutionList.get(index.getZeroBased());
        expectedModel.updateIssue(issueSelected,
                issueSelected.setPrimarySolution(index.getZeroBased()));
        expectedModel.commitSaveIt();
        String expectedMessage = String.format(SetPrimaryCommand.MESSAGE_SUCCESS, staredSolution);

        assertCommandSuccess(setPrimaryCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    /**
     * Executes a {@code SetPrimaryCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        SetPrimaryCommand setPrimaryCommand = new SetPrimaryCommand(index);
        assertCommandFailure(setPrimaryCommand, model, commandHistory, expectedMessage);
    }
}
