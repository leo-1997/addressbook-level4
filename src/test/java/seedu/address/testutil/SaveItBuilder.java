package seedu.address.testutil;

import seedu.address.model.SaveIt;
import seedu.address.model.issue.Issue;

/**
 * A utility class to help with building address objects. Example usage: <br> {@code SaveIt ab = new
 * SaveItBuilder().withPerson("John", "Doe").build();}
 */
public class SaveItBuilder {

    private SaveIt saveIt;

    public SaveItBuilder() {
        saveIt = new SaveIt();
    }

    public SaveItBuilder(SaveIt saveIt) {
        this.saveIt = saveIt;
    }

    /**
     * Adds a new {@code Issue} to the {@code SaveIt} that we are building.
     */
    public SaveItBuilder withPerson(Issue issue) {
        saveIt.addPerson(issue);
        return this;
    }

    public SaveIt build() {
        return saveIt;
    }
}
