package seedu.saveit.logic.suggestion;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import seedu.saveit.commons.util.StringUtil;
import seedu.saveit.logic.parser.ArgumentTokenizer;
import seedu.saveit.logic.parser.Prefix;
import seedu.saveit.model.Model;

/**
 * The suggestion component which stores and provides issue statement key words
 */
public class IssueNameSuggestion implements Suggestion {

    public static final String STATEMENT_SUCCESS = "Existing Statement selected";
    public static final int WHITE_SPACE_OFFSET = 1;

    private Model model;
    private String argument;
    private Prefix startPrefix;
    private Prefix endPrefix;

    public IssueNameSuggestion(Model model, String argument, Prefix startPrefix, Prefix endPrefix) {
        this.model = model;
        this.argument = argument;
        this.startPrefix = startPrefix;
        this.endPrefix = endPrefix;
    }

    /**
     * Compares and match the keywords.
     */
    @Override
    public SuggestionResult evaluate() {
        List<String> statements = model.getCurrentIssueStatementSet()
                .stream()
                .filter(statement -> StringUtil.partialMatchFromStart(statement, argument))
                .collect(Collectors.toList());
        statements.sort(String.CASE_INSENSITIVE_ORDER);

        LinkedList<SuggestionValue> values = new LinkedList<>();
        for (String statement : statements) {
            values.add(new SuggestionValue(statement, statement));
        }

        int startPosition = startPrefix.getPosition() + startPrefix.getPrefix().length() + WHITE_SPACE_OFFSET;
        int endPosition = endPrefix.getPrefix() == ArgumentTokenizer.END_MARKER
                ? endPrefix.getPosition() : endPrefix.getPosition() - 1;

        return new SuggestionResult(values, STATEMENT_SUCCESS, argument, startPosition, endPosition);
    }
}
