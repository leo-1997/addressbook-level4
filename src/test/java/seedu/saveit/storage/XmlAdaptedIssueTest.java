package seedu.saveit.storage;

import static org.junit.Assert.assertEquals;
import static seedu.saveit.storage.XmlAdaptedIssue.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.saveit.testutil.TypicalIssues.C_SEGMENTATION_FAULT;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.saveit.commons.exceptions.IllegalValueException;
import seedu.saveit.model.issue.Description;
import seedu.saveit.model.issue.IssueStatement;
import seedu.saveit.testutil.Assert;

public class XmlAdaptedIssueTest {
    private static final String INVALID_STATEMENT = " ";
    private static final String INVALID_DESCRIPTION = " ";
    private static final String INVALID_TAG = " ";
    private static final String INVALID_SOLUTION_LINK = "&StackπOverflow";
    private static final String INVALID_REMARK = "*remark";

    private static final String VALID_STATEMENT = C_SEGMENTATION_FAULT.getStatement().toString();
    private static final String VALID_DESCRIPTION = C_SEGMENTATION_FAULT.getDescription().toString();
    private static final List<XmlAdaptedSolution> VALID_SOLUTIONS = C_SEGMENTATION_FAULT.getSolutions().stream()
            .map(XmlAdaptedSolution::new)
            .collect(Collectors.toList());
    private static final List<XmlAdaptedTag> VALID_TAGS = C_SEGMENTATION_FAULT.getTags().stream()
        .map(XmlAdaptedTag::new)
        .collect(Collectors.toList());
    private static final Integer VALID_FREQUENCY = 1;
    private static final Long VALID_TIME = new Long("1541509287278");

    @Test
    public void toModelType_validIssueDetails_returnsIssue() throws Exception {
        XmlAdaptedIssue issue = new XmlAdaptedIssue(C_SEGMENTATION_FAULT);
        assertEquals(C_SEGMENTATION_FAULT, issue.toModelType());
    }

    @Test
    public void toModelType_invalidStatement_throwsIllegalValueException() {
        XmlAdaptedIssue issue =
            new XmlAdaptedIssue(INVALID_STATEMENT, VALID_DESCRIPTION, VALID_SOLUTIONS, VALID_TAGS, VALID_FREQUENCY,
                VALID_TIME, VALID_TIME);
        String expectedMessage = IssueStatement.MESSAGE_ISSUE_STATEMENT_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, issue::toModelType);
    }

    @Test
    public void toModelType_nullStatement_throwsIllegalValueException() {
        XmlAdaptedIssue issue = new XmlAdaptedIssue(null, VALID_DESCRIPTION, VALID_SOLUTIONS, VALID_TAGS,
                VALID_FREQUENCY, VALID_TIME, VALID_TIME);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, IssueStatement.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, issue::toModelType);
    }

    @Test
    public void toModelType_invalidDescription_throwsIllegalValueException() {
        XmlAdaptedIssue issue =
            new XmlAdaptedIssue(VALID_STATEMENT, INVALID_DESCRIPTION, VALID_SOLUTIONS, VALID_TAGS, VALID_FREQUENCY,
                    VALID_TIME, VALID_TIME);
        String expectedMessage = Description.MESSAGE_DESCRIPTION_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, issue::toModelType);
    }

    @Test
    public void toModelType_nullDescription_throwsIllegalValueException() {
        XmlAdaptedIssue issue = new XmlAdaptedIssue(VALID_STATEMENT, null, VALID_SOLUTIONS, VALID_TAGS, VALID_FREQUENCY,
                VALID_TIME, VALID_TIME);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, issue::toModelType);
    }

    @Test
    public void toModelType_invalidSolutions_throwsIllegalValueException() {
        List<XmlAdaptedSolution> invalidSolutions = new ArrayList<>(VALID_SOLUTIONS);
        invalidSolutions.add(new XmlAdaptedSolution(INVALID_SOLUTION_LINK, INVALID_REMARK));
        XmlAdaptedIssue issue =
            new XmlAdaptedIssue(VALID_STATEMENT, VALID_DESCRIPTION, invalidSolutions, VALID_TAGS, VALID_FREQUENCY,
                    VALID_TIME, VALID_TIME);
        Assert.assertThrows(IllegalValueException.class, issue::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<XmlAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new XmlAdaptedTag(INVALID_TAG));
        XmlAdaptedIssue issue =
            new XmlAdaptedIssue(VALID_STATEMENT, VALID_DESCRIPTION, VALID_SOLUTIONS, invalidTags, VALID_FREQUENCY,
                    VALID_TIME, VALID_TIME);
        Assert.assertThrows(IllegalValueException.class, issue::toModelType);
    }

}
