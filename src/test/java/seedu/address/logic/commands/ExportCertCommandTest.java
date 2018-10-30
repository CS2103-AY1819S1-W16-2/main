package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertExportCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showVolunteerAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_VOLUNTEER;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_VOLUNTEER;
import static seedu.address.testutil.TypicalVolunteers.getTypicalVolunteerAddressBook;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.volunteer.Volunteer;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ExportCertCommand.
 */
public class ExportCertCommandTest {

    private Model model = new ModelManager(getTypicalVolunteerAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validVolunteerIndexUnfilteredList_success() {
        // Get the volunteer to be exported
        Volunteer volunteerToExport = model.getFilteredVolunteerList().get(INDEX_FIRST_VOLUNTEER.getZeroBased());

        // Create the export cert command with the first volunteer's index
        ExportCertCommand exportCertCommand = new ExportCertCommand(INDEX_FIRST_VOLUNTEER);

        String expectedMessage = String.format(ExportCertCommand.MESSAGE_EXPORT_CERT_SUCCESS,
                INDEX_FIRST_VOLUNTEER.getOneBased());

        // Make sure there is a pdf file with the volunteer's name ONLY
        assertExportCommandSuccess(exportCertCommand, model, commandHistory, expectedMessage, volunteerToExport);
    }

    @Test
    public void execute_invalidVolunteerIndexUnfilteredList_throwsCommandException() {
        // Find an invalid volunteer index that is out of bounds
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredVolunteerList().size() + 1);

        // Create the export cert command with the invalid out of bound index
        ExportCertCommand exportCertCommand = new ExportCertCommand(outOfBoundIndex);

        assertCommandFailure(exportCertCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_VOLUNTEER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validVolunteerIndexFilteredList_success() {
        // Filter to only show the volunteer at INDEX_FIRST_VOLUNTEER
        showVolunteerAtIndex(model, INDEX_FIRST_VOLUNTEER);

        // Get the volunteer to be exported
        Volunteer volunteerToExport = model.getFilteredVolunteerList().get(INDEX_FIRST_VOLUNTEER.getZeroBased());

        // Create the export cert command with the first volunteer's index
        ExportCertCommand exportCertCommand = new ExportCertCommand(INDEX_FIRST_VOLUNTEER);

        String expectedMessage = String.format(ExportCertCommand.MESSAGE_EXPORT_CERT_SUCCESS,
                INDEX_FIRST_VOLUNTEER.getOneBased());

        assertExportCommandSuccess(exportCertCommand, model, commandHistory, expectedMessage, volunteerToExport);
    }

    @Test
    public void execute_invalidVolunteerIndexFilteredList_throwsCommandException() {
        // Filter to only show the volunteer at INDEX_FIRST_VOLUNTEER
        showVolunteerAtIndex(model, INDEX_FIRST_VOLUNTEER);

        // Invalid volunteer index that is out of filtered bounds
        Index outOfBoundIndex = INDEX_SECOND_VOLUNTEER;

        // Make sure outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getVolunteerList().size());

        // Create the export cert command with the invalid out of bound index
        ExportCertCommand exportCertCommand = new ExportCertCommand(outOfBoundIndex);

        assertCommandFailure(exportCertCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_VOLUNTEER_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final ExportCertCommand standardCommand = new ExportCertCommand(INDEX_FIRST_VOLUNTEER);

        // different object, same value -> return true
        ExportCertCommand commandWithSameValue = new ExportCertCommand(INDEX_FIRST_VOLUNTEER);
        assertTrue(standardCommand.equals(commandWithSameValue));

        // same object, same values -> return true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> return false
        assertFalse(standardCommand.equals(null));

        // different type -> return false
        assertFalse(standardCommand.equals(new ListCommand()));

        // different index value -> return false
        assertFalse(standardCommand.equals(new ExportCertCommand(INDEX_SECOND_VOLUNTEER)));
    }
}
