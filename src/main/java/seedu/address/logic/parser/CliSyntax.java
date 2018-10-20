package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {
    /* Switch Prefix definitions */
    public static final Prefix PREFIX_SWITCH = new Prefix("-");

    /* Person Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");

    /* Volunteer Prefix definitions */
    public static final Prefix PREFIX_VOLUNTEER_NAME = new Prefix("n/");
    public static final Prefix PREFIX_VOLUNTEER_GENDER = new Prefix("g/");
    public static final Prefix PREFIX_VOLUNTEER_BIRTHDAY = new Prefix("b/");
    public static final Prefix PREFIX_VOLUNTEER_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_VOLUNTEER_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_VOLUNTEER_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_VOLUNTEER_TAG = new Prefix("t/");

    /* Event Prefix definitions */
    public static final Prefix PREFIX_EVENT_NAME = new Prefix("n/");
    public static final Prefix PREFIX_EVENT_LOCATION = new Prefix("l/");
    public static final Prefix PREFIX_EVENT_START_DATE = new Prefix("sd/");
    public static final Prefix PREFIX_EVENT_END_DATE = new Prefix("ed/");
    public static final Prefix PREFIX_EVENT_START_TIME = new Prefix("st/");
    public static final Prefix PREFIX_EVENT_END_TIME = new Prefix("et/");
    public static final Prefix PREFIX_EVENT_DESCRIPTION = new Prefix("d/");

    /* Record Prefix definitions */
    public static final Prefix PREFIX_RECORD_HOUR = new Prefix("h/");
    public static final Prefix PREFIX_RECORD_REMARK = new Prefix("r/");
}
