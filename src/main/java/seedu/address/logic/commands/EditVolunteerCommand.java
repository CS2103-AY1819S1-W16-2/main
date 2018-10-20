package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VOLUNTEER_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VOLUNTEER_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VOLUNTEER_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VOLUNTEER_GENDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VOLUNTEER_BIRTHDAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VOLUNTEER_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VOLUNTEER_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_VOLUNTEERS;


import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.volunteer.VolunteerAddress;
import seedu.address.model.volunteer.VolunteerEmail;
import seedu.address.model.volunteer.VolunteerName;
import seedu.address.model.volunteer.Gender;
import seedu.address.model.volunteer.Birthday;
import seedu.address.model.volunteer.Volunteer;
import seedu.address.model.volunteer.VolunteerId;
import seedu.address.model.volunteer.VolunteerPhone;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing volunteer in the address book.
 */
public class EditVolunteerCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the volunteer identified "
            + "by the index number used in the displayed volunteer list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_VOLUNTEER_NAME + "NAME] "
            + "[" + PREFIX_VOLUNTEER_GENDER + "GENDER] "
            + "[" + PREFIX_VOLUNTEER_BIRTHDAY + "BIRTHDAY] "
            + "[" + PREFIX_VOLUNTEER_PHONE + "PHONE] "
            + "[" + PREFIX_VOLUNTEER_EMAIL + "EMAIL] "
            + "[" + PREFIX_VOLUNTEER_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_VOLUNTEER_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_VOLUNTEER_PHONE + "91234567 "
            + PREFIX_VOLUNTEER_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_VOLUNTEER_SUCCESS = "Edited Volunteer: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_VOLUNTEER = "This volunteer already exists in the address book.";

    private final Index index;
    private final EditVolunteerDescriptor editVolunteerDescriptor;

    /**
     * @param index of the volunteer in the filtered person list to edit
     * @param editVolunteerDescriptor details to edit the volunteer with
     */
    public EditVolunteerCommand(Index index, EditVolunteerDescriptor editVolunteerDescriptor) {
        requireNonNull(index);
        requireNonNull(editVolunteerDescriptor);

        this.index = index;
        this.editVolunteerDescriptor = new EditVolunteerDescriptor(editVolunteerDescriptor);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Volunteer> lastShownList = model.getFilteredVolunteerList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_VOLUNTEER_DISPLAYED_INDEX);
        }

        Volunteer volunteerToEdit = lastShownList.get(index.getZeroBased());
        Volunteer editedVolunteer = createEditedVolunteer(volunteerToEdit, editVolunteerDescriptor);

        if (!volunteerToEdit.isSameVolunteer(editedVolunteer) && model.hasVolunteer(editedVolunteer)) {
            throw new CommandException(MESSAGE_DUPLICATE_VOLUNTEER);
        }

        model.updateVolunteer(volunteerToEdit, editedVolunteer);
        model.updateFilteredVolunteerList(PREDICATE_SHOW_ALL_VOLUNTEERS);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_EDIT_VOLUNTEER_SUCCESS, editedVolunteer));
    }

    /**
     * Creates and returns a {@code Volunteer} with the details of {@code volunteerToEdit}
     * edited with {@code editVolunteerDescriptor}.
     */
    private static Volunteer createEditedVolunteer(Volunteer volunteerToEdit, EditVolunteerDescriptor editVolunteerDescriptor) {
        assert volunteerToEdit != null;

        VolunteerId volunteerId = volunteerToEdit.getVolunteerId();
        VolunteerName updatedName = editVolunteerDescriptor.getName().orElse(volunteerToEdit.getName());
        Gender updatedGender = editVolunteerDescriptor.getGender().orElse(volunteerToEdit.getGender());
        Birthday updatedBirthday = editVolunteerDescriptor.getBirthday().orElse(volunteerToEdit.getBirthday());
        VolunteerPhone updatedPhone = editVolunteerDescriptor.getPhone().orElse(volunteerToEdit.getPhone());
        VolunteerEmail updatedEmail = editVolunteerDescriptor.getEmail().orElse(volunteerToEdit.getEmail());
        VolunteerAddress updatedAddress = editVolunteerDescriptor.getAddress().orElse(volunteerToEdit.getAddress());
        Set<Tag> updatedTags = editVolunteerDescriptor.getTags().orElse(volunteerToEdit.getTags());

        return new Volunteer(volunteerId, updatedName, updatedGender, updatedBirthday, updatedPhone, updatedEmail, updatedAddress, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditVolunteerCommand)) {
            return false;
        }

        // state check
        EditVolunteerCommand e = (EditVolunteerCommand) other;
        return index.equals(e.index)
                && editVolunteerDescriptor.equals(e.editVolunteerDescriptor);
    }

    /**
     * Stores the details to edit the volunteer with. Each non-empty field value will replace the
     * corresponding field value of the volunteer.
     */
    public static class EditVolunteerDescriptor {
        private VolunteerId volunteerId;
        private VolunteerName name;
        private Gender gender;
        private Birthday birthday;
        private VolunteerPhone phone;
        private VolunteerEmail email;
        private VolunteerAddress address;
        private Set<Tag> tags;

        public EditVolunteerDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditVolunteerDescriptor(EditVolunteerDescriptor toCopy) {
            setVolunteerId(toCopy.volunteerId);
            setName(toCopy.name);
            setGender(toCopy.gender);
            setBirthday(toCopy.birthday);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, gender, birthday, phone, email, address, tags);
        }

        public void setVolunteerId(VolunteerId volunteerId) { this.volunteerId = volunteerId; }

        public void setName(VolunteerName name) {
            this.name = name;
        }

        public Optional<VolunteerName> getName() {
            return Optional.ofNullable(name);
        }

        public void setGender(Gender gender) {
            this.gender = gender;
        }

        public Optional<Gender> getGender() {
            return Optional.ofNullable(gender);
        }

        public void setBirthday(Birthday birthday) {
            this.birthday = birthday;
        }

        public Optional<Birthday> getBirthday() { return Optional.ofNullable(birthday); }

        public void setPhone(VolunteerPhone phone) {
            this.phone = phone;
        }

        public Optional<VolunteerPhone> getPhone() { return Optional.ofNullable(phone); }

        public void setEmail(VolunteerEmail email) {
            this.email = email;
        }

        public Optional<VolunteerEmail> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(VolunteerAddress address) {
            this.address = address;
        }

        public Optional<VolunteerAddress> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditVolunteerDescriptor)) {
                return false;
            }

            // state check
            EditVolunteerDescriptor e = (EditVolunteerDescriptor) other;

            return getName().equals(e.getName())
                    && getGender().equals(e.getGender())
                    && getBirthday().equals(e.getBirthday())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getTags().equals(e.getTags());
        }
    }
}