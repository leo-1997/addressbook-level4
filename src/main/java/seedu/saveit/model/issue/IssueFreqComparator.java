package seedu.saveit.model.issue;

import java.util.Comparator;

import seedu.saveit.model.Issue;

/**
 * Comparator used to sort the Issues in search frequency order
 */
public class IssueFreqComparator implements Comparator<Issue> {
    /**
     * Compare Issue a and b with their searchFrequency.
     * If searchFrequency are the same, sort by created time.
     */
    public int compare(Issue a, Issue b) {
        int freqDiff = b.getFrequency().compare(a.getFrequency());
        if (freqDiff == 0) {
            return a.getCreatedTime().compareTo(b.getCreatedTime());
        } else {
            return freqDiff;
        }
    }

    @Override
    public String toString() {
        return "Search Frequency";
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof IssueFreqComparator;
    }
}
