package seedu.saveit.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_DESCRIPTION_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_STATEMENT_C;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_TAG_UI;
import static seedu.saveit.testutil.TypicalIssues.JAVA_NULL_POINTER;
import static seedu.saveit.testutil.TypicalIssues.VALID_C_ISSUE;
import static seedu.saveit.testutil.TypicalSolutions.SOLUTION_C;
import static seedu.saveit.testutil.TypicalSolutions.SOLUTION_STACKOVERFLOW;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.saveit.testutil.IssueBuilder;

public class IssueTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Issue issue = new IssueBuilder().build();
        thrown.expect(UnsupportedOperationException.class);
        issue.getTags().remove(0);
    }

    @Test
    public void isSameIssue() {
        // same object -> returns true
        assertTrue(JAVA_NULL_POINTER.isSameIssue(JAVA_NULL_POINTER));

        // null -> returns false
        assertFalse(JAVA_NULL_POINTER.isSameIssue(null));

        // different description -> returns false
        Issue editedAlice = new IssueBuilder(JAVA_NULL_POINTER).withDescription(VALID_DESCRIPTION_JAVA).build();
        assertTrue(JAVA_NULL_POINTER.isSameIssue(editedAlice));

        // different statement -> returns false
        editedAlice = new IssueBuilder(JAVA_NULL_POINTER).withStatement(VALID_STATEMENT_C).build();
        assertFalse(JAVA_NULL_POINTER.isSameIssue(editedAlice));

        // same name, same description, different attributes -> returns true
        editedAlice = new IssueBuilder(JAVA_NULL_POINTER)
                .withSolutions(SOLUTION_STACKOVERFLOW)
                .withTags(VALID_TAG_UI).build();
        assertTrue(JAVA_NULL_POINTER.isSameIssue(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Issue aliceCopy = new IssueBuilder(JAVA_NULL_POINTER).build();
        assertTrue(JAVA_NULL_POINTER.equals(aliceCopy));

        // same object -> returns true
        assertTrue(JAVA_NULL_POINTER.equals(JAVA_NULL_POINTER));

        // different description, same issue statement -> returns true
        Issue editedAlice = new IssueBuilder(JAVA_NULL_POINTER).withDescription(VALID_DESCRIPTION_JAVA).build();
        assertTrue(JAVA_NULL_POINTER.equals(editedAlice));

        // different solutions, same issue statement -> returns true
        editedAlice = new IssueBuilder(JAVA_NULL_POINTER).withSolutions(SOLUTION_C).build();
        assertTrue(JAVA_NULL_POINTER.equals(editedAlice));

        // different tags, same issue statement -> returns true
        editedAlice = new IssueBuilder(JAVA_NULL_POINTER).withTags(VALID_TAG_UI).build();
        assertTrue(JAVA_NULL_POINTER.equals(editedAlice));
        // null -> returns false
        assertFalse(JAVA_NULL_POINTER.equals(null));

        // different type -> returns false
        assertFalse(JAVA_NULL_POINTER.equals(5));

        // different issue -> returns false
        assertFalse(JAVA_NULL_POINTER.equals(VALID_C_ISSUE));

        // different statement -> returns false
        editedAlice = new IssueBuilder(JAVA_NULL_POINTER).withStatement(VALID_STATEMENT_C).build();
        assertFalse(JAVA_NULL_POINTER.equals(editedAlice));

    }
}
