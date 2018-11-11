package guitests.guihandles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.ListView;
import seedu.saveit.model.issue.Solution;

/**
 * Provides a handle for {@code IssueListPanel} containing the list of {@code IssueCard}.
 */
public class SolutionListPanelHandle extends NodeHandle<ListView<Solution>> {
    public static final String SOLUTION_LIST_VIEW_ID = "#solutionListView";

    private static final String CARD_PANE_ID = "#cardPane";

    private Optional<Solution> lastRememberedSelectedSolutionCard;

    public SolutionListPanelHandle(ListView<Solution> solutionListPanelNode) {
        super(solutionListPanelNode);
    }

    /**
     * Returns a handle to the selected {@code SolutionCardHandle}.
     * A maximum of 1 item can be selected at any time.
     * @throws AssertionError if no card is selected, or more than 1 card is selected.
     * @throws IllegalStateException if the selected card is currently not in the scene graph.
     */
    public SolutionCardHandle getHandleToSelectedCard() {
        List<Solution> selectedSolutionList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedSolutionList.size() != 1) {
            throw new AssertionError("Solution list size expected 1.");
        }

        return getAllCardNodes().stream()
                .map(SolutionCardHandle::new)
                .filter(handle -> handle.equals(selectedSolutionList.get(0)))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    /**
     * Returns the index of the selected card.
     */
    public int getSelectedCardIndex() {
        return getRootNode().getSelectionModel().getSelectedIndex();
    }

    /**
     * Returns true if a card is currently selected.
     */
    public boolean isAnyCardSelected() {
        List<Solution> selectedCardsList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedCardsList.size() > 1) {
            throw new AssertionError("Card list size expected 0 or 1.");
        }

        return !selectedCardsList.isEmpty();
    }

    /**
     * Navigates the listview to display {@code solution}.
     */
    public void navigateToCard(Solution solution) {
        if (!getRootNode().getItems().contains(solution)) {
            throw new IllegalArgumentException("Solution does not exist.");
        }

        guiRobot.interact(() -> {
            getRootNode().scrollTo(solution);
        });
        guiRobot.pauseForHuman();
    }

    /**
     * Navigates the listview to {@code index}.
     */
    public void navigateToCard(int index) {
        if (index < 0 || index >= getRootNode().getItems().size()) {
            throw new IllegalArgumentException("Index is out of bounds.");
        }

        guiRobot.interact(() -> {
            getRootNode().scrollTo(index);
        });
        guiRobot.pauseForHuman();
    }

    /**
     * Selects the {@code IssueCard} at {@code index} in the list.
     */
    public void select(int index) {
        getRootNode().getSelectionModel().select(index);
    }

    /**
     * Returns the solution card handle of a solution associated with the {@code index} in the list.
     * @throws IllegalStateException if the selected card is currently not in the scene graph.
     */
    public SolutionCardHandle getSolutionCardHandle(int index) {
        return getAllCardNodes().stream()
                .map(SolutionCardHandle::new)
                .filter(handle -> handle.equals(getSolution(index)))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private Solution getSolution(int index) {
        return getRootNode().getItems().get(index);
    }

    /**
     * Returns all card nodes in the scene graph.
     * Card nodes that are visible in the listview are definitely in the scene graph, while some nodes that are not
     * visible in the listview may also be in the scene graph.
     */
    private Set<Node> getAllCardNodes() {
        return guiRobot.lookup(CARD_PANE_ID).queryAll();
    }

    /**
     * Remembers the selected {@code SolutionCard} in the list.
     */
    public void rememberSelectedSolutionCard() {
        List<Solution> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            lastRememberedSelectedSolutionCard = Optional.empty();
        } else {
            lastRememberedSelectedSolutionCard = Optional.of(selectedItems.get(0));
        }
    }

    /**
     * Returns true if the selected {@code SolutionCard} is different from the value remembered by the most recent
     * {@code rememberSelectedSolutionCard()} call.
     */
    public boolean isSelectedSolutionCardChanged() {
        List<Solution> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            return lastRememberedSelectedSolutionCard.isPresent();
        } else {
            return !lastRememberedSelectedSolutionCard.isPresent()
                    || !lastRememberedSelectedSolutionCard.get().equals(selectedItems.get(0));
        }
    }

    /**
     * Returns the size of the list.
     */
    public int getListSize() {
        return getRootNode().getItems().size();
    }
}
