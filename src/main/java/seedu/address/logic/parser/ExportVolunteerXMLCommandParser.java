package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ExportVolunteerXMLCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code ExportVolunteerXMLCommand} object
 */
public class ExportVolunteerXMLCommandParser implements Parser<ExportVolunteerXMLCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code ExportVolunteerXMLCommand}
     * and returns a {@code ExportVolunteerXMLCommand} object for execution.
     * @param userInput as a {@code String}
     * @return ExportVolunteerXMLCommand object
     * @throws ParseException if the user input does not abide by the expected format
     */
    public ExportVolunteerXMLCommand parse(String userInput) throws ParseException {
        requireNonNull(userInput);

        Index index;
        try {
            index = ParserUtil.parseIndex(userInput);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ExportVolunteerXMLCommand.MESSAGE_USAGE), pe);
        }

        return new ExportVolunteerXMLCommand(index);
    }
}