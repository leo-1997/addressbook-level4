package seedu.saveit.logic.parser;

import static seedu.saveit.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.saveit.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.saveit.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.saveit.logic.commands.SortCommand;
import seedu.saveit.model.issue.SortType;

public class SortCommandParserTest {
    private SortCommandParser parser = new SortCommandParser();
    private String failureMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE);

    @Test
    public void parse_emptyArg_returnsSortCommand() {
        SortCommand expectedSortCommand =
                new SortCommand(new SortType(SortType.EMPTY_SORT));
        assertParseSuccess(parser, "     ", expectedSortCommand);
    }

    @Test
    public void parse_oneValidArg_returnsSortCommand() {
        // no leading and trailing whitespaces
        SortCommand expectedSortCommand =
                new SortCommand(new SortType(SortType.TAG_SORT));
        assertParseSuccess(parser, "tag", expectedSortCommand);

        // has leading and trailing whitespaces
        assertParseSuccess(parser, " \n tag  \t", expectedSortCommand);
    }

    @Test
    public void parse_invalidArgs_returnsSortCommand() {
        // one invalid argument
        assertParseFailure(parser, "random", failureMessage);

        // multiple arguments
        assertParseFailure(parser, "tag random", failureMessage);
        assertParseFailure(parser, "random invalid", failureMessage);
    }
}
