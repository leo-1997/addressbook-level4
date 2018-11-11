package seedu.saveit.testutil;

import static seedu.saveit.logic.commands.CommandTestUtil.VALID_REMARK_C;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_REMARK_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_REMARK_STACKOVERFLOW;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_SOLUTION_LINK_C;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_SOLUTION_LINK_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_SOLUTION_LINK_STACKOVERFLOW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.saveit.model.issue.Solution;

/**
 * A utility class containing a list of {@code Solution} objects to be used in tests.
 */
public class TypicalSolutions {
    public static final Solution SOLUTION_REPO = new SolutionBuilder()
            .withLink("https://github.com/CS2103-AY1819S1-T12-4/main").withRemark("repoPage").build();
    public static final Solution SOLUTION_ZHIHU = new SolutionBuilder()
            .withLink("http://www.zhihu.com").withRemark("newSol").build();
    public static final Solution SOLUTION_GOOGLE = new SolutionBuilder()
            .withLink("http://www.google.com").withRemark("ave").build();
    public static final Solution SOLUTION_REDDIT = new SolutionBuilder()
            .withLink("http://www.reddit.com").withRemark("Reddit").build();

    // Manually added
    public static final Solution SOLUTION_C = new SolutionBuilder().withLink(VALID_SOLUTION_LINK_C)
            .withRemark(VALID_REMARK_C).build();
    public static final Solution SOLUTION_JAVA = new SolutionBuilder().withLink(VALID_SOLUTION_LINK_JAVA)
            .withRemark(VALID_REMARK_JAVA).build();
    public static final Solution SOLUTION_STACKOVERFLOW = new SolutionBuilder()
            .withLink(VALID_SOLUTION_LINK_STACKOVERFLOW).withRemark(VALID_REMARK_STACKOVERFLOW).build();

    private TypicalSolutions() {
    } // prevents instantiation

    public static List<Solution> getTypicalSolutions() {
        return new ArrayList<>(Arrays.asList(SOLUTION_JAVA, SOLUTION_C,
                SOLUTION_STACKOVERFLOW, SOLUTION_REPO, SOLUTION_GOOGLE, SOLUTION_REDDIT, SOLUTION_ZHIHU));
    }
}
