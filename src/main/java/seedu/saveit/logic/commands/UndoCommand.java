package seedu.saveit.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.saveit.commons.core.EventsCenter;
import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.commons.events.model.DirectoryChangedEvent;
import seedu.saveit.logic.CommandHistory;
import seedu.saveit.logic.commands.exceptions.CommandException;
import seedu.saveit.model.Model;

/**
 * Reverts the {@code model}'s saveIt to its previous state.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String COMMAND_ALIAS = "u";

    public static final String MESSAGE_SUCCESS = "Undo success!";
    public static final String MESSAGE_FAILURE = "No more commands to undo!";

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (!model.canUndoSaveIt()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.undoSaveIt();
        model.updateFilteredIssueList(Model.PREDICATE_SHOW_ALL_ISSUES);
        model.resetDirectory(new Directory(0, 0));
        EventsCenter.getInstance().post(new DirectoryChangedEvent(Directory.formRootDirectory()));
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
