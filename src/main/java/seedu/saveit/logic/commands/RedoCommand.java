package seedu.saveit.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.saveit.commons.core.EventsCenter;
import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.commons.events.model.DirectoryChangedEvent;
import seedu.saveit.logic.CommandHistory;
import seedu.saveit.logic.commands.exceptions.CommandException;
import seedu.saveit.model.Model;

/**
 * Reverts the {@code model}'s saveIt to its previously undone state.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String COMMAND_ALIAS = "r";

    public static final String MESSAGE_SUCCESS = "Redo success!";
    public static final String MESSAGE_FAILURE = "No more commands to redo!";

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (!model.canRedoSaveIt()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.redoSaveIt();
        model.updateFilteredIssueList(Model.PREDICATE_SHOW_ALL_ISSUES);
        model.resetDirectory(new Directory(0, 0));
        EventsCenter.getInstance().post(new DirectoryChangedEvent(Directory.formRootDirectory()));
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
