package seedu.saveit.model.issue;

import java.util.Comparator;

import seedu.saveit.model.Issue;

/**
 * Comparator used to sort the Issues in chronological order
 */
public class IssueChroComparator implements Comparator<Issue> {
    /**
     * Compare Issue a and b with their lastModifiedTime.
     * If lastModifiedTime are the same, sort by created time.
     */
    public int compare(Issue a, Issue b) {
        int timeDiff = b.getLastModifiedTime().compareTo(a.getLastModifiedTime());
        if (timeDiff == 0) {
            return a.getCreatedTime().compareTo(b.getCreatedTime());
        } else {
            return timeDiff;
        }
    }

    @Override
    public String toString() {
        return "Chronological";
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof IssueChroComparator;
    }
}
