package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.model.event.Event;
import seedu.address.model.person.Person;
import seedu.address.model.record.Record;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedAddressBook versionedAddressBook;

    private final Context context;

    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Event> filteredEvents;
    private final FilteredList<Record> filteredRecords;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        versionedAddressBook = new VersionedAddressBook(addressBook);

        context = new Context(Context.VOLUNTEER_CONTEXT_ID, Context.VOLUNTEER_CONTEXT_NAME);

        filteredPersons = new FilteredList<>(versionedAddressBook.getPersonList());
        filteredEvents = new FilteredList<>(versionedAddressBook.getEventList());
        filteredRecords = new FilteredList<>(versionedAddressBook.getRecordList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        versionedAddressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return versionedAddressBook;
    }

    /**
     * Raises an event to indicate the model has changed
     */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(versionedAddressBook));
    }

    //===========  Context Switching Methods =============================================================
    @Override
    public void setCurrentContext(String contextId) {
        requireAllNonNull(contextId);
        context.setContextValue(contextId);
    }

    @Override
    public void switchToRecordContext() {
        context.switchToRecordContext();
    }

    @Override
    public String getContextId() {
        return context.getContextId();
    }

    @Override
    public String getContextName() {
        return context.getContextName();
    }

    //===========  Person List Methods =============================================================
    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return versionedAddressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        versionedAddressBook.removePerson(target);
        indicateAddressBookChanged();
    }

    @Override
    public void addPerson(Person person) {
        versionedAddressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public void updatePerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        versionedAddressBook.updatePerson(target, editedPerson);
        indicateAddressBookChanged();
    }

    //=========== Filtered Person Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredPersons);
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }


    //===========  Event List Methods =============================================================
    @Override
    public boolean hasEvent(Event event) {
        requireNonNull(event);
        return versionedAddressBook.hasEvent(event);
    }

    @Override
    public void deleteEvent(Event target) {
        versionedAddressBook.removeEvent(target);
        indicateAddressBookChanged();
    }

    @Override
    public void addEvent(Event event) {
        versionedAddressBook.addEvent(event);
        updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
        indicateAddressBookChanged();
    }

    @Override
    public void updateEvent(Event target, Event editedEvent) {
        requireAllNonNull(target, editedEvent);

        versionedAddressBook.updateEvent(target, editedEvent);
        indicateAddressBookChanged();
    }

    //=========== Filtered Event List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Event} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Event> getFilteredEventList() {
        return FXCollections.unmodifiableObservableList(filteredEvents);
    }

    @Override
    public void updateFilteredEventList(Predicate<Event> predicate) {
        requireNonNull(predicate);
        filteredEvents.setPredicate(predicate);
    }

    //===========  Record List Methods =============================================================
    @Override
    public boolean hasRecord(Record record) {
        requireNonNull(record);
        return versionedAddressBook.hasRecord(record);
    }

    @Override
    public void deleteRecord(Record target) {
        versionedAddressBook.removeRecord(target);
        indicateAddressBookChanged();
    }

    @Override
    public void addRecord(Record record) {
        versionedAddressBook.addRecord(record);
        updateFilteredRecordList(PREDICATE_SHOW_ALL_RECORDS);
        indicateAddressBookChanged();
    }

    @Override
    public void updateRecord(Record target, Record editedRecord) {
        requireAllNonNull(target, editedRecord);

        versionedAddressBook.updateRecord(target, editedRecord);
        indicateAddressBookChanged();
    }

    //=========== Filtered Record List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Record} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Record> getFilteredRecordList() {
        return FXCollections.unmodifiableObservableList(filteredRecords);
    }

    @Override
    public void updateFilteredRecordList(Predicate<Record> predicate) {
        requireNonNull(predicate);
        filteredRecords.setPredicate(predicate);
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoAddressBook() {
        return versionedAddressBook.canUndo();
    }

    @Override
    public boolean canRedoAddressBook() {
        return versionedAddressBook.canRedo();
    }

    @Override
    public void undoAddressBook() {
        versionedAddressBook.undo();
        indicateAddressBookChanged();
    }

    @Override
    public void redoAddressBook() {
        versionedAddressBook.redo();
        indicateAddressBookChanged();
    }

    @Override
    public void commitAddressBook() {
        versionedAddressBook.commit();
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedAddressBook.equals(other.versionedAddressBook)
                && filteredPersons.equals(other.filteredPersons)
                && filteredEvents.equals(other.filteredEvents);
    }

}
