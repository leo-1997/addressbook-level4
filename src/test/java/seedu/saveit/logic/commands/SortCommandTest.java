package seedu.saveit.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static seedu.saveit.logic.commands.CommandTestUtil.C_RACE_CONDITION_STATEMENT;
import static seedu.saveit.logic.commands.CommandTestUtil.C_SEGMENTATION_FAULT_STATEMENT;
import static seedu.saveit.logic.commands.CommandTestUtil.JAVA_NULL_POINTER_STATEMENT;
import static seedu.saveit.logic.commands.CommandTestUtil.TRAVIS_BUILD_STATEMENT;
import static seedu.saveit.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.saveit.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_FIRST_ISSUE;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_FIRST_SOLUTION;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_THIRD_ISSUE;
import static seedu.saveit.testutil.TypicalIssues.CHECKSTYLE_ERROR;
import static seedu.saveit.testutil.TypicalIssues.C_RACE_CONDITION;
import static seedu.saveit.testutil.TypicalIssues.C_SEGMENTATION_FAULT;
import static seedu.saveit.testutil.TypicalIssues.JAVA_NULL_POINTER;
import static seedu.saveit.testutil.TypicalIssues.QUICKSORT_BUG;
import static seedu.saveit.testutil.TypicalIssues.RUBY_HASH_BUG;
import static seedu.saveit.testutil.TypicalIssues.TRAVIS_BUILD;
import static seedu.saveit.testutil.TypicalIssues.getTypicalSaveIt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import seedu.saveit.commons.core.Messages;
import seedu.saveit.logic.CommandHistory;
import seedu.saveit.model.Issue;
import seedu.saveit.model.Model;
import seedu.saveit.model.ModelManager;
import seedu.saveit.model.UserPrefs;
import seedu.saveit.model.issue.Description;
import seedu.saveit.model.issue.IssueContainsKeywordsPredicate;
import seedu.saveit.model.issue.IssueStatement;
import seedu.saveit.model.issue.SortType;
import seedu.saveit.testutil.DirectoryBuilder;

public class SortCommandTest {
    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalSaveIt(), new UserPrefs());
        expectedModel = new ModelManager(model.getSaveIt(), new UserPrefs());
    }

    @Test
    public void execute_sortIsNotFiltered_success() {
        SortType sortType = prepareIssueSort(SortType.TAG_SORT);
        String expectedMessage = String.format(SortCommand.MESSAGE_SUCCESS, sortType.getSortType());
        expectedModel.updateFilteredAndSortedIssueList(sortType.getComparator());
        SortCommand command = new SortCommand(sortType);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(
                Arrays.asList(C_SEGMENTATION_FAULT, JAVA_NULL_POINTER, TRAVIS_BUILD, RUBY_HASH_BUG,
                        CHECKSTYLE_ERROR, QUICKSORT_BUG, C_RACE_CONDITION),
                model.getFilteredAndSortedIssueList());

    }

    @Test
    public void execute_sortIsFiltered_success() {
        String[] keywordArray = {
                JAVA_NULL_POINTER_STATEMENT.split("\\s+")[0],
                C_SEGMENTATION_FAULT_STATEMENT.split("\\s+")[0],
                TRAVIS_BUILD_STATEMENT.split("\\s+")[0],
                C_RACE_CONDITION_STATEMENT.split("\\s+")[0]
        };
        // Filtered the issue list to the above 4 issues
        filterIssueList(keywordArray);

        SortType sortType = prepareIssueSort(SortType.TAG_SORT);
        String expectedMessage = String.format(SortCommand.MESSAGE_SUCCESS, sortType.getSortType());
        expectedModel.updateFilteredAndSortedIssueList(sortType.getComparator());
        SortCommand command = new SortCommand(sortType);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(C_SEGMENTATION_FAULT, JAVA_NULL_POINTER, TRAVIS_BUILD, C_RACE_CONDITION),
                model.getFilteredAndSortedIssueList());
    }

    @Test
    public void execute_sortAfterUpdate_success() {
        SortType sortType = prepareIssueSort(SortType.TAG_SORT);
        String expectedMessage = String.format(SortCommand.MESSAGE_SUCCESS, sortType.getSortType());
        Issue issue = new Issue(new IssueStatement("new SOLUTION_C++ problem"),
                new Description("only for test"), new ArrayList<>(), new HashSet<>());

        expectedModel.addIssue(issue);
        expectedModel.updateFilteredAndSortedIssueList(sortType.getComparator());
        model.addIssue(issue);
        SortCommand command = new SortCommand(sortType);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(C_SEGMENTATION_FAULT, JAVA_NULL_POINTER, TRAVIS_BUILD, RUBY_HASH_BUG,
                CHECKSTYLE_ERROR, QUICKSORT_BUG, C_RACE_CONDITION, issue),
                model.getFilteredAndSortedIssueList());
    }

    @Test
    public void execute_notUnderRootLevel_failure() {
        model.resetDirectory(new DirectoryBuilder().withIssueIndex(INDEX_FIRST_ISSUE).build());
        SortType sortType = prepareIssueSort(SortType.TAG_SORT);
        SortCommand command = new SortCommand(sortType);
        assertCommandFailure(command, model, commandHistory, Messages.MESSAGE_WRONG_DIRECTORY);

        model.resetDirectory(new DirectoryBuilder().withIssueIndex(INDEX_THIRD_ISSUE)

                .withSolutionIndex(INDEX_FIRST_SOLUTION).build());
        assertCommandFailure(command, model, commandHistory, Messages.MESSAGE_WRONG_DIRECTORY);
    }

    /**
     * Parses {@code userInput} into a {@code Comparator<Issue>}.
     */
    private SortType prepareIssueSort(String userInput) {
        return new SortType(userInput);
    }

    /**
     * Filter issue lists in both model and expectedModel by the provided keywords.
     * @param keywordArray
     */
    private void filterIssueList(String[] keywordArray) {
        List<String> keywords = Arrays.asList(keywordArray);
        IssueContainsKeywordsPredicate predicate = new IssueContainsKeywordsPredicate(keywords);
        model.updateFilteredIssueList(predicate);
        expectedModel.updateFilteredIssueList(predicate);
    }
}
