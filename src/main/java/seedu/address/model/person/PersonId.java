package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

/**
 * Represents an Event's Id in the application. Event Id is used in identifying record entries.
 * Guarantees: immutable; is valid as declared in {@link #isValidId(int)}
 */
public class PersonId {
    public static final String MESSAGE_NAME_CONSTRAINTS =
            "Id should be more than zero, and it should not be blank";

    public final int id;
    /**
     * Constructs an {@code id}.
     *
     * @param id A valid id.
     */
    public PersonId(int id) {
        requireNonNull(id);
        this.id = id;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidId(int test) {
        return test > 0;
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PersonId // instanceof handles nulls
                && id == ((PersonId) other).id); // state check
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
