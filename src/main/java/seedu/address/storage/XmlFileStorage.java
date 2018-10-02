package seedu.address.storage;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import javax.xml.bind.JAXBException;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.XmlUtil;

/**
 * Stores address data in an XML file
 */
public class XmlFileStorage {

    /**
     * Saves the given address data to the specified file.
     */
    public static void saveDataToFile(Path file, XmlSerializableSaveIt saveIt)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, saveIt);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage(), e);
        }
    }

    /**
     * Returns address book in the file or an empty address book
     */
    public static XmlSerializableSaveIt loadDataFromSaveFile(Path file) throws DataConversionException,
            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableSaveIt.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

}
