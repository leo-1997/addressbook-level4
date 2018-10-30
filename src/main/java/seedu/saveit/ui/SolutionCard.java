package seedu.saveit.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.saveit.model.issue.Solution;

/**
 * An UI component that displays information of a {@code Issue}.
 */
public class SolutionCard extends UiPart<Region> {

    private static final String FXML = "SolutionListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX. As a
     * consequence, UI elements' variable names cannot be set to such keywords or an exception will be thrown
     * by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/saveit-level4/issues/336">The issue on SaveIt level 4</a>
     */

    public final Solution solution;

    @FXML
    private HBox cardPane;
    @FXML
    private Label link;
    @FXML
    private Label id;
    @FXML
    private Label remark;

    public SolutionCard(Solution solution, int displayedIndex) {
        super(FXML);
        this.solution = solution;
        id.setText("Solution " + String.valueOf(displayedIndex));
        link.setText(solution.getLink().getValue());
        remark.setText(solution.getRemark().toString());
    }


    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SolutionCard)) {
            return false;
        }

        // state check
        SolutionCard card = (SolutionCard) other;
        return id.getText().equals(card.id.getText())
                && solution.equals(card.solution);
    }
}