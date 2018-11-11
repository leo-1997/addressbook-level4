package systemtests;

import static seedu.saveit.ui.testutil.GuiTestAssert.assertListMatching;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Ignore;
import org.junit.Test;

import seedu.saveit.model.Issue;
import seedu.saveit.model.SaveIt;
import seedu.saveit.model.util.SampleDataUtil;
import seedu.saveit.testutil.TestUtil;

public class SampleDataTest extends SaveItSystemTest {
    /**
     * Returns null to force test app to load data of the file in {@code getDataFileLocation()}.
     */
    @Override
    protected SaveIt getInitialData() {
        return null;
    }

    /**
     * Returns a non-existent file location to force test app to load sample data.
     */
    @Override
    protected Path getDataFileLocation() {
        Path filePath = TestUtil.getFilePathInSandboxFolder("SomeFileThatDoesNotExist1234567890.xml");
        deleteFileIfExists(filePath);
        return filePath;
    }

    /**
     * Deletes the file at {@code filePath} if it exists.
     */
    private void deleteFileIfExists(Path filePath) {
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException ioe) {
            throw new AssertionError(ioe);
        }
    }

    @Test
    @Ignore
    public void saveIt_dataFileDoesNotExist_loadSampleData() {
        Issue[] expectedList = SampleDataUtil.getSampleIssues();
        assertListMatching(getIssueListPanel(), expectedList);
    }
}
