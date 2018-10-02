package systemtests;

import seedu.address.model.Model;
import seedu.address.model.issue.Issue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Contains helper methods to set up {@code Model} for testing.
 */
public class ModelHelper {

    private static final Predicate<Issue> PREDICATE_MATCHING_NO_PERSONS = unused -> false;

    /**
     * Updates {@code model}'s filtered list to display only {@code toDisplay}.
     */
    public static void setFilteredList(Model model, List<Issue> toDisplay) {
        Optional<Predicate<Issue>> predicate =
                toDisplay.stream().map(ModelHelper::getPredicateMatching).reduce(Predicate::or);
        model.updateFilteredPersonList(predicate.orElse(PREDICATE_MATCHING_NO_PERSONS));
    }

    /**
     * @see ModelHelper#setFilteredList(Model, List)
     */
    public static void setFilteredList(Model model, Issue... toDisplay) {
        setFilteredList(model, Arrays.asList(toDisplay));
    }

    /**
     * Returns a predicate that evaluates to true if this {@code Issue} equals to {@code other}.
     */
    private static Predicate<Issue> getPredicateMatching(Issue other) {
        return person -> person.equals(other);
    }
}
