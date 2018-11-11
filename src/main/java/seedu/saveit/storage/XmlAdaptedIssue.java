package seedu.saveit.storage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;

import seedu.saveit.commons.exceptions.IllegalValueException;
import seedu.saveit.model.Issue;
import seedu.saveit.model.issue.Description;
import seedu.saveit.model.issue.IssueSearchFrequency;
import seedu.saveit.model.issue.IssueStatement;
import seedu.saveit.model.issue.Solution;
import seedu.saveit.model.issue.Tag;

/**
 * JAXB-friendly version of the Issue.
 */
public class XmlAdaptedIssue {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Issue's %s field is missing!";

    @XmlElement(required = true)
    private String statement;
    @XmlElement(required = true)
    private String description;

    @XmlElement
    private List<XmlAdaptedSolution> solutions = new ArrayList<>();

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    @XmlElement(required = true)
    private Integer frequency;

    @XmlElement(required = true)
    private Long createdTime;

    @XmlElement(required = true)
    private Long lastModifiedTime;

    /**
     * Constructs an XmlAdaptedIssue. This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedIssue() {}

    /**
     * Constructs an {@code XmlAdaptedIssue} with the given statement details.
     */
    public XmlAdaptedIssue(String statement, String description, List<XmlAdaptedSolution> solutions,
                           List<XmlAdaptedTag> tagged, Integer frequency, Long createdTime, Long lastModifiedTime) {
        this.statement = statement;
        this.description = description;
        if (solutions != null) {
            this.solutions = new ArrayList<>(solutions);
        }
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
        this.frequency = frequency;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /**
     * Converts a given Issue into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedIssue
     */
    public XmlAdaptedIssue(Issue source) {
        statement = source.getStatement().getValue();
        description = source.getDescription().getValue();
        solutions = source.getSolutions().stream()
                .map(XmlAdaptedSolution::new)
                .collect(Collectors.toList());
        tagged = source.getTags().stream()
                .map(XmlAdaptedTag::new)
                .collect(Collectors.toList());
        frequency = source.getFrequency().getValue();
        createdTime = source.getCreatedTime().getTime();
        lastModifiedTime = source.getLastModifiedTime().getTime();
    }

    /**
     * Converts this jaxb-friendly adapted statement object into the model's Issue object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted statement
     */
    public Issue toModelType() throws IllegalValueException {
        final List<Tag> issueTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            issueTags.add(tag.toModelType());
        }

        final List<Solution> issueSolutions = new ArrayList<>();
        for (XmlAdaptedSolution solution : solutions) {
            issueSolutions.add(solution.toModelType());
        }

        if (statement == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    IssueStatement.class.getSimpleName()));
        }
        if (!IssueStatement.isValidIssueStatement(statement)) {
            throw new IllegalValueException(IssueStatement.MESSAGE_ISSUE_STATEMENT_CONSTRAINTS);
        }
        final IssueStatement modelName = new IssueStatement(statement);

        if (description == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName()));
        }
        if (!Description.isValidDescription(description)) {
            throw new IllegalValueException(Description.MESSAGE_DESCRIPTION_CONSTRAINTS);
        }
        final Description modelDescription = new Description(description);

        final List<Solution> modelSolutions = new ArrayList<>(issueSolutions);

        final Set<Tag> modelTags = new LinkedHashSet<>(issueTags);

        final IssueSearchFrequency searchFrequency = new IssueSearchFrequency(frequency);

        final Timestamp modelCreatedTime = new Timestamp(createdTime);

        final Timestamp modelLastModifiedTime = new Timestamp(lastModifiedTime);

        return new Issue(modelName, modelDescription, modelSolutions, modelTags, searchFrequency,
                modelCreatedTime, modelLastModifiedTime);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedIssue)) {
            return false;
        }

        XmlAdaptedIssue otherIssue = (XmlAdaptedIssue) other;
        return Objects.equals(statement, otherIssue.statement)
                && Objects.equals(description, otherIssue.description)
                && solutions.equals(otherIssue.solutions)
                && tagged.equals(otherIssue.tagged)
                && frequency.equals(frequency)
                && createdTime.equals(createdTime)
                && lastModifiedTime.equals(lastModifiedTime);
    }
}
