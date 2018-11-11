package seedu.saveit.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.saveit.commons.core.Messages;
import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.commons.core.index.Index;
import seedu.saveit.logic.CommandHistory;
import seedu.saveit.logic.commands.exceptions.CommandException;
import seedu.saveit.model.Issue;
import seedu.saveit.model.Model;
import seedu.saveit.model.issue.Solution;

/**
 * Star one existing solution for an issue.
 */
public class SetPrimaryCommand extends Command {
    public static final String COMMAND_WORD = "setprimary";
    public static final String COMMAND_ALIAS = "sp";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Highlight one solution in the displayed solution list\n"
            + "Parameters: INDEX (must be the index shown in the list)\n"
            + "Example: " + COMMAND_WORD + " 1";
    public static final String MESSAGE_SUCCESS = "Set primary solution: %1$s";

    private final Index index;

    public SetPrimaryCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        Directory currentDirectory = model.getCurrentDirectory();
        List<Issue> lastShownIssueList = model.getFilteredAndSortedIssueList();

        if (!currentDirectory.isIssueLevel()) {
            throw new CommandException(Messages.MESSAGE_WRONG_DIRECTORY);
        }

        Issue issueSelected = lastShownIssueList.get(currentDirectory.getIssue() - 1);
        List<Solution> solutionList = model.getFilteredAndSortedSolutionList();
        int zeroBasedIndex = index.getZeroBased();

        if (zeroBasedIndex < 0 || zeroBasedIndex >= solutionList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SOLUTION_DISPLAYED_INDEX);
        }

        Solution primarySolution = solutionList.get(zeroBasedIndex);
        Issue updatedIssue = issueSelected.setPrimarySolution(zeroBasedIndex);

        model.updateIssue(issueSelected, updatedIssue);
        model.commitSaveIt();
        return new CommandResult(
                String.format(MESSAGE_SUCCESS, primarySolution));
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SetPrimaryCommand // instanceof handles nulls
                && index.equals(((SetPrimaryCommand) other).index)); // state check
    }
}
