package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.issue.Issue;
import seedu.address.model.issue.UniquePersonList;

/**
 * Wraps all data at the address-book level Duplicates are not allowed (by .isSameIssue comparison)
 */
public class SaveIt implements ReadOnlySaveIt {

    private final UniquePersonList persons;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid
     * duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */ {
        persons = new UniquePersonList();
    }

    public SaveIt() {
    }

    /**
     * Creates an SaveIt using the Persons in the {@code toBeCopied}
     */
    public SaveIt(ReadOnlySaveIt toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the issue list with {@code issues}. {@code issues} must not contain duplicate
     * issues.
     */
    public void setPersons(List<Issue> issues) {
        this.persons.setPersons(issues);
    }

    /**
     * Resets the existing data of this {@code SaveIt} with {@code newData}.
     */
    public void resetData(ReadOnlySaveIt newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
    }

    //// issue-level operations

    /**
     * Returns true if a issue with the same identity as {@code issue} exists in the address book.
     */
    public boolean hasPerson(Issue issue) {
        requireNonNull(issue);
        return persons.contains(issue);
    }

    /**
     * Adds a issue to the address book. The issue must not already exist in the address book.
     */
    public void addPerson(Issue p) {
        persons.add(p);
    }

    /**
     * Replaces the given issue {@code target} in the list with {@code editedIssue}. {@code target} must exist
     * in the address book. The issue identity of {@code editedIssue} must not be the same as another existing
     * issue in the address book.
     */
    public void updatePerson(Issue target, Issue editedIssue) {
        requireNonNull(editedIssue);

        persons.setPerson(target, editedIssue);
    }

    /**
     * Removes {@code key} from this {@code SaveIt}. {@code key} must exist in the address book.
     */
    public void removePerson(Issue key) {
        persons.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asUnmodifiableObservableList().size() + " persons";
        // TODO: refine later
    }

    @Override
    public ObservableList<Issue> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SaveIt // instanceof handles nulls
                && persons.equals(((SaveIt) other).persons));
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
