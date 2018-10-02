package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.events.model.SaveItChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlySaveIt;
import seedu.address.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends SaveItStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    Path getSaveItFilePath();

    @Override
    Optional<ReadOnlySaveIt> readSaveIt() throws DataConversionException, IOException;

    @Override
    void saveSaveIt(ReadOnlySaveIt saveIt) throws IOException;

    /**
     * Saves the current version of the SaveIt to the hard disk. Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleSaveItChangedEvent(SaveItChangedEvent abce);
}
