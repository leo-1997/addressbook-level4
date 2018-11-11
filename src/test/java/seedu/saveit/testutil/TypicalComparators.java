package seedu.saveit.testutil;

import java.util.Comparator;

import seedu.saveit.model.Issue;
import seedu.saveit.model.issue.IssueChroComparator;
import seedu.saveit.model.issue.IssueFreqComparator;
import seedu.saveit.model.issue.IssueTagComparator;

/**
 * A utility class containing a list of {@code Comparator} objects to be used in tests.
 */
public class TypicalComparators {
    public static final Comparator<Issue> FREQUENCY_COMPARATOR = new IssueFreqComparator();
    public static final Comparator<Issue> TAG_COMPARATOR = new IssueTagComparator();
    public static final Comparator<Issue> CHRONOLOGY_COMPRATOR = new IssueChroComparator();
}
