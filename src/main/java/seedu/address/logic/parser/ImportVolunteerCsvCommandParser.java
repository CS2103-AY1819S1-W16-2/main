package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.logic.commands.ImportVolunteerCsvCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code ImportVolunteerCsvCommand} object
 */
public class ImportVolunteerCsvCommandParser implements Parser<ImportVolunteerCsvCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code ImportVolunteerCsvCommand}
     * and returns a {@code ImportVolunteerCsvCommand} object for execution.
     * @param userInput as a {@code String}
     * @return ImportVolunteerCsvCommand object
     * @throws ParseException if the user input does not abide by the expected format
     */
    public ImportVolunteerCsvCommand parse(String userInput) throws ParseException {
        requireNonNull(userInput);

        try {
            String wew = new File("wew.csv").getAbsolutePath();
            System.out.println(wew);
            Path path = Paths.get("C:/Users/ice_d/Documents/wew.csv");
            BufferedReader br = Files.newBufferedReader(path);

            return new ImportVolunteerCsvCommand(br);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ImportVolunteerCsvCommand.FILE_ERROR));
        }
    }
}
