package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlySaveIt;

/**
 * A class to access SaveIt data stored as an xml file on the hard disk.
 */
public class XmlSaveItStorage implements SaveItStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlSaveItStorage.class);

    private Path filePath;

    public XmlSaveItStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getSaveItFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlySaveIt> readSaveIt() throws DataConversionException, IOException {
        return readSaveIt(filePath);
    }

    /**
     * Similar to {@link #readSaveIt()}
     *
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlySaveIt> readSaveIt(Path filePath) throws DataConversionException,
            FileNotFoundException {
        requireNonNull(filePath);

        if (!Files.exists(filePath)) {
            logger.info("SaveIt file " + filePath + " not found");
            return Optional.empty();
        }

        XmlSerializableSaveIt xmlSaveIt = XmlFileStorage.loadDataFromSaveFile(filePath);
        try {
            return Optional.of(xmlSaveIt.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveSaveIt(ReadOnlySaveIt saveIt) throws IOException {
        saveSaveIt(saveIt, filePath);
    }

    /**
     * Similar to {@link #saveSaveIt(ReadOnlySaveIt)}
     *
     * @param filePath location of the data. Cannot be null
     */
    public void saveSaveIt(ReadOnlySaveIt saveIt, Path filePath) throws IOException {
        requireNonNull(saveIt);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        XmlFileStorage.saveDataToFile(filePath, new XmlSerializableSaveIt(saveIt));
    }

}
