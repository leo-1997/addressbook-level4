package seedu.saveit.logic;

import static seedu.saveit.logic.parser.ArgumentTokenizer.START_MARKER;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_DESCRIPTION_STRING;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_SOLUTION_LINK;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_STATEMENT;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_STATEMENT_STRING;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_TAG_STRING;
import static seedu.saveit.logic.parser.SaveItParser.BASIC_COMMAND_FORMAT;

import java.util.LinkedList;
import java.util.Optional;
import java.util.regex.Matcher;

import seedu.saveit.commons.core.index.Index;
import seedu.saveit.logic.commands.AddCommand;
import seedu.saveit.logic.commands.EditCommand;
import seedu.saveit.logic.commands.FindByTagCommand;
import seedu.saveit.logic.commands.FindCommand;
import seedu.saveit.logic.parser.ArgumentMultimap;
import seedu.saveit.logic.parser.ArgumentTokenizer;
import seedu.saveit.logic.parser.ParserUtil;
import seedu.saveit.logic.parser.Prefix;
import seedu.saveit.logic.parser.exceptions.ParseException;
import seedu.saveit.logic.suggestion.CopyExistingSuggestion;
import seedu.saveit.logic.suggestion.IssueNameSuggestion;
import seedu.saveit.logic.suggestion.Suggestion;
import seedu.saveit.logic.suggestion.SuggestionResult;
import seedu.saveit.logic.suggestion.TagNameSuggestion;
import seedu.saveit.model.Model;

/**
 * The TextField component which supports auto key word suggestion
 */
public class SuggestionLogicManager implements SuggestionLogic {

    private static final int INITIAL_CARET_POSITION = 0;

    private final Model model;
    private int caretPosition;

    public SuggestionLogicManager(Model model) {
        this.model = model;
        this.caretPosition = INITIAL_CARET_POSITION;
    }

    /**
     * Generates a {@code SuggestionResult} based on the {@code userInput}
     */
    @Override
    public SuggestionResult evaluate(String userInput) {
        Suggestion suggestion = parseUserInput(userInput);
        if (suggestion == null) {
            return new SuggestionResult(new LinkedList<>(), "", "", 0, 0);
        }
        return suggestion.evaluate();
    }

    /**
     * Called by the UI component
     */
    public void updateCaretPosition(int position) {
        this.caretPosition = position;
    }

    /**
     * Instantiates a {@code Suggestion} based on the {@code userInput}
     */
    public Suggestion parseUserInput(String userInput) {
        Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput);

        if (!matcher.matches()) {
            //let SaveItParser handle the invalid command format
            return null;
        }

        String commandWord = matcher.group("commandWord");
        String arguments = matcher.group("arguments");

        ArgumentMultimap argMultiMap =
                ArgumentTokenizer.tokenizeAndOffset(arguments, commandWord.length(),
                        PREFIX_STATEMENT, PREFIX_DESCRIPTION, PREFIX_SOLUTION_LINK, PREFIX_REMARK, PREFIX_TAG);

        // Nearest Prefix preceding the caret position
        Prefix startPrefix = argMultiMap.findPrecedingPrefixKey(caretPosition);
        if (startPrefix == null) {
            // TODO: Handle no startPrefix found
            return null;
        }

        Prefix endPrefix = argMultiMap.findSucceedingPrefixKey(startPrefix);

        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
        case AddCommand.COMMAND_ALIAS:
            return parseAddCommandSuggestion(argMultiMap, startPrefix, endPrefix);

        case EditCommand.COMMAND_WORD:
        case EditCommand.COMMAND_ALIAS:
            return parseEditCommandSuggestion(argMultiMap, startPrefix, endPrefix);

        case FindCommand.COMMAND_WORD:
        case FindCommand.COMMAND_ALIAS:
            return parseFindCommandSuggestion(argMultiMap, startPrefix, endPrefix);

        case FindByTagCommand.COMMAND_WORD:
        case FindByTagCommand.COMMAND_ALIAS:
            return parseFindByTagCommandSuggestion(argMultiMap, startPrefix, endPrefix);

        default:
            return null;
        }
    }

    //=================== Command Parsers ============================================

    /**
     * Determine which {@code Suggestion} to return given the arguments and caret position
     * for the {@code AddCommand}
     */
    private Suggestion parseAddCommandSuggestion(
            ArgumentMultimap argMultiMap, Prefix startPrefix, Prefix endPrefix) {
        Optional<String> posArgs = argMultiMap.getValueOrdered(startPrefix);

        switch (startPrefix.getPrefix()) {

        case PREFIX_TAG_STRING:
            return handleTagNameSuggestion(posArgs, startPrefix, endPrefix);

        default:
            return null;
        }
    }

    /**
     * Determine which {@code Suggestion} to return given the arguments and caret position
     * for the {@code EditCommand}
     */
    private Suggestion parseEditCommandSuggestion(
            ArgumentMultimap argMultiMap, Prefix startPrefix, Prefix endPrefix) {

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultiMap.getPreamble());
        } catch (ParseException pe) {
            // TODO: Throw some exception?
            return null;
        }

        Optional<String> posArgs = argMultiMap.getValueOrdered(startPrefix);

        switch (startPrefix.getPrefix()) {

        case PREFIX_STATEMENT_STRING:
        case PREFIX_DESCRIPTION_STRING:
            return handleCopyExistingSuggestion(posArgs, index, startPrefix, endPrefix);

        case PREFIX_TAG_STRING:
            return handleTagNameSuggestion(posArgs, startPrefix, endPrefix);

        default:
            return null;
        }
    }

    /**
     * Determine which {@code Suggestion} to return given the arguments and caret position
     * for the {@code FindCommand}
     */
    private Suggestion parseFindCommandSuggestion(
            ArgumentMultimap argMultiMap, Prefix startPrefix, Prefix endPrefix) {

        Optional<String> posArgs = argMultiMap.getValueOrdered(startPrefix);

        switch (startPrefix.getPrefix()) {

        case START_MARKER:
            return handleIssueNameSuggestion(posArgs, startPrefix, endPrefix);

        default:
            return null;
        }
    }

    /**
     * Determine which {@code Suggestion} to return given the arguments and caret position
     * for the {@code FindByTagCommand}
     */
    private Suggestion parseFindByTagCommandSuggestion(
            ArgumentMultimap argMultiMap, Prefix startPrefix, Prefix endPrefix) {

        Optional<String> posArgs = argMultiMap.getValueOrdered(startPrefix);

        switch (startPrefix.getPrefix()) {

        case START_MARKER:
            return handleTagNameSuggestion(posArgs, startPrefix, endPrefix);

        default:
            return null;
        }
    }

    //=================== Suggestion Handlers ============================================

    /**
     * Generates a {@code CopyExistingSuggestion} if the arguments are valid
     */
    private Suggestion handleCopyExistingSuggestion(
            Optional<String> posArgs, Index index, Prefix startPrefix, Prefix endPrefix) {
        // Only show Suggestion box if the string is empty
        if (posArgs.isPresent() && posArgs.get().length() == 0) {
            return new CopyExistingSuggestion(model, index, startPrefix, endPrefix);
        }
        return null;
    }

    /**
     * Generates a {@code TagNameSuggestion} if the arguments are valid
     */
    private Suggestion handleTagNameSuggestion(
            Optional<String> posArgs, Prefix startPrefix, Prefix endPrefix) {
        // Only show Suggestion box if the string is not empty
        if (posArgs.isPresent() && posArgs.get().length() > 0) {
            return new TagNameSuggestion(model, posArgs.get(), startPrefix, endPrefix);
        }
        return null;
    }

    /**
     * Generates a {@code TagNameSuggestion} if the arguments are valid
     */
    private Suggestion handleIssueNameSuggestion(
            Optional<String> posArgs, Prefix startPrefix, Prefix endPrefix) {
        // Only show Suggestion box if the string is not empty
        if (posArgs.isPresent() && posArgs.get().length() > 0) {
            return new IssueNameSuggestion(model, posArgs.get(), startPrefix, endPrefix);
        }
        return null;
    }
}
