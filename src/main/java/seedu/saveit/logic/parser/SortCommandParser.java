package seedu.saveit.logic.parser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import seedu.saveit.commons.core.Messages;
import seedu.saveit.logic.commands.SortCommand;
import seedu.saveit.logic.parser.exceptions.ParseException;
import seedu.saveit.model.issue.SortType;

/**
 * Parses input arguments and creates a new SortCommand object
 */
public class SortCommandParser implements Parser<SortCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SortCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        Set<String> sortTypes = new HashSet<>();
        sortTypes.add(SortType.EMPTY_SORT);
        sortTypes.add(SortType.CHRONOLOGICAL_SORT);
        sortTypes.add(SortType.FREQUENCY_SORT);
        sortTypes.add(SortType.TAG_SORT);

        if (Arrays.asList(trimmedArgs.isEmpty()).size() > 1) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE)
            );
        }

        if (!areSortTypeValid(sortTypes, trimmedArgs)) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        return new SortCommand(new SortType(trimmedArgs));
    }

    /**
     * Returns true if none of the sortType contains empty {@code Optional} values in the given {@code
     * ArgumentMultimap}.
     */
    private static boolean areSortTypeValid(Set<String> sortTypes, String sortType) {
        return sortTypes.stream().anyMatch(s -> s.equals(sortType));
    }
}
