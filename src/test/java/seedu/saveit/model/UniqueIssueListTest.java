package seedu.saveit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.saveit.logic.commands.CommandTestUtil.JAVA_NULL_POINTER_DESCRIPTION;
import static seedu.saveit.logic.commands.CommandTestUtil.JAVA_NULL_POINTER_STATEMENT;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_DESCRIPTION_C;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_TAG_SYNTAX;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_TAG_UI;
import static seedu.saveit.testutil.TypicalIssues.JAVA_NULL_POINTER;
import static seedu.saveit.testutil.TypicalIssues.VALID_C_ISSUE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.saveit.model.issue.exceptions.DuplicateIssueException;
import seedu.saveit.model.issue.exceptions.IssueNotFoundException;
import seedu.saveit.testutil.IssueBuilder;

public class UniqueIssueListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final UniqueIssueList uniqueIssueList = new UniqueIssueList();

    @Test
    public void contains_nullIssue_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueIssueList.contains(null);
    }

    @Test
    public void contains_issueNotInList_returnsFalse() {
        assertFalse(uniqueIssueList.contains(JAVA_NULL_POINTER));
    }

    @Test
    public void contains_issueInList_returnsTrue() {
        uniqueIssueList.add(JAVA_NULL_POINTER);
        assertTrue(uniqueIssueList.contains(JAVA_NULL_POINTER));
    }

    @Test
    public void contains_issueWithSameIdentityFieldsInList_returnsTrue() {
        uniqueIssueList.add(JAVA_NULL_POINTER);
        Issue editedJavaIssue = new IssueBuilder(JAVA_NULL_POINTER)
                .withStatement(JAVA_NULL_POINTER_STATEMENT)


                .withDescription(JAVA_NULL_POINTER_DESCRIPTION)
                .withTags(VALID_TAG_SYNTAX)
                .build();
        assertTrue(uniqueIssueList.contains(editedJavaIssue));
    }

    @Test
    public void add_nullIssue_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueIssueList.add(null);
    }

    @Test
    public void add_duplicateIssue_throwsDuplicateIssueException() {
        uniqueIssueList.add(JAVA_NULL_POINTER);
        thrown.expect(DuplicateIssueException.class);
        uniqueIssueList.add(JAVA_NULL_POINTER);
    }


    @Test
    public void setIssue_nullTargetIssue_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueIssueList.setIssue(null, JAVA_NULL_POINTER);
    }

    @Test
    public void setIssue_nullEditedIssue_throwsNullPointerException() {

        thrown.expect(NullPointerException.class);
        uniqueIssueList.setIssue(JAVA_NULL_POINTER, null);
    }

    @Test
    public void setIssue_targetIssueNotInList_throwsIssueNotFoundException() {
        thrown.expect(IssueNotFoundException.class);
        uniqueIssueList.setIssue(JAVA_NULL_POINTER, JAVA_NULL_POINTER);
    }

    @Test
    public void setIssue_editedIssueIsSameIssue_success() {
        uniqueIssueList.add(JAVA_NULL_POINTER);
        uniqueIssueList.setIssue(JAVA_NULL_POINTER, JAVA_NULL_POINTER);
        UniqueIssueList expectedUniqueIssueList = new UniqueIssueList();
        expectedUniqueIssueList.add(JAVA_NULL_POINTER);
        assertEquals(expectedUniqueIssueList, uniqueIssueList);
    }

    @Test
    public void setIssue_editedIssueHasSameIdentity_success() {
        uniqueIssueList.add(JAVA_NULL_POINTER);
        Issue editedAlice = new IssueBuilder(JAVA_NULL_POINTER)
                .withDescription(VALID_DESCRIPTION_C)
                .withTags(VALID_TAG_UI)
                .build();
        uniqueIssueList.setIssue(JAVA_NULL_POINTER, editedAlice);
        UniqueIssueList expectedUniqueIssueList = new UniqueIssueList();
        expectedUniqueIssueList.add(editedAlice);
        assertEquals(expectedUniqueIssueList, uniqueIssueList);
    }

    @Test
    public void setIssue_editedIssueHasDifferentIdentity_success() {
        uniqueIssueList.add(JAVA_NULL_POINTER);
        uniqueIssueList.setIssue(JAVA_NULL_POINTER, VALID_C_ISSUE);
        UniqueIssueList expectedUniqueIssueList = new UniqueIssueList();
        expectedUniqueIssueList.add(VALID_C_ISSUE);
        assertEquals(expectedUniqueIssueList, uniqueIssueList);
    }

    @Test
    public void setIssue_editedIssueHasNonUniqueIdentity_throwsDuplicateIssueException() {
        uniqueIssueList.add(JAVA_NULL_POINTER);
        uniqueIssueList.add(VALID_C_ISSUE);
        thrown.expect(DuplicateIssueException.class);
        uniqueIssueList.setIssue(JAVA_NULL_POINTER, VALID_C_ISSUE);
    }

    @Test
    public void remove_nullIssue_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueIssueList.remove(null);
    }

    @Test
    public void remove_issueDoesNotExist_throwsIssueNotFoundException() {
        thrown.expect(IssueNotFoundException.class);
        uniqueIssueList.remove(JAVA_NULL_POINTER);
    }

    @Test
    public void remove_existingIssue_removesIssue() {
        uniqueIssueList.add(JAVA_NULL_POINTER);
        uniqueIssueList.remove(JAVA_NULL_POINTER);
        UniqueIssueList expectedUniqueIssueList = new UniqueIssueList();
        assertEquals(expectedUniqueIssueList, uniqueIssueList);
    }

    @Test
    public void setIssues_nullUniqueIssueList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueIssueList.setIssues((UniqueIssueList) null);
    }

    @Test
    public void setIssues_uniqueIssueList_replacesOwnListWithProvidedUniqueIssueList() {
        uniqueIssueList.add(JAVA_NULL_POINTER);
        UniqueIssueList expectedUniqueIssueList = new UniqueIssueList();
        expectedUniqueIssueList.add(VALID_C_ISSUE);
        uniqueIssueList.setIssues(expectedUniqueIssueList);
        assertEquals(expectedUniqueIssueList, uniqueIssueList);
    }

    @Test
    public void setIssues_nullList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueIssueList.setIssues((List<Issue>) null);
    }

    @Test
    public void setIssues_list_replacesOwnListWithProvidedList() {
        uniqueIssueList.add(JAVA_NULL_POINTER);
        List<Issue> issueList = Collections.singletonList(VALID_C_ISSUE);
        uniqueIssueList.setIssues(issueList);
        UniqueIssueList expectedUniqueIssueList = new UniqueIssueList();
        expectedUniqueIssueList.add(VALID_C_ISSUE);
        assertEquals(expectedUniqueIssueList, uniqueIssueList);
    }

    @Test
    public void setIssues_listWithDuplicateIssues_throwsDuplicateIssueException() {
        List<Issue> listWithDuplicateIssues = Arrays.asList(JAVA_NULL_POINTER, JAVA_NULL_POINTER);
        thrown.expect(DuplicateIssueException.class);
        uniqueIssueList.setIssues(listWithDuplicateIssues);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        uniqueIssueList.asUnmodifiableObservableList().remove(0);
    }
}
