package seedu.saveit.testutil;

import static seedu.saveit.logic.commands.CommandTestUtil.CHECKSTYLE_ERROR_DESCRIPTION;
import static seedu.saveit.logic.commands.CommandTestUtil.CHECKSTYLE_ERROR_STATEMENT;
import static seedu.saveit.logic.commands.CommandTestUtil.C_RACE_CONDITION_DESCRIPTION;
import static seedu.saveit.logic.commands.CommandTestUtil.C_RACE_CONDITION_STATEMENT;
import static seedu.saveit.logic.commands.CommandTestUtil.C_SEGMENTATION_FAULT_DESCRIPTION;
import static seedu.saveit.logic.commands.CommandTestUtil.C_SEGMENTATION_FAULT_STATEMENT;
import static seedu.saveit.logic.commands.CommandTestUtil.DUMMY_DESCRIPTION;
import static seedu.saveit.logic.commands.CommandTestUtil.DUMMY_STATEMENT;
import static seedu.saveit.logic.commands.CommandTestUtil.JAVA_NULL_POINTER_DESCRIPTION;
import static seedu.saveit.logic.commands.CommandTestUtil.JAVA_NULL_POINTER_STATEMENT;
import static seedu.saveit.logic.commands.CommandTestUtil.MYSQL_ERROR_DESCRIPTION;
import static seedu.saveit.logic.commands.CommandTestUtil.MYSQL_ERROR_STATEMENT;
import static seedu.saveit.logic.commands.CommandTestUtil.POSTGRESQL_ERROR_DESCRIPTION;
import static seedu.saveit.logic.commands.CommandTestUtil.POSTGRESQL_ERROR_STATEMENT;
import static seedu.saveit.logic.commands.CommandTestUtil.QUICKSORT_BUG_DESCRIPTION;
import static seedu.saveit.logic.commands.CommandTestUtil.QUICKSORT_BUG_STATEMENT;
import static seedu.saveit.logic.commands.CommandTestUtil.RUBY_HASH_BUG_DESCRIPTION;
import static seedu.saveit.logic.commands.CommandTestUtil.RUBY_HASH_BUG_STATEMENT;
import static seedu.saveit.logic.commands.CommandTestUtil.TRAVIS_BUILD_DESCRIPTION;
import static seedu.saveit.logic.commands.CommandTestUtil.TRAVIS_BUILD_STATEMENT;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_DESCRIPTION_C;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_DESCRIPTION_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_STATEMENT_C;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_STATEMENT_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_TAG_BUG;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_TAG_CPP;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_TAG_SYNTAX;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_TAG_TRAVIS;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_TAG_UI;
import static seedu.saveit.testutil.TypicalSolutions.SOLUTION_C;
import static seedu.saveit.testutil.TypicalSolutions.SOLUTION_JAVA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.saveit.model.Issue;
import seedu.saveit.model.SaveIt;

/**
 * A utility class containing a list of {@code Issue} objects to be used in tests.
 */
public class TypicalIssues {

    // {@code IssueSearchFrequency} value when first initialized
    public static final Integer INITIALIZED_ISSUE_FREQUENCY = 0;
    // {@code IssueSearchFrequency} value commonly used across the test cases
    public static final Integer COMMON_ISSUE_FREQUENCY = 1;
    public static final long COMMON_ISSUE_TIME = 0;

    public static final Issue JAVA_NULL_POINTER = new IssueBuilder()
            .withStatement(JAVA_NULL_POINTER_STATEMENT)
            .withDescription(JAVA_NULL_POINTER_DESCRIPTION)
            .withTags(VALID_TAG_SYNTAX)
            .withFrequency(COMMON_ISSUE_FREQUENCY)
            .withLastModifiedTime(COMMON_ISSUE_TIME + 10).build();
    public static final Issue C_SEGMENTATION_FAULT = new IssueBuilder()
            .withStatement(C_SEGMENTATION_FAULT_STATEMENT)
            .withDescription(C_SEGMENTATION_FAULT_DESCRIPTION)
            .withTags(VALID_TAG_CPP, VALID_TAG_BUG)
            .withFrequency(COMMON_ISSUE_FREQUENCY)
            .withLastModifiedTime(COMMON_ISSUE_TIME + 20).build();
    public static final Issue RUBY_HASH_BUG = new IssueBuilder()
            .withStatement(RUBY_HASH_BUG_STATEMENT)
            .withDescription(RUBY_HASH_BUG_DESCRIPTION)
            .withSolutions(new SolutionBuilder()
                    .withLink("https://github.com/CS2103-AY1819S1-T12-4/main")
                    .withRemark("repoPage").build())
            .withFrequency(COMMON_ISSUE_FREQUENCY)
            .withLastModifiedTime(COMMON_ISSUE_TIME + 30).build();
    public static final Issue TRAVIS_BUILD = new IssueBuilder()
            .withStatement(TRAVIS_BUILD_STATEMENT)
            .withDescription(TRAVIS_BUILD_DESCRIPTION)
            .withTags(VALID_TAG_TRAVIS)
            .withSolutions(
                    new SolutionBuilder().withLink("https://stackoverflow.com/").withRemark("newSolution")
                            .build(),
                    new SolutionBuilder().withLink("http://www.ZhiHu.com").withRemark("newSol").build())
            .withFrequency(COMMON_ISSUE_FREQUENCY)
            .build();
    public static final Issue CHECKSTYLE_ERROR = new IssueBuilder()
            .withStatement(CHECKSTYLE_ERROR_STATEMENT)
            .withDescription(CHECKSTYLE_ERROR_DESCRIPTION)
            .withSolutions(
                    new SolutionBuilder().withLink("http://www.google.com").withRemark("ave").build())
            .withFrequency(COMMON_ISSUE_FREQUENCY)
            .withLastModifiedTime(COMMON_ISSUE_TIME + 50).build();
    public static final Issue QUICKSORT_BUG = new IssueBuilder()
            .withStatement(QUICKSORT_BUG_STATEMENT)
            .withDescription(QUICKSORT_BUG_DESCRIPTION)
            .withSolutions(new SolutionBuilder().withLink("http://www.reddit.com").withRemark("tokyo").build())
            .withFrequency(COMMON_ISSUE_FREQUENCY)
            .build();

    public static final Issue C_RACE_CONDITION = new IssueBuilder()
            .withStatement(C_RACE_CONDITION_STATEMENT)
            .withDescription(C_RACE_CONDITION_DESCRIPTION)
            .withSolutions(new SolutionBuilder().withLink("http://www.yahoo.com").withRemark("street").build())
            .withFrequency(COMMON_ISSUE_FREQUENCY)
            .build();

    // Manually added
    public static final Issue MYSQL_ERROR = new IssueBuilder()
            .withStatement(MYSQL_ERROR_STATEMENT)
            .withDescription(MYSQL_ERROR_DESCRIPTION)
            .withSolutions(new SolutionBuilder().withLink("https://stackoverflow.com/").withRemark("india").build())
            .withFrequency(COMMON_ISSUE_FREQUENCY)
            .withLastModifiedTime(COMMON_ISSUE_TIME + 60).build();
    public static final Issue POSTGRESQL_ERROR = new IssueBuilder()
            .withStatement(POSTGRESQL_ERROR_STATEMENT)
            .withDescription(POSTGRESQL_ERROR_DESCRIPTION)
            .withSolutions(new SolutionBuilder().withLink("http://www.baidu.com").withRemark("ave").build())
            .withFrequency(COMMON_ISSUE_FREQUENCY)
            .build();

    // Manually added - Issue's details found in {@code CommandTestUtil}
    public static final Issue VALID_JAVA_ISSUE = new IssueBuilder()
            .withStatement(VALID_STATEMENT_JAVA)
            .withDescription(VALID_DESCRIPTION_JAVA)
            .withSolutions(SOLUTION_JAVA)
            .withTags(VALID_TAG_UI)
            .withFrequency(COMMON_ISSUE_FREQUENCY)
            .withLastModifiedTime(COMMON_ISSUE_TIME + 60).build();
    public static final Issue VALID_C_ISSUE = new IssueBuilder()
            .withStatement(VALID_STATEMENT_C)
            .withDescription(VALID_DESCRIPTION_C)
            .withSolutions(SOLUTION_C)
            .withTags(VALID_TAG_UI)
            .withFrequency(COMMON_ISSUE_FREQUENCY)
            .build();

    public static final Issue DUMMY_ISSUE = new IssueBuilder()
            .withStatement(DUMMY_STATEMENT)
            .withDescription(DUMMY_DESCRIPTION)
            .withSolutions(SOLUTION_C)
            .withTags(VALID_TAG_UI)
            .withFrequency(COMMON_ISSUE_FREQUENCY)
            .build();

    // A keyword that matches the MYSQL_ERROR_STATEMENT
    public static final String KEYWORD_MATCHING_MYSQL = MYSQL_ERROR_STATEMENT.split("\\s+")[0];

    private TypicalIssues() {
    } // prevents instantiation

    /**
     * Returns an {@code SaveIt} with all the typical issues.
     */
    public static SaveIt getTypicalSaveIt() {
        SaveIt ab = new SaveIt();
        for (Issue issue : getTypicalIssues()) {
            ab.addIssue(issue);
        }
        return ab;
    }

    public static List<Issue> getTypicalIssues() {
        return new ArrayList<>(Arrays.asList(JAVA_NULL_POINTER, C_SEGMENTATION_FAULT, RUBY_HASH_BUG,
                TRAVIS_BUILD, CHECKSTYLE_ERROR, QUICKSORT_BUG, C_RACE_CONDITION));
    }
}
