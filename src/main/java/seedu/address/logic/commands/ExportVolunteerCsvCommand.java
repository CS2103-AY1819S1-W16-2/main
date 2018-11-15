package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.volunteer.Volunteer;

/**
 * Exports a person's volunteer information from SocialCare
 */
public class ExportVolunteerCsvCommand extends Command {
    public static final String COMMAND_WORD = "exportvolunteercsv";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports a CSV file of the volunteer "
            + "the specified index in the displayed volunteer list.\n"
            + "Example: " + COMMAND_WORD;

    private static final String MESSAGE_EXPORT_VOLUNTEER_SUCCESS = "Volunteer(s) exported as CSV ";
    private static final String MESSAGE_EXPORT_VOLUNTEER_FAILED = "Volunteer(s) export failed, please try again.";

    private static final String DEFAULT_SAVE_PATH = System.getProperty("user.dir")
            + File.separator + "VolunteerCSV"
            + File.separator;
    private static final String ALT_SAVE_PATH = System.getProperty("user.home")
            + File.separator + "Desktop"
            + File.separator;
    private static String SAVE_PATH = DEFAULT_SAVE_PATH;

    private static final java.util.logging.Logger logger = LogsCenter.getLogger(ExportEventCsvCommand.class);


    /**
     * Exports the entire list out
     */
    public ExportVolunteerCsvCommand() {
        //Create folder for output
        File exportDir = new File(DEFAULT_SAVE_PATH);
        if (!exportDir.exists()) {
            try {
                exportDir.mkdir();
            } catch (SecurityException se) {
                logger.warning("Couldn't create a relative export path next to jar file. "
                        + "Defaulting to user's Desktop.");
                SAVE_PATH = ALT_SAVE_PATH;
            }
        }
    }


    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Volunteer> list = model.getFilteredVolunteerList();

        try {
            createVolunteerCsv(model.getFilteredVolunteerList());
        } catch (FileNotFoundException e) {
            throw new CommandException(MESSAGE_EXPORT_VOLUNTEER_FAILED);
        }

        return new CommandResult(MESSAGE_EXPORT_VOLUNTEER_SUCCESS);

    }

    /**
     * Helper method to create and write the csv file given the volunteer
     * @param list to contain the list of volunteers from model
     */
    private void createVolunteerCsv(ObservableList<Volunteer> list) throws FileNotFoundException {
        // Setting up file path
        File output = new File(SAVE_PATH
                + Integer.toString(list.size()) + "volunteers.csv");

        // Setting up writer & stringbuilder for appending
        PrintWriter pw = new PrintWriter(output);
        StringBuilder sb = new StringBuilder();
        String csvSplit = "\",\"";

        //appending column titles
        sb.append( "\"" + "Name" + csvSplit);
        sb.append("Gender" + csvSplit);
        sb.append("Birthday" + csvSplit);
        sb.append("Phone" + csvSplit);
        sb.append("Email" + csvSplit);
        sb.append("Address" + csvSplit);
        sb.append("VolunteerID" + "\"");
        sb.append(System.getProperty("line.separator"));

        for (Volunteer volunteer : list) {

            //appending volunteer information accordingly
            sb.append("\"" + volunteer.getName().toString() + csvSplit);
            sb.append(volunteer.getGender().toString() + csvSplit);
            sb.append(volunteer.getBirthday().toString() + csvSplit);
            sb.append(volunteer.getPhone().toString() + csvSplit);
            sb.append(volunteer.getEmail().toString() + csvSplit);
            sb.append(volunteer.getAddress().toString() + csvSplit);
            sb.append(volunteer.getVolunteerId().toString() + "\"");
            sb.append(System.getProperty("line.separator"));
        }

        pw.write(sb.toString());
        pw.close();
    }

}
