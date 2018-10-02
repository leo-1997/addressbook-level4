package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ReadOnlySaveIt;
import seedu.address.model.SaveIt;
import seedu.address.model.issue.Issue;

/**
 * An Immutable SaveIt that is serializable to XML format
 */
@XmlRootElement(name = "address")
public class XmlSerializableSaveIt {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate issue(s).";

    @XmlElement
    private List<XmlAdaptedPerson> persons;

    /**
     * Creates an empty XmlSerializableSaveIt. This empty constructor is required for marshalling.
     */
    public XmlSerializableSaveIt() {
        persons = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableSaveIt(ReadOnlySaveIt src) {
        this();
        persons.addAll(src.getPersonList().stream().map(XmlAdaptedPerson::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address into the model's {@code SaveIt} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the {@code
     * XmlAdaptedPerson}.
     */
    public SaveIt toModelType() throws IllegalValueException {
        SaveIt saveIt = new SaveIt();
        for (XmlAdaptedPerson p : persons) {
            Issue issue = p.toModelType();
            if (saveIt.hasPerson(issue)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            saveIt.addPerson(issue);
        }
        return saveIt;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableSaveIt)) {
            return false;
        }
        return persons.equals(((XmlSerializableSaveIt) other).persons);
    }
}
