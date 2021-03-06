package seedu.saveit.model.issue;

import java.util.List;
import java.util.function.Predicate;

import seedu.saveit.commons.util.StringUtil;
import seedu.saveit.model.Issue;

/**
 * Tests that a {@code Issue}'s {@code IssueStatement} matches any of the keywords given.
 */
public class IssueContainsKeywordsPredicate implements Predicate<Issue> {
    private final List<String> keywords;

    public IssueContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Issue issue) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.partialMatch(issue.getStatement().getValue(), keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof IssueContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((IssueContainsKeywordsPredicate) other).keywords)); // state check
    }
}
