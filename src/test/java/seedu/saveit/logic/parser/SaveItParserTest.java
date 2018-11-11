package seedu.saveit.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.saveit.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.saveit.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.saveit.logic.commands.CommandTestUtil.NEWTAG_DESC_SYNTAX;
import static seedu.saveit.logic.commands.CommandTestUtil.TAG_DESC_PYTHON;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_TAG_PYTHON;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_TAG_SYNTAX;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_FIRST_ISSUE;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_THIRD_ISSUE;
import static seedu.saveit.testutil.TypicalIssues.INITIALIZED_ISSUE_FREQUENCY;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.saveit.commons.core.index.Index;
import seedu.saveit.logic.commands.AddCommand;
import seedu.saveit.logic.commands.AddTagCommand;
import seedu.saveit.logic.commands.ClearCommand;
import seedu.saveit.logic.commands.DeleteCommand;
import seedu.saveit.logic.commands.EditCommand;
import seedu.saveit.logic.commands.EditCommand.EditIssueDescriptor;
import seedu.saveit.logic.commands.ExitCommand;
import seedu.saveit.logic.commands.FindByTagCommand;
import seedu.saveit.logic.commands.FindCommand;
import seedu.saveit.logic.commands.HelpCommand;
import seedu.saveit.logic.commands.HistoryCommand;
import seedu.saveit.logic.commands.HomeCommand;
import seedu.saveit.logic.commands.ListCommand;
import seedu.saveit.logic.commands.RedoCommand;
import seedu.saveit.logic.commands.RefactorTagCommand;
import seedu.saveit.logic.commands.ResetPrimaryCommand;
import seedu.saveit.logic.commands.RetrieveCommand;
import seedu.saveit.logic.commands.SelectCommand;
import seedu.saveit.logic.commands.SetPrimaryCommand;
import seedu.saveit.logic.commands.SortCommand;
import seedu.saveit.logic.commands.UndoCommand;
import seedu.saveit.logic.parser.exceptions.ParseException;
import seedu.saveit.model.Issue;
import seedu.saveit.model.issue.IssueContainsKeywordsPredicate;
import seedu.saveit.model.issue.Tag;
import seedu.saveit.testutil.EditIssueDescriptorBuilder;
import seedu.saveit.testutil.IssueBuilder;
import seedu.saveit.testutil.IssueUtil;

public class SaveItParserTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final SaveItParser parser = new SaveItParser();

    @Test
    public void parseCommand_add() throws Exception {
        Issue issue = new IssueBuilder().withFrequency(INITIALIZED_ISSUE_FREQUENCY).build();
        AddCommand command = (AddCommand) parser.parseCommand(IssueUtil.getAddCommand(issue));
        assertEquals(new AddCommand(issue), command);
    }

    @Test
    public void parseCommand_addTag() throws Exception {
        AddTagCommand command = (AddTagCommand) parser
            .parseCommand(AddTagCommand.COMMAND_WORD + " 3" + TAG_DESC_PYTHON);
        Set<Tag> tagList = new LinkedHashSet<Tag>();
        tagList.add(new Tag(VALID_TAG_PYTHON));
        Set<Index> indexSet = new LinkedHashSet<Index>();
        indexSet.add(INDEX_THIRD_ISSUE);
        assertEquals(new AddTagCommand(indexSet, tagList), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
            DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_ISSUE.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_ISSUE), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Issue issue = new IssueBuilder().build();
        EditIssueDescriptor descriptor = new EditIssueDescriptorBuilder(issue).build();

        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
            + INDEX_FIRST_ISSUE.getOneBased() + " " + IssueUtil.getEditIssueDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_ISSUE, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
            FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new IssueContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findByTag() throws Exception {
        assertTrue(parser.parseCommand(FindByTagCommand.COMMAND_WORD + " tag") instanceof FindByTagCommand);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_history() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            throw new AssertionError("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommand_home() throws Exception {
        assertTrue(parser.parseCommand(HomeCommand.COMMAND_WORD) instanceof HomeCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " freq") instanceof ListCommand);
    }

    @Test
    public void parseCommand_select() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
            SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_ISSUE.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_ISSUE), command);
    }

    @Test
    public void parseCommand_redoCommandWord_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_retrieveCommand_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RetrieveCommand.COMMAND_WORD + " 1") instanceof RetrieveCommand);
    }

    @Test
    public void parseCommand_undoCommandWord_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_setPrimaryCommand() throws Exception {
        assertTrue(parser.parseCommand("setprimary 2") instanceof SetPrimaryCommand);


    }

    @Test
    public void parseCommand_resetPrimaryCommand() throws Exception {
        assertTrue(parser.parseCommand(ResetPrimaryCommand.COMMAND_WORD) instanceof ResetPrimaryCommand);
        assertTrue(parser.parseCommand("resetprimary") instanceof ResetPrimaryCommand);
    }

    @Test
    public void parseCommand_refactorTagCommand() throws Exception {
        RefactorTagCommand command = (RefactorTagCommand) parser
            .parseCommand(RefactorTagCommand.COMMAND_WORD + TAG_DESC_PYTHON);
        assertEquals(new RefactorTagCommand(new Tag(VALID_TAG_PYTHON)), command);

        command = (RefactorTagCommand) parser
            .parseCommand(RefactorTagCommand.COMMAND_WORD + TAG_DESC_PYTHON + NEWTAG_DESC_SYNTAX);
        assertEquals(new RefactorTagCommand(new Tag(VALID_TAG_PYTHON), new Tag(VALID_TAG_SYNTAX)), command);
    }

    @Test
    public void parseCommand_sortCommand() throws Exception {
        assertTrue(parser.parseCommand(SortCommand.COMMAND_WORD) instanceof SortCommand);
        assertTrue(parser.parseCommand("sort chro") instanceof SortCommand);
        assertTrue(parser.parseCommand("sort tag") instanceof SortCommand);
        assertTrue(parser.parseCommand("sort freq") instanceof SortCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.parseCommand("unknownCommand");
    }
}
