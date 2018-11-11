package seedu.saveit.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.saveit.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_FIRST_ISSUE;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.saveit.logic.parser.exceptions.ParseException;
import seedu.saveit.model.issue.Description;
import seedu.saveit.model.issue.IssueStatement;
import seedu.saveit.model.issue.Tag;
import seedu.saveit.model.issue.solution.Remark;
import seedu.saveit.testutil.Assert;

public class ParserUtilTest {

    private static final String INVALID_STATEMENT = " ";
    private static final String INVALID_DESCRIPTION = " ";
    private static final String INVALID_TAG = "my friend";
    private static final String INVALID_REMARK = " ";
    private static final String INVALID_LINK = "wwwstackoverflowcom";

    private static final String VALID_STATEMENT = "StackOverFlow";
    private static final String VALID_DESCRIPTION = "123456";
    private static final String VALID_REMARK = "This is a remark; this remark is #1.";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";
    private static final String VALID_LINK = "https://github.com/CS2103-AY1819S1-T12-4/main";

    private static final String WHITESPACE = " \t\r\n";

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseIndex_invalidInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        ParserUtil.parseIndex("10 a");
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_INVALID_INDEX);
        ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_ISSUE, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_ISSUE, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseStatement_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseStatement((String) null));
    }

    @Test
    public void parseStatement_invalidValue_throwsParseException() {
        Assert.assertThrows(ParseException.class, () -> ParserUtil.parseStatement(INVALID_STATEMENT));
    }

    @Test
    public void parseStatement_validValueWithoutWhitespace_returnsName() throws Exception {
        IssueStatement expectedName = new IssueStatement(VALID_STATEMENT);
        assertEquals(expectedName, ParserUtil.parseStatement(VALID_STATEMENT));
    }

    @Test
    public void parseStatement_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String statementWithWhitespace = WHITESPACE + VALID_STATEMENT + WHITESPACE;
        IssueStatement expectedName = new IssueStatement(VALID_STATEMENT);
        assertEquals(expectedName, ParserUtil.parseStatement(statementWithWhitespace));
    }

    @Test
    public void parseStatement_lengthExceedsLimit_throwsParseException() {
        String longStatement = new String(new char[2]).replace("\0", VALID_STATEMENT);
        Assert.assertThrows(ParseException.class, () -> ParserUtil.parseStatement(longStatement));
    }

    @Test
    public void parseDescription_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseDescription((String) null));
    }

    @Test
    public void parseDescription_invalidValue_throwsParseException() {
        Assert.assertThrows(ParseException.class, () -> ParserUtil.parseDescription(INVALID_DESCRIPTION));
    }

    @Test
    public void parseDescription_validValueWithoutWhitespace_returnsDescription() throws Exception {
        Description expectedDescription = new Description(VALID_DESCRIPTION);
        assertEquals(expectedDescription, ParserUtil.parseDescription(VALID_DESCRIPTION));
    }

    @Test
    public void parseDescription_validValueWithWhitespace_returnsTrimmedDescription() throws Exception {
        String descriptionsWithWhitespace = WHITESPACE + VALID_DESCRIPTION + WHITESPACE;
        Description expectedDescription = new Description(VALID_DESCRIPTION);
        assertEquals(expectedDescription, ParserUtil.parseDescription(descriptionsWithWhitespace));
    }

    @Test
    public void parseSolution_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseSolution((String) null, (String) null));
    }

    @Test
    public void parseSolutionLink_invalidValue_throwsParseException() {
        Assert.assertThrows(ParseException.class, () -> ParserUtil.parseSolutionLink((INVALID_LINK)));
    }

    @Test
    public void parseRemark_validValueWithoutWhitespace_returnsRemark() throws Exception {
        Remark expectedSolution = new Remark(VALID_REMARK);
        assertEquals(expectedSolution, ParserUtil.parseSolutionRemark(VALID_REMARK));
    }

    @Test
    public void parseSolutionRemark_validValueWithWhitespace_returnsTrimmedRemark() throws Exception {
        String remarkWithWhitespace = WHITESPACE + VALID_REMARK + WHITESPACE;
        Remark expectedRemark = new Remark(VALID_REMARK);
        assertEquals(expectedRemark, ParserUtil.parseSolutionRemark(remarkWithWhitespace));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseTag(null);
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        ParserUtil.parseTag(INVALID_TAG);
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseTags(null);
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    public void parseTag_lengthExceedsLimit_throwsParseException() {
        String longTag = new String(new char[4]).replace("\0", VALID_TAG_1);
        Assert.assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1,
                VALID_TAG_2, longTag)));
    }
}
