package seedu.saveit.testutil;

import seedu.saveit.commons.core.directory.Directory;

/**
 * A utility class containing a list of {@code Directory} objects to be used in tests.
 */
public class TypicalDirectories {
    private static final int DEFAULT_ISSUE_INDEX = 0;
    private static final int ISSUE_INDEX = 5;
    private static final int DEFAULT_SOLUTION_INDEX = 0;

    public static final Directory ROOT_LEVEL = new Directory(DEFAULT_ISSUE_INDEX, DEFAULT_SOLUTION_INDEX);
    public static final Directory ISSUE_LEVEL = new Directory(ISSUE_INDEX, DEFAULT_SOLUTION_INDEX);
}
