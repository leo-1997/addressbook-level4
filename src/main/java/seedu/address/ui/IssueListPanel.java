package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.IssuePanelSelectionChangedEvent;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.model.Issue;

/**
 * Panel containing the list of persons.
 */
public class IssueListPanel extends UiPart<Region> {
    private static final String FXML = "IssueListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(IssueListPanel.class);

    @FXML
    private ListView<Issue> issueListView;

    public IssueListPanel(ObservableList<Issue> issueList) {
        super(FXML);
        setConnections(issueList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Issue> issueList) {
        issueListView.setItems(issueList);
        issueListView.setCellFactory(listView -> new IssueListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        issueListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in issue list panel changed to : '" + newValue + "'");
                        raise(new IssuePanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code PersonCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            issueListView.scrollTo(index);
            issueListView.getSelectionModel().clearAndSelect(index);

        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Issue} using a {@code PersonCard}.
     */
    class IssueListViewCell extends ListCell<Issue> {
        @Override
        protected void updateItem(Issue issue, boolean empty) {
            super.updateItem(issue, empty);

            if (empty || issue == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new IssueCard(issue, getIndex() + 1).getRoot());
            }
        }
    }

}
