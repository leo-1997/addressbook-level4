package seedu.saveit.logic.commands;

import seedu.saveit.commons.core.index.Index;
import seedu.saveit.logic.CommandHistory;
import seedu.saveit.model.Model;


/**
 * Terminates the program.
 */
public class HomeCommand extends Command {

    public static final String COMMAND_WORD = "home";
    public static final String COMMAND_ALIAS = "hm";

    public static final String MESSAGE_SUCCESS = "Return to the home directory.";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        model.updateFilteredIssueList(Model.PREDICATE_SHOW_ALL_ISSUES);
        model.resetDirectory(Index.fromZeroBased(ROOT_DIRECTORY), true);
        return new CommandResult(MESSAGE_SUCCESS);
    }

}