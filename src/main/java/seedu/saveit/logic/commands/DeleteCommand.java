package seedu.saveit.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.saveit.commons.core.Messages;
import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.commons.core.index.Index;
import seedu.saveit.logic.CommandHistory;
import seedu.saveit.logic.commands.exceptions.CommandException;
import seedu.saveit.logic.parser.ParserUtil;
import seedu.saveit.model.Issue;
import seedu.saveit.model.Model;
import seedu.saveit.model.issue.Solution;

/**
 * Deletes an issue identified using it's displayed index from the saveIt.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";
    public static final String COMMAND_ALIAS = "d";


    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the issue identified by the index number used in the displayed issue list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_ISSUE_SUCCESS = "Deleted Issue: %1$s";
    public static final String MESSAGE_DELETE_SOLUTION_SUCCESS = "Deleted Solution: %1$s";

    private final Index targetIndex;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        Directory currentDirectory = model.getCurrentDirectory();
        if (currentDirectory.isRootLevel()) {
            return handleDeleteIssue(model);
        } else {
            return handleDeleteSolution(model);
        }
    }

    private CommandResult handleDeleteIssue(Model model) throws CommandException{
        List<Issue> lastShownList = model.getFilteredAndSortedIssueList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ISSUE_DISPLAYED_INDEX);
        }
        Issue issueToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteIssue(issueToDelete);
        model.commitSaveIt();
        return new CommandResult(String.format(MESSAGE_DELETE_ISSUE_SUCCESS, issueToDelete));
    }

    private CommandResult handleDeleteSolution(Model model) throws CommandException{
        int issueIndex = model.getCurrentDirectory().getIssue() - 1;
        List<Issue> lastShownList = model.getFilteredAndSortedIssueList();
        if (targetIndex.getZeroBased() >= lastShownList.get(issueIndex).getSolutions().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ISSUE_DISPLAYED_INDEX);
        }
        Solution solutionToDelete =  lastShownList.get(issueIndex).getSolutions().get(targetIndex.getZeroBased());
        model.deleteSolution(Index.fromZeroBased(issueIndex), solutionToDelete);
        model.commitSaveIt();
        return new CommandResult(String.format(MESSAGE_DELETE_SOLUTION_SUCCESS, solutionToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && targetIndex.equals(((DeleteCommand) other).targetIndex)); // state check
    }
}
