package seedu.saveit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.saveit.testutil.TypicalIssues.RUBY_HASH_BUG;
import static seedu.saveit.testutil.TypicalIssues.VALID_C_ISSUE;
import static seedu.saveit.testutil.TypicalIssues.VALID_JAVA_ISSUE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.saveit.testutil.SaveItBuilder;

public class VersionedSaveItTest {

    private final ReadOnlySaveIt saveItWithAmy = new SaveItBuilder().withIssue(VALID_JAVA_ISSUE).build();
    private final ReadOnlySaveIt saveItWithBob = new SaveItBuilder().withIssue(VALID_C_ISSUE).build();
    private final ReadOnlySaveIt saveItWithCarl = new SaveItBuilder().withIssue(RUBY_HASH_BUG).build();
    private final ReadOnlySaveIt emptySaveIt = new SaveItBuilder().build();

    @Test
    public void commit_singleSaveIt_noStatesRemovedCurrentStateSaved() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(emptySaveIt);

        versionedSaveIt.commit();
        assertSaveItListStatus(versionedSaveIt,
                Collections.singletonList(emptySaveIt),
                emptySaveIt,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleSaveItPointerAtEndOfStateList_noStatesRemovedCurrentStateSaved() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(
                emptySaveIt, saveItWithAmy, saveItWithBob);

        versionedSaveIt.commit();
        assertSaveItListStatus(versionedSaveIt,
                Arrays.asList(emptySaveIt, saveItWithAmy, saveItWithBob),
                saveItWithBob,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleSaveItPointerNotAtEndOfStateList_statesAfterPointerRemovedCurrentStateSaved() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(
                emptySaveIt, saveItWithAmy, saveItWithBob);
        shiftCurrentStatePointerLeftwards(versionedSaveIt, 2);

        versionedSaveIt.commit();
        assertSaveItListStatus(versionedSaveIt,
                Collections.singletonList(emptySaveIt),
                emptySaveIt,
                Collections.emptyList());
    }

    @Test
    public void canUndo_multipleSaveItPointerAtEndOfStateList_returnsTrue() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(
                emptySaveIt, saveItWithAmy, saveItWithBob);

        assertTrue(versionedSaveIt.canUndo());
    }

    @Test
    public void canUndo_multipleSaveItPointerAtStartOfStateList_returnsTrue() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(
                emptySaveIt, saveItWithAmy, saveItWithBob);
        shiftCurrentStatePointerLeftwards(versionedSaveIt, 1);

        assertTrue(versionedSaveIt.canUndo());
    }

    @Test
    public void canUndo_singleSaveIt_returnsFalse() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(emptySaveIt);

        assertFalse(versionedSaveIt.canUndo());
    }

    @Test
    public void canUndo_multipleSaveItPointerAtStartOfStateList_returnsFalse() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(
                emptySaveIt, saveItWithAmy, saveItWithBob);
        shiftCurrentStatePointerLeftwards(versionedSaveIt, 2);

        assertFalse(versionedSaveIt.canUndo());
    }

    @Test
    public void canRedo_multipleSaveItPointerNotAtEndOfStateList_returnsTrue() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(
                emptySaveIt, saveItWithAmy, saveItWithBob);
        shiftCurrentStatePointerLeftwards(versionedSaveIt, 1);

        assertTrue(versionedSaveIt.canRedo());
    }

    @Test
    public void canRedo_multipleSaveItPointerAtStartOfStateList_returnsTrue() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(
                emptySaveIt, saveItWithAmy, saveItWithBob);
        shiftCurrentStatePointerLeftwards(versionedSaveIt, 2);

        assertTrue(versionedSaveIt.canRedo());
    }

    @Test
    public void canRedo_singleSaveIt_returnsFalse() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(emptySaveIt);

        assertFalse(versionedSaveIt.canRedo());
    }

    @Test
    public void canRedo_multipleSaveItPointerAtEndOfStateList_returnsFalse() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(
                emptySaveIt, saveItWithAmy, saveItWithBob);

        assertFalse(versionedSaveIt.canRedo());
    }

    @Test
    public void undo_multipleSaveItPointerAtEndOfStateList_success() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(
                emptySaveIt, saveItWithAmy, saveItWithBob);

        versionedSaveIt.undo();
        assertSaveItListStatus(versionedSaveIt,
                Collections.singletonList(emptySaveIt),
                saveItWithAmy,
                Collections.singletonList(saveItWithBob));
    }

    @Test
    public void undo_multipleSaveItPointerNotAtStartOfStateList_success() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(
                emptySaveIt, saveItWithAmy, saveItWithBob);
        shiftCurrentStatePointerLeftwards(versionedSaveIt, 1);

        versionedSaveIt.undo();
        assertSaveItListStatus(versionedSaveIt,
                Collections.emptyList(),
                emptySaveIt,
                Arrays.asList(saveItWithAmy, saveItWithBob));
    }

    @Test
    public void undo_singleSaveIt_throwsNoUndoableStateException() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(emptySaveIt);

        assertThrows(VersionedSaveIt.NoUndoableStateException.class, versionedSaveIt::undo);
    }

    @Test
    public void undo_multipleSaveItPointerAtStartOfStateList_throwsNoUndoableStateException() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(
                emptySaveIt, saveItWithAmy, saveItWithBob);
        shiftCurrentStatePointerLeftwards(versionedSaveIt, 2);

        assertThrows(VersionedSaveIt.NoUndoableStateException.class, versionedSaveIt::undo);
    }

    @Test
    public void redo_multipleSaveItPointerNotAtEndOfStateList_success() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(
                emptySaveIt, saveItWithAmy, saveItWithBob);
        shiftCurrentStatePointerLeftwards(versionedSaveIt, 1);

        versionedSaveIt.redo();
        assertSaveItListStatus(versionedSaveIt,
                Arrays.asList(emptySaveIt, saveItWithAmy),
                saveItWithBob,
                Collections.emptyList());
    }

    @Test
    public void redo_multipleSaveItPointerAtStartOfStateList_success() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(
                emptySaveIt, saveItWithAmy, saveItWithBob);
        shiftCurrentStatePointerLeftwards(versionedSaveIt, 2);

        versionedSaveIt.redo();
        assertSaveItListStatus(versionedSaveIt,
                Collections.singletonList(emptySaveIt),
                saveItWithAmy,
                Collections.singletonList(saveItWithBob));
    }

    @Test
    public void redo_singleSaveIt_throwsNoRedoableStateException() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(emptySaveIt);

        assertThrows(VersionedSaveIt.NoRedoableStateException.class, versionedSaveIt::redo);
    }

    @Test
    public void redo_multipleSaveItPointerAtEndOfStateList_throwsNoRedoableStateException() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(
                emptySaveIt, saveItWithAmy, saveItWithBob);

        assertThrows(VersionedSaveIt.NoRedoableStateException.class, versionedSaveIt::redo);
    }

    @Test
    public void equals() {
        VersionedSaveIt versionedSaveIt = prepareSaveItList(saveItWithAmy, saveItWithBob);

        // same values -> returns true
        VersionedSaveIt copy = prepareSaveItList(saveItWithAmy, saveItWithBob);
        assertTrue(versionedSaveIt.equals(copy));

        // same object -> returns true
        assertTrue(versionedSaveIt.equals(versionedSaveIt));

        // null -> returns false
        assertFalse(versionedSaveIt.equals(null));

        // different types -> returns false
        assertFalse(versionedSaveIt.equals(1));

        // different state list -> returns false
        VersionedSaveIt differentSaveItList = prepareSaveItList(saveItWithBob, saveItWithCarl);
        assertFalse(versionedSaveIt.equals(differentSaveItList));

        // different current pointer index -> returns false
        VersionedSaveIt differentCurrentStatePointer = prepareSaveItList(
                saveItWithAmy, saveItWithBob);
        shiftCurrentStatePointerLeftwards(versionedSaveIt, 1);
        assertFalse(versionedSaveIt.equals(differentCurrentStatePointer));
    }

    /**
     * Asserts that {@code versionedSaveIt} is currently pointing at {@code expectedCurrentState},
     * states before {@code versionedSaveIt#currentStatePointer} is equal to {@code expectedStatesBeforePointer},
     * and states after {@code versionedSaveIt#currentStatePointer} is equal to {@code expectedStatesAfterPointer}.
     */
    private void assertSaveItListStatus(VersionedSaveIt versionedSaveIt,
                                             List<ReadOnlySaveIt> expectedStatesBeforePointer,
                                             ReadOnlySaveIt expectedCurrentState,
                                             List<ReadOnlySaveIt> expectedStatesAfterPointer) {
        // check state currently pointing at is correct
        assertEquals(new SaveIt(versionedSaveIt), expectedCurrentState);

        // shift pointer to start of state list
        while (versionedSaveIt.canUndo()) {
            versionedSaveIt.undo();
        }

        // check states before pointer are correct
        for (ReadOnlySaveIt expectedSaveIt : expectedStatesBeforePointer) {
            assertEquals(expectedSaveIt, new SaveIt(versionedSaveIt));
            versionedSaveIt.redo();
        }

        // check states after pointer are correct
        for (ReadOnlySaveIt expectedSaveIt : expectedStatesAfterPointer) {
            versionedSaveIt.redo();
            assertEquals(expectedSaveIt, new SaveIt(versionedSaveIt));
        }

        // check that there are no more states after pointer
        assertFalse(versionedSaveIt.canRedo());

        // revert pointer to original position
        expectedStatesAfterPointer.forEach(unused -> versionedSaveIt.undo());
    }

    /**
     * Creates and returns a {@code VersionedSaveIt} with the {@code saveItStates} added into it, and the
     * {@code VersionedSaveIt#currentStatePointer} at the end of list.
     */
    private VersionedSaveIt prepareSaveItList(ReadOnlySaveIt... saveItStates) {
        assertFalse(saveItStates.length == 0);

        VersionedSaveIt versionedSaveIt = new VersionedSaveIt(saveItStates[0]);
        for (int i = 1; i < saveItStates.length; i++) {
            versionedSaveIt.resetData(saveItStates[i]);
            versionedSaveIt.commit();
        }

        return versionedSaveIt;
    }

    /**
     * Shifts the {@code versionedSaveIt#currentStatePointer} by {@code count} to the left of its list.
     */
    private void shiftCurrentStatePointerLeftwards(VersionedSaveIt versionedSaveIt, int count) {
        for (int i = 0; i < count; i++) {
            versionedSaveIt.undo();
        }
    }
}
