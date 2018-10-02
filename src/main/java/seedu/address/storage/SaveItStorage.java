package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlySaveIt;
import seedu.address.model.SaveIt;

/**
 * Represents a storage for {@link SaveIt}.
 */
public interface SaveItStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getSaveItFilePath();

    /**
     * Returns SaveIt data as a {@link ReadOnlySaveIt}. Returns {@code Optional.empty()} if storage file is
     * not found.
     *
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlySaveIt> readSaveIt() throws DataConversionException, IOException;

    /**
     * @see #getSaveItFilePath()
     */
    Optional<ReadOnlySaveIt> readSaveIt(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlySaveIt} to the storage.
     *
     * @param saveIt cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveSaveIt(ReadOnlySaveIt saveIt) throws IOException;

    /**
     * @see #saveSaveIt(ReadOnlySaveIt)
     */
    void saveSaveIt(ReadOnlySaveIt saveIt, Path filePath) throws IOException;

}
