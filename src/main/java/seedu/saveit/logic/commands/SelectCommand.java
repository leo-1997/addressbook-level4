package seedu.saveit.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.saveit.commons.core.EventsCenter;
import seedu.saveit.commons.core.Messages;
import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.commons.core.index.Index;
import seedu.saveit.commons.events.model.DirectoryChangedEvent;
import seedu.saveit.commons.events.ui.JumpToListRequestEvent;
import seedu.saveit.commons.events.ui.JumpToSolutionListRequestEvent;
import seedu.saveit.logic.CommandHistory;
import seedu.saveit.logic.commands.exceptions.CommandException;
import seedu.saveit.model.Issue;
import seedu.saveit.model.Model;

/**
 * Selects an issue identified using it's displayed index from the saveIt.
 * Change the current directory to the selected issue.
 */
public class SelectCommand extends Command {

    public static final String COMMAND_WORD = "select";
    public static final String COMMAND_ALIAS = "s";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the issue identified by the index number used in the displayed issue list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_ISSUE_SUCCESS = "Selected Issue: %1$s";
    public static final String MESSAGE_SELECT_SOLUTION_SUCCESS = ", Solution: %1$s";

    private final Index targetIndex;

    public SelectCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        if (model.getCurrentDirectory().isRootLevel()) {
            return selectIssue(model, history);
        } else {
            return selectSolution(model, history);
        }

    }

    /**
     * execute command for selecting an {@code Issue}
     */
    private CommandResult selectIssue(Model model, CommandHistory history) throws CommandException {
        List<Issue> filteredIssueList = model.getFilteredAndSortedIssueList();
        if (targetIndex.getZeroBased() >= filteredIssueList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ISSUE_DISPLAYED_INDEX + "\n" + MESSAGE_USAGE);
        }
        model.resetDirectory(new Directory(targetIndex.getOneBased(), 0));
        EventsCenter.getInstance().post(
                new DirectoryChangedEvent(model.getCurrentDirectory()));
        EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex));
        return new CommandResult(String.format(MESSAGE_SELECT_ISSUE_SUCCESS, targetIndex.getOneBased()));
    }

    /**
     * execute command for selecting a {@code Solution}
     */
    private CommandResult selectSolution(Model model, CommandHistory history) throws CommandException {
        if (targetIndex.getZeroBased() >= model.getFilteredAndSortedSolutionList().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SOLUTION_DISPLAYED_INDEX + "\n" + MESSAGE_USAGE);
        }
        model.resetDirectory(new Directory(model.getCurrentDirectory().getIssue(), targetIndex.getOneBased()));
        //EventsCenter.getInstance().post(
        //        new DirectoryChangedEvent(model.getCurrentDirectory()));
        EventsCenter.getInstance().post(new JumpToSolutionListRequestEvent(targetIndex));
        return new CommandResult(
                String.format(MESSAGE_SELECT_ISSUE_SUCCESS, model.getCurrentDirectory().getIssue())
                        + String.format(MESSAGE_SELECT_SOLUTION_SUCCESS, targetIndex.getOneBased()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectCommand // instanceof handles nulls
                && targetIndex.equals(((SelectCommand) other).targetIndex)); // state check
    }
}
