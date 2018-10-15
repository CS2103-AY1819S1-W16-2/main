package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.ContextChangeEvent;
import seedu.address.commons.events.ui.RecordChangeEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Event;

/**
 * Selects a person identified using it's displayed index from the address book.
 */
public class ManageCommand extends Command {

    public static final String COMMAND_WORD = "manage";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Manages the event identified by the index number used in the displayed event list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_MANAGE_EVENT_SUCCESS = "Selected Event to Manage: %1$s";

    private final Index targetIndex;

    public ManageCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        List<Event> filteredEventList = model.getFilteredEventList();

        if (targetIndex.getZeroBased() >= filteredEventList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        model.switchToRecordContext();

        // TO_UPDATE

        // EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex));
        // EventsCenter.getInstance().post(new ContextChangeEvent(model.getContextId(),
        // filteredEventList.get(targetIndex.getZeroBased())));
        EventsCenter.getInstance().post(new RecordChangeEvent(targetIndex));
        EventsCenter.getInstance().post(new ContextChangeEvent(model.getContextId()));
        return new CommandResult(String.format(MESSAGE_MANAGE_EVENT_SUCCESS, targetIndex.getOneBased()));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ManageCommand // instanceof handles nulls
                && targetIndex.equals(((ManageCommand) other).targetIndex)); // state check
    }
}
