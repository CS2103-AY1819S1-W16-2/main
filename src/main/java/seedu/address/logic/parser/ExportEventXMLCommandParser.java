package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ExportEventXMLCommand;
import seedu.address.logic.parser.exceptions.ParseException;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

/**
 * Parses input arguments and creates a new {@code ExportEventXMLCommand} object
 */
public class ExportEventXMLCommandParser implements Parser<ExportEventXMLCommand> {

        /**
         * Parses the given {@code String} of arguments in the context of the {@code ExportEventXMLCommand}
         * and returns a {@code ExportEventXMLCommand} object for execution.
         * @param userInput as a {@code String}
         * @return ExportEventXMLCommand object
         * @throws ParseException if the user input does not abide by the expected format
         */
        public ExportEventXMLCommand parse(String userInput) throws ParseException {
            requireNonNull(userInput);

            Index index;
            try {
                index = ParserUtil.parseIndex(userInput);
            } catch (ParseException pe) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        ExportEventXMLCommand.MESSAGE_USAGE), pe);
            }

            return new ExportEventXMLCommand(index);
        }


}
