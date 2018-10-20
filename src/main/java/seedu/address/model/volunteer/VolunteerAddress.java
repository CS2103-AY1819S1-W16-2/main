package seedu.address.model.volunteer;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Volunteer's address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidAddress(String)}
 */
public class VolunteerAddress {
    public static final String MESSAGE_ADDRESS_CONSTRAINTS =
            "Addresses can take any values, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String ADDRESS_VALIDATION_REGEX = "[^\\s].*";

    public final String value;

    /**
     * Constructs an {@code VolunteerAddress}.
     *
     * @param address A valid address.
     */
    public VolunteerAddress(String address) {
        requireNonNull(address);
        checkArgument(isValidAddress(address), MESSAGE_ADDRESS_CONSTRAINTS);
        value = address;
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidAddress(String test) {
        return test.matches(ADDRESS_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof VolunteerAddress // instanceof handles nulls
                && value.equals(((VolunteerAddress) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
