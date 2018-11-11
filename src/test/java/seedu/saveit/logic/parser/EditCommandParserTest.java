package seedu.saveit.logic.parser;

import static seedu.saveit.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.saveit.logic.commands.CommandTestUtil.DESCRIPTION_DESC_C;
import static seedu.saveit.logic.commands.CommandTestUtil.DESCRIPTION_DESC_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.saveit.logic.commands.CommandTestUtil.INVALID_SOLUTION_LINK;
import static seedu.saveit.logic.commands.CommandTestUtil.INVALID_STATEMENT_DESC;
import static seedu.saveit.logic.commands.CommandTestUtil.STATEMENT_DESC_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.TAG_DESC_UI;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_DESCRIPTION_C;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_DESCRIPTION_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_STATEMENT_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_TAG_SYNTAX;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_TAG_UI;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_STATEMENT;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.saveit.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.saveit.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_FIRST_ISSUE;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_SECOND_ISSUE;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_THIRD_ISSUE;

import org.junit.Test;

import seedu.saveit.commons.core.index.Index;
import seedu.saveit.logic.commands.CommandTestUtil;
import seedu.saveit.logic.commands.EditCommand;
import seedu.saveit.model.issue.Description;
import seedu.saveit.model.issue.IssueStatement;
import seedu.saveit.model.issue.Tag;
import seedu.saveit.model.issue.solution.SolutionLink;
import seedu.saveit.testutil.EditIssueDescriptorBuilder;

public class EditCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;
    private static final String STATEMENT_EMPTY = " " + PREFIX_STATEMENT;


    private static final String MESSAGE_INVALID_FORMAT =
        String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, STATEMENT_EMPTY + VALID_STATEMENT_JAVA, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + STATEMENT_DESC_JAVA, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + STATEMENT_DESC_JAVA, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 n/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_STATEMENT_DESC,
            IssueStatement.MESSAGE_ISSUE_STATEMENT_CONSTRAINTS); // invalid statement
        assertParseFailure(parser, "1" + INVALID_DESCRIPTION_DESC,
            Description.MESSAGE_DESCRIPTION_CONSTRAINTS); // invalid description

        // valid description followed by invalid description.
        // The test case for invalid description followed by valid description
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, "1" + DESCRIPTION_DESC_C
            + INVALID_DESCRIPTION_DESC, Description.MESSAGE_DESCRIPTION_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Issue} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + CommandTestUtil.TAG_DESC_UI + TAG_DESC_UI + TAG_EMPTY,
            Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(parser, "1" + CommandTestUtil.TAG_DESC_UI + TAG_EMPTY + TAG_DESC_UI,
            Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + CommandTestUtil.TAG_DESC_UI + TAG_DESC_UI,
            Tag.MESSAGE_TAG_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_STATEMENT_DESC + INVALID_DESCRIPTION_DESC + TAG_DESC_UI,
            IssueStatement.MESSAGE_ISSUE_STATEMENT_CONSTRAINTS);

        // invalid solution link
        assertParseFailure(parser, "1" + INVALID_SOLUTION_LINK, SolutionLink.MESSAGE_SOLUTION_LINK_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_ISSUE;
        String userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_C + TAG_DESC_UI
            + STATEMENT_DESC_JAVA + CommandTestUtil.TAG_DESC_SYNTAX;

        EditCommand.EditIssueDescriptor descriptor = new EditIssueDescriptorBuilder()
                .withStatement(VALID_STATEMENT_JAVA)
                .withDescription(VALID_DESCRIPTION_C)
                .withTags(VALID_TAG_UI, VALID_TAG_SYNTAX).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }



    // TODO: test solution-level edit

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_ISSUE;
        String userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_C;

        EditCommand.EditIssueDescriptor descriptor =
                new EditIssueDescriptorBuilder().withDescription(VALID_DESCRIPTION_C).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // statement
        Index targetIndex = INDEX_THIRD_ISSUE;
        String userInput = targetIndex.getOneBased() + STATEMENT_DESC_JAVA;
        EditCommand.EditIssueDescriptor descriptor = new EditIssueDescriptorBuilder()
                .withStatement(VALID_STATEMENT_JAVA).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // description
        userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_JAVA;
        descriptor = new EditIssueDescriptorBuilder().withDescription(VALID_DESCRIPTION_JAVA).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // TODO: solution

        // tags
        userInput = targetIndex.getOneBased() + CommandTestUtil.TAG_DESC_UI;
        descriptor = new EditIssueDescriptorBuilder().withTags(VALID_TAG_UI).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_ISSUE;
        String userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_JAVA
                + CommandTestUtil.TAG_DESC_UI + DESCRIPTION_DESC_JAVA
                + CommandTestUtil.TAG_DESC_SYNTAX
                + DESCRIPTION_DESC_C + TAG_DESC_UI;

        EditCommand.EditIssueDescriptor descriptor = new EditIssueDescriptorBuilder()
                .withDescription(VALID_DESCRIPTION_C)
                .withTags(VALID_TAG_SYNTAX, VALID_TAG_UI)
                .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    // TODO: test solution-level multiple repeated fields edit

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_ISSUE;
        String userInput = targetIndex.getOneBased() + INVALID_DESCRIPTION_DESC + DESCRIPTION_DESC_C;
        System.out.println(userInput);
        EditCommand.EditIssueDescriptor descriptor =
            new EditIssueDescriptorBuilder().withDescription(VALID_DESCRIPTION_C).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + INVALID_DESCRIPTION_DESC + DESCRIPTION_DESC_C + TAG_DESC_UI;
        descriptor = new EditIssueDescriptorBuilder()
                .withDescription(VALID_DESCRIPTION_C).withTags(VALID_TAG_UI).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_ISSUE;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditCommand.EditIssueDescriptor descriptor = new EditIssueDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
