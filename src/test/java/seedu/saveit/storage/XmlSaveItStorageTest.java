package seedu.saveit.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.saveit.testutil.TypicalIssues.JAVA_NULL_POINTER;
import static seedu.saveit.testutil.TypicalIssues.MYSQL_ERROR;
import static seedu.saveit.testutil.TypicalIssues.POSTGRESQL_ERROR;
import static seedu.saveit.testutil.TypicalIssues.getTypicalSaveIt;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.saveit.commons.exceptions.DataConversionException;
import seedu.saveit.model.ReadOnlySaveIt;
import seedu.saveit.model.SaveIt;

public class XmlSaveItStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "XmlSaveItStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readSaveIt_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readSaveIt(null);
    }

    private java.util.Optional<ReadOnlySaveIt> readSaveIt(String filePath) throws Exception {
        return new XmlSaveItStorage(Paths.get(filePath)).readSaveIt(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readSaveIt("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readSaveIt("NotXmlFormatSaveIt.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readSaveIt_invalidAndValidIssueSaveIt_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readSaveIt("invalidAndValidIssueSaveIt.xml");
    }

    @Test
    public void readAndSaveSaveIt_allInOrder_success() throws Exception {
        Path filePath = testFolder.getRoot().toPath().resolve("TempSaveIt.xml");
        SaveIt original = getTypicalSaveIt();
        XmlSaveItStorage xmlSaveItStorage = new XmlSaveItStorage(filePath);

        //Save in new file and read back
        xmlSaveItStorage.saveSaveIt(original, filePath);
        ReadOnlySaveIt readBack = xmlSaveItStorage.readSaveIt(filePath).get();
        assertEquals(original, new SaveIt(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addIssue(MYSQL_ERROR);
        original.removeIssue(JAVA_NULL_POINTER);
        xmlSaveItStorage.saveSaveIt(original, filePath);
        readBack = xmlSaveItStorage.readSaveIt(filePath).get();
        assertEquals(original, new SaveIt(readBack));

        //Save and read without specifying file path
        original.addIssue(POSTGRESQL_ERROR);
        xmlSaveItStorage.saveSaveIt(original); //file path not specified
        readBack = xmlSaveItStorage.readSaveIt().get(); //file path not specified
        assertEquals(original, new SaveIt(readBack));

    }

    @Test
    public void saveSaveIt_nullSaveIt_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveSaveIt(null, "SomeFile.xml");
    }

    /**
     * Saves {@code saveIt} at the specified {@code filePath}.
     */
    private void saveSaveIt(ReadOnlySaveIt saveIt, String filePath) {
        try {
            new XmlSaveItStorage(Paths.get(filePath))
                    .saveSaveIt(saveIt, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveSaveIt_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveSaveIt(new SaveIt(), null);
    }


}
