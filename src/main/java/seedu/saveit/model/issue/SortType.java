package seedu.saveit.model.issue;

import java.util.Comparator;

import seedu.saveit.model.Issue;

/**
 * Create respective Comparator based on the command inputted.
 */
public class SortType {
    public static final String FREQUENCY_SORT = "freq";
    public static final String CHRONOLOGICAL_SORT = "chro";
    public static final String TAG_SORT = "tag";
    public static final String EMPTY_SORT = "";
    public static final String FREQUENCY = "search frequency";
    public static final String CHRONOLOGICAL = "last modified time";
    public static final String TAG = "tag in alphabetical order";
    public static final String DEFAULT = "added time";
    private final Comparator<Issue> comparator;
    private final String sortType;

    public SortType(String sortType) {
        switch (sortType) {
        case FREQUENCY_SORT:
            this.comparator = new IssueFreqComparator();
            this.sortType = FREQUENCY;
            break;
        case CHRONOLOGICAL_SORT:
            this.comparator = new IssueChroComparator();
            this.sortType = CHRONOLOGICAL;
            break;
        case TAG_SORT:
            this.comparator = new IssueTagComparator();
            this.sortType = TAG;
            break;
        case EMPTY_SORT:default:
            this.comparator = null;
            this.sortType = DEFAULT;
        }
    }

    public Comparator<Issue> getComparator() {
        return this.comparator;
    }

    public String getSortType() {
        return this.sortType;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SortType // instanceof handles nulls
                && sortType.equals(((SortType) other).sortType));
    }
}
