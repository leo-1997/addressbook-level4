package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.SaveItChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlySaveIt;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of SaveIt data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private SaveItStorage saveItStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(SaveItStorage saveItStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.saveItStorage = saveItStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }

    // ================ SaveIt methods ==============================

    @Override
    public Path getSaveItFilePath() {
        return saveItStorage.getSaveItFilePath();
    }

    @Override
    public Optional<ReadOnlySaveIt> readSaveIt() throws DataConversionException, IOException {
        return readSaveIt(saveItStorage.getSaveItFilePath());
    }

    @Override
    public Optional<ReadOnlySaveIt> readSaveIt(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return saveItStorage.readSaveIt(filePath);
    }

    @Override
    public void saveSaveIt(ReadOnlySaveIt saveIt) throws IOException {
        saveSaveIt(saveIt, saveItStorage.getSaveItFilePath());
    }

    @Override
    public void saveSaveIt(ReadOnlySaveIt saveIt, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        saveItStorage.saveSaveIt(saveIt, filePath);
    }


    @Override
    @Subscribe
    public void handleSaveItChangedEvent(SaveItChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveSaveIt(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
