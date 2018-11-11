package seedu.saveit.ui;

import static org.junit.Assert.assertEquals;
import static seedu.saveit.testutil.EventsUtil.postNow;
import static seedu.saveit.testutil.TypicalComparators.FREQUENCY_COMPARATOR;
import static seedu.saveit.testutil.TypicalDirectories.ISSUE_LEVEL;
import static seedu.saveit.testutil.TypicalDirectories.ROOT_LEVEL;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.ResultDisplayHandle;
import seedu.saveit.commons.events.model.DirectoryChangedEvent;
import seedu.saveit.commons.events.model.SortTypeChangedEvent;
import seedu.saveit.commons.events.ui.NewResultAvailableEvent;


public class ResultDisplayTest extends GuiUnitTest {

    private static final NewResultAvailableEvent NEW_RESULT_EVENT_STUB = new NewResultAvailableEvent("Stub");
    private static final DirectoryChangedEvent DIRECTORY_CHANGED_ROOT_EVENT_STUB =
            new DirectoryChangedEvent(ROOT_LEVEL);
    private static final DirectoryChangedEvent DIRECTORY_CHANGED_ISSUE_EVENT_STUB =
            new DirectoryChangedEvent(ISSUE_LEVEL);
    private static final SortTypeChangedEvent SORT_TYPE_CHANGED_EVENT_STUB =
            new SortTypeChangedEvent(FREQUENCY_COMPARATOR);
    private static final String PREFIX = "Sorted By: ";
    private ResultDisplayHandle resultDisplayHandle;

    @Before
    public void setUp() {
        ResultDisplay resultDisplay = new ResultDisplay();
        uiPartRule.setUiPart(resultDisplay);

        resultDisplayHandle = new ResultDisplayHandle(resultDisplay.getRoot());
    }

    @Test
    public void display() {
        // default result text
        guiRobot.pauseForHuman();
        assertEquals("", resultDisplayHandle.getText());

        // new result received
        postNow(NEW_RESULT_EVENT_STUB);
        guiRobot.pauseForHuman();
        assertEquals(NEW_RESULT_EVENT_STUB.message, resultDisplayHandle.getText());

        //directory changed to root
        postNow(DIRECTORY_CHANGED_ROOT_EVENT_STUB);
        guiRobot.pauseForHuman();;
        assertEquals(ROOT_LEVEL.toString(), resultDisplayHandle.getDirectory());

        //directory changed to issue
        postNow(DIRECTORY_CHANGED_ISSUE_EVENT_STUB);
        guiRobot.pauseForHuman();
        assertEquals(ISSUE_LEVEL.toString(), resultDisplayHandle.getDirectory());

        //sortType changed
        postNow(SORT_TYPE_CHANGED_EVENT_STUB);
        guiRobot.pauseForHuman();
        assertEquals(PREFIX + FREQUENCY_COMPARATOR.toString(), resultDisplayHandle.getSortType());
    }
}
