package seedu.saveit.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.saveit.commons.core.Messages;
import seedu.saveit.logic.CommandHistory;
import seedu.saveit.logic.commands.exceptions.CommandException;
import seedu.saveit.model.Model;

/**
 * Lists all issues in the saveIt to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String COMMAND_ALIAS = "l";

    public static final String MESSAGE_SUCCESS = "Listed all issues";

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        if (!model.getCurrentDirectory().isRootLevel()) {
            throw new CommandException(Messages.MESSAGE_WRONG_DIRECTORY);
        }
        model.updateFilteredIssueList(Model.PREDICATE_SHOW_ALL_ISSUES);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
