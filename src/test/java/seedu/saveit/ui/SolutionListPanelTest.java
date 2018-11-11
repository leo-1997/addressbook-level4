package seedu.saveit.ui;

import static java.time.Duration.ofMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static seedu.saveit.testutil.TypicalSolutions.getTypicalSolutions;
import static seedu.saveit.ui.testutil.GuiTestAssert.assertCardDisplaysSolution;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import guitests.guihandles.SolutionCardHandle;
import guitests.guihandles.SolutionListPanelHandle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.saveit.commons.util.FileUtil;
import seedu.saveit.commons.util.XmlUtil;
import seedu.saveit.model.issue.Solution;
import seedu.saveit.storage.XmlSerializableSaveIt;


public class SolutionListPanelTest extends GuiUnitTest {
    private static final ObservableList<Solution> TYPICAL_SOLUTIONS =
            FXCollections.observableList(getTypicalSolutions());

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "sandbox");

    private static final long CARD_CREATION_AND_DELETION_TIMEOUT = 2500;

    private SolutionListPanelHandle solutionListPanelHandle;

    @Test
    public void display() {
        initUi(TYPICAL_SOLUTIONS);

        for (int i = 0; i < TYPICAL_SOLUTIONS.size(); i++) {
            solutionListPanelHandle.navigateToCard(TYPICAL_SOLUTIONS.get(i));
            Solution expectedSolution = TYPICAL_SOLUTIONS.get(i);
            SolutionCardHandle actualCard = solutionListPanelHandle.getSolutionCardHandle(i);

            assertCardDisplaysSolution(expectedSolution, actualCard);
            assertEquals(Integer.toString(i + 1) + ". Solution", actualCard.getId());
        }
    }

    /**
     * Verifies that creating and deleting large number of solutions in {@code SolutionListPanel} requires lesser than
     * {@code CARD_CREATION_AND_DELETION_TIMEOUT} milliseconds to execute.
     */
    @Test
    public void performanceTest() throws Exception {
        ObservableList<Solution> backingList = createBackingList(10000);

        assertTimeoutPreemptively(ofMillis(CARD_CREATION_AND_DELETION_TIMEOUT), () -> {
            initUi(backingList);
            guiRobot.interact(backingList::clear);
        }, "Creation and deletion of solution cards exceeded time limit");
    }

    /**
     * Returns a list of solutions containing {@code solutionCount} solutions that is used to populate the
     * {@code SolutionListPanel}.
     */
    private ObservableList<Solution> createBackingList(int solutionCount) throws Exception {
        Path xmlFile = createXmlFileWithIssues(solutionCount);
        XmlSerializableSaveIt xmlSaveIt =
                XmlUtil.getDataFromFile(xmlFile, XmlSerializableSaveIt.class);
        return FXCollections.observableArrayList(xmlSaveIt.toModelType().getIssueList().get(0).getSolutions());
    }

    /**
     * Returns a .xml file containing an issue with {@code solutionCount} solutions.
     * This file will be deleted when the JVM terminates.
     */
    private Path createXmlFileWithIssues(int solutionCount) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        builder.append("<saveit>\n");
        builder.append("<issues>\n");
        builder.append("<statement>").append("a</statement>\n");
        builder.append("<description>000</description>\n");
        for (int i = 0; i < solutionCount; i++) {
            builder.append("<solutions>\n");
            builder.append("<solutionLink>https://www.example.com</solutionLink>\n");
            builder.append("<remark>remark</remark>\n");
            builder.append("<isPrimarySolution>false</isPrimarySolution>\n");
            builder.append("</solutions>\n");
        }
        builder.append("<frequency>0</frequency>\n");
        builder.append("<createdTime>1541846485039</createdTime>\n");
        builder.append("<lastModifiedTime>1541846485039</lastModifiedTime>\n");
        builder.append("</issues>\n");
        builder.append("</saveit>\n");

        Path manySolutionsFile = Paths.get(TEST_DATA_FOLDER + "manySolutions.xml");
        FileUtil.createFile(manySolutionsFile);
        FileUtil.writeToFile(manySolutionsFile, builder.toString());
        manySolutionsFile.toFile().deleteOnExit();
        return manySolutionsFile;
    }

    /**
     * Initializes {@code solutionListPanelHandle} with a {@code SolutionListPanel} backed by {@code backingList}.
     * Also shows the {@code Stage} that displays only {@code SolutionListPanel}.
     */
    private void initUi(ObservableList<Solution> backingList) {
        SolutionListPanel solutionListPanel = new SolutionListPanel(backingList);
        uiPartRule.setUiPart(solutionListPanel);

        solutionListPanelHandle = new SolutionListPanelHandle(getChildNode(solutionListPanel.getRoot(),
                SolutionListPanelHandle.SOLUTION_LIST_VIEW_ID));
    }
}
