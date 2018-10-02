package seedu.address.model;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.issue.Issue;

/**
 * The API of the Model component.
 */
public interface Model {

    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<Issue> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Clears existing backing model and replaces with the provided new data.
     */
    void resetData(ReadOnlySaveIt newData);

    /**
     * Returns the SaveIt
     */
    ReadOnlySaveIt getSaveIt();

    /**
     * Returns true if a issue with the same identity as {@code issue} exists in the address book.
     */
    boolean hasPerson(Issue issue);

    /**
     * Deletes the given issue. The issue must exist in the address book.
     */
    void deletePerson(Issue target);

    /**
     * Adds the given issue. {@code issue} must not already exist in the address book.
     */
    void addPerson(Issue issue);

    /**
     * Replaces the given issue {@code target} with {@code editedIssue}. {@code target} must exist in the
     * address book. The issue identity of {@code editedIssue} must not be the same as another existing issue
     * in the address book.
     */
    void updatePerson(Issue target, Issue editedIssue);

    /**
     * Returns an unmodifiable view of the filtered issue list
     */
    ObservableList<Issue> getFilteredPersonList();

    /**
     * Updates the filter of the filtered issue list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Issue> predicate);

    /**
     * Returns true if the model has previous address book states to restore.
     */
    boolean canUndoSaveIt();

    /**
     * Returns true if the model has undone address book states to restore.
     */
    boolean canRedoSaveIt();

    /**
     * Restores the model's address book to its previous state.
     */
    void undoSaveIt();

    /**
     * Restores the model's address book to its previously undone state.
     */
    void redoSaveIt();

    /**
     * Saves the current address book state for undo/redo.
     */
    void commitSaveIt();
}
