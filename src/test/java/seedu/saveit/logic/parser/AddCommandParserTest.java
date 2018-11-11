package seedu.saveit.logic.parser;

import static seedu.saveit.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.saveit.logic.commands.CommandTestUtil.DESCRIPTION_DESC_C;
import static seedu.saveit.logic.commands.CommandTestUtil.DESCRIPTION_DESC_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.saveit.logic.commands.CommandTestUtil.INVALID_REMARK;
import static seedu.saveit.logic.commands.CommandTestUtil.INVALID_SOLUTION_LINK;
import static seedu.saveit.logic.commands.CommandTestUtil.INVALID_STATEMENT_DESC;
import static seedu.saveit.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.saveit.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.saveit.logic.commands.CommandTestUtil.REMARK_DES_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.REMARK_DES_STACKOVERFLOW;
import static seedu.saveit.logic.commands.CommandTestUtil.SOLUTION_DESC_C;
import static seedu.saveit.logic.commands.CommandTestUtil.SOLUTION_DESC_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.SOLUTION_LINK_DES_C;
import static seedu.saveit.logic.commands.CommandTestUtil.SOLUTION_LINK_DES_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.STATEMENT_DESC_C;
import static seedu.saveit.logic.commands.CommandTestUtil.STATEMENT_DESC_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.TAG_DESC_SYNTAX;
import static seedu.saveit.logic.commands.CommandTestUtil.TAG_DESC_UI;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_DESCRIPTION_C;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_REMARK_C;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_SOLUTION_LINK_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_STATEMENT_C;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_TAG_SYNTAX;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_TAG_UI;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_SOLUTION_LINK;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_STATEMENT;
import static seedu.saveit.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.saveit.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.saveit.model.issue.solution.Remark.MESSAGE_REMARK_CONSTRAINTS;
import static seedu.saveit.model.issue.solution.SolutionLink.MESSAGE_SOLUTION_LINK_CONSTRAINTS;
import static seedu.saveit.testutil.TypicalIssues.DUMMY_ISSUE;
import static seedu.saveit.testutil.TypicalIssues.INITIALIZED_ISSUE_FREQUENCY;
import static seedu.saveit.testutil.TypicalIssues.VALID_C_ISSUE;
import static seedu.saveit.testutil.TypicalIssues.VALID_JAVA_ISSUE;
import static seedu.saveit.testutil.TypicalSolutions.SOLUTION_JAVA;

import org.junit.Test;

import seedu.saveit.logic.commands.AddCommand;
import seedu.saveit.logic.commands.CommandTestUtil;
import seedu.saveit.model.Issue;
import seedu.saveit.model.issue.Description;
import seedu.saveit.model.issue.IssueStatement;
import seedu.saveit.testutil.IssueBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parseIssue_allFieldsPresent_success() {
        Issue expectedIssue = new IssueBuilder(VALID_C_ISSUE).withTags(VALID_TAG_UI).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + STATEMENT_DESC_C + DESCRIPTION_DESC_C
                + CommandTestUtil.TAG_DESC_UI, new AddCommand(expectedIssue));

        // multiple names - last name accepted
        assertParseSuccess(parser, STATEMENT_DESC_JAVA + STATEMENT_DESC_C + DESCRIPTION_DESC_C
                + CommandTestUtil.TAG_DESC_UI, new AddCommand(expectedIssue));

        // multiple descriptions - last descriptions accepted
        assertParseSuccess(parser, STATEMENT_DESC_C + DESCRIPTION_DESC_JAVA + DESCRIPTION_DESC_C
                + CommandTestUtil.TAG_DESC_UI, new AddCommand(expectedIssue));

        // multiple solutions - all accepted
        Issue expectedIssueMultipleSolutions = new IssueBuilder(DUMMY_ISSUE)
                .withSolutions(SOLUTION_JAVA)
                .build();
        assertParseSuccess(parser, SOLUTION_DESC_C + SOLUTION_DESC_JAVA,
                new AddCommand(expectedIssueMultipleSolutions));

        // multiple tags - all accepted
        Issue expectedIssueMultipleTags = new IssueBuilder(VALID_C_ISSUE).withTags(VALID_TAG_SYNTAX, VALID_TAG_UI)
                .build();
        assertParseSuccess(parser, STATEMENT_DESC_C + DESCRIPTION_DESC_C
                + TAG_DESC_SYNTAX + CommandTestUtil.TAG_DESC_UI, new AddCommand(expectedIssueMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Issue expectedIssue = new IssueBuilder(VALID_JAVA_ISSUE).withTags().withSolutions()
                .withFrequency(INITIALIZED_ISSUE_FREQUENCY).build();

        assertParseSuccess(parser, STATEMENT_DESC_JAVA + DESCRIPTION_DESC_JAVA,
                new AddCommand(expectedIssue));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing statement prefix
        assertParseFailure(parser, VALID_STATEMENT_C + DESCRIPTION_DESC_C,
                expectedMessage);

        // missing description prefix
        assertParseFailure(parser, STATEMENT_DESC_C + VALID_DESCRIPTION_C,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_STATEMENT_C + VALID_DESCRIPTION_C,
                expectedMessage);

        // missing solution link prefix
        assertParseFailure(parser, VALID_SOLUTION_LINK_JAVA + REMARK_DES_JAVA,
                expectedMessage);

        // missing remark prefix
        assertParseFailure(parser, SOLUTION_LINK_DES_C + VALID_REMARK_C,
                expectedMessage);
    }

    @Test
    public void parse_compulsoryFieldValueMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing statement value
        assertParseFailure(parser, PREFIX_STATEMENT + DESCRIPTION_DESC_C,
                expectedMessage);
        // missing description value
        assertParseFailure(parser, STATEMENT_DESC_C + PREFIX_DESCRIPTION,
                expectedMessage);
        // missing solution link value
        assertParseFailure(parser, PREFIX_SOLUTION_LINK + REMARK_DES_JAVA,
                expectedMessage);
        // missing solution remark value
        assertParseFailure(parser, SOLUTION_LINK_DES_C + PREFIX_REMARK,
                expectedMessage);
    }

    @Test
    public void parse_rootLevelAndIssueLevelPresent_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        //all fields present at the same time
        assertParseFailure(parser, PREFIX_STATEMENT + DESCRIPTION_DESC_C + SOLUTION_DESC_JAVA
                + TAG_DESC_SYNTAX + CommandTestUtil.TAG_DESC_UI, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_STATEMENT_DESC + DESCRIPTION_DESC_C
                + TAG_DESC_UI + CommandTestUtil.TAG_DESC_UI, IssueStatement.MESSAGE_ISSUE_STATEMENT_CONSTRAINTS);

        // invalid descriptions
        assertParseFailure(parser, STATEMENT_DESC_C + INVALID_DESCRIPTION_DESC
                + TAG_DESC_UI + CommandTestUtil.TAG_DESC_UI, Description.MESSAGE_DESCRIPTION_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + STATEMENT_DESC_C + DESCRIPTION_DESC_C
                + TAG_DESC_UI + CommandTestUtil.TAG_DESC_UI,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        //invalid solution link
        assertParseFailure(parser, INVALID_SOLUTION_LINK + REMARK_DES_STACKOVERFLOW,
                String.format(MESSAGE_SOLUTION_LINK_CONSTRAINTS, AddCommand.MESSAGE_USAGE));

        //invalid solution remark
        assertParseFailure(parser, SOLUTION_LINK_DES_JAVA + INVALID_REMARK,
                String.format(MESSAGE_REMARK_CONSTRAINTS, AddCommand.MESSAGE_USAGE));
    }
}
