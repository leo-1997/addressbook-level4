package seedu.saveit.commons.core.directory;

/**
 * Represent the directory of current edition by user. It can be passed as parameters to communicate with other
 * components as the directory design is rather complicated. There are three levels: root/issue/solution.
 * issue and solution are all one based index. Zero value denotes root directory.
 */
public class Directory {
    private int issue;
    private int solution;

    /**
     * newIssue and newSolution should be non-negative values.
     */
    public Directory(int newIssue, int newSolution) {
        assert newIssue >= 0 && newSolution >= 0;
        issue = newIssue;
        solution = newSolution;
    }

    /**
     * newIssue and newSolution should be non-negative values.
     */
    public static Directory formRootDirectory() {
        return new Directory(0, 0);
    }

    /**
     * Judge whether the directory is at Root level.
    */
    public boolean isRootLevel() {
        return issue == 0;
    }

    /**
     * Judge whether the directory is at Issue level.
     */
    public boolean isIssueLevel() {
        return issue > 0 && solution == 0;
    }

    /**
     * Judge whether the directory is at Solution level.
     */
    public boolean isSolutionLevel() {
        return issue > 0 && solution > 0;
    }

    public int getIssue() {
        return issue;
    }

    public int getSolution() {
        return solution;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("SaveIt/");
        if (issue != 0) {
            result.append("Issue ");
            result.append(issue);
            if (solution != 0) {
                result.append("/");
                result.append("Solution ");
                result.append(solution);
            }
        }
        return result.toString();
    }
}
