package seedu.address.logic.util;

import java.awt.Color;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javafx.util.Pair;

import seedu.address.model.event.Date;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventId;
import seedu.address.model.record.Hour;
import seedu.address.model.record.Record;
import seedu.address.model.volunteer.Name;
import seedu.address.model.volunteer.Volunteer;
import seedu.address.model.volunteer.VolunteerId;


/**
 * Populates and exports a PDF file containing supplied volunteer information to the given file path.
 */
public class CertGenerator {
    private String savePath;
    private Volunteer volunteer;
    private List<Pair<Record, Event>> volunteerRecordEventPairs;
    private PDDocument doc;

    public CertGenerator(String savePath, Volunteer selectedVolunteer, List<Pair<Record, Event>> recordEventPairs) {
        this.savePath = savePath;
        this.volunteer = selectedVolunteer;
        this.volunteerRecordEventPairs = recordEventPairs;
        this.doc = new PDDocument();
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    public String getSavePath() {
        return savePath;
    }

    /**
     * Populates and exports a PDF with volunteer information from the fields to the save path.
     * @throws IOException if there are issues writing to or exporting PDF file
     */
    public void generatePdf() throws IOException {
        populatePdf();
        exportPdf();
    }

    /**
     * Populates PDF page(s) with volunteer information from the fields.
     * @throws IOException if there are issues writing to the file
     */
    private void populatePdf() throws IOException {
        // Define some constants used in the population
        int maxEventNameLength = 43;
        int titleHeightOffset = 740;
        int bodyIndentWidth = 20;
        String bullet = "\u2022  ";
        Color borderColor = Color.DARK_GRAY;
        float titleLeading = 20f;
        float bodyLeading = 17f;
        int titleFontSize = 24;
        int bodyFontSize = 14;


        // Create a new page and add it to the document
        PDPage page = new PDPage();
        doc.addPage(page);

        // Setup a new content stream to write to a page
        PDPageContentStream contStream = new PDPageContentStream(doc, page);

        // Draw a dark gray border around the page
        contStream.setStrokingColor(borderColor);
        contStream.addRect(bodyIndentWidth / 2,
                bodyIndentWidth / 2,
                page.getMediaBox().getWidth() - bodyIndentWidth,
                page.getMediaBox().getHeight() - bodyIndentWidth);
        contStream.stroke();

        // Retrieve the selected volunteer's attributes
        VolunteerId volunteerId = volunteer.getVolunteerId();
        Name volunteerName = volunteer.getName();

        contStream.beginText();
        contStream.setLeading(titleLeading);

        // Set title font
        PDFont titleFont = PDType1Font.TIMES_BOLD_ITALIC;
        contStream.setFont(titleFont, titleFontSize);

        // Input title to the center of the page
        String title = "Certificate of Recognition";
        float titleWidth = titleFont.getStringWidth(title) * titleFontSize / 1000f;
        contStream.newLineAtOffset(page.getMediaBox().getWidth() / 2 - titleWidth / 2, titleHeightOffset);
        contStream.showText(title);
        contStream.newLine();
        contStream.newLine();

        // Volunteer Name, ID and current date section
        contStream.setFont(PDType1Font.TIMES_BOLD_ITALIC, bodyFontSize);

        contStream.newLineAtOffset(-(page.getMediaBox().getWidth() / 2 - titleWidth / 2) + bodyIndentWidth, 0);

        String volunteerNameLine = "Volunteer Name: " + volunteerName;
        contStream.showText(volunteerNameLine);
        contStream.newLine();

        String volunteerIdLine = "Volunteer NRIC: " + volunteerId;
        contStream.showText(volunteerIdLine);
        contStream.newLine();

        contStream.showText("Date: " + String.valueOf(LocalDate.now().format(DateTimeFormatter
                .ofPattern("dd-MM-yyyy"))));
        contStream.newLine();
        contStream.newLine();

        // Reduce the leading for main body of certificate
        contStream.setLeading(bodyLeading);

        // Standardised formality text
        String formalityTextLine1 = "To whomever it may concern,";
        contStream.showText(formalityTextLine1);
        contStream.newLine();
        contStream.newLine();

        String formalityTextLine2 = "This is to certify " + volunteerName
                + "'s contributions to our organisation via the following event(s):";
        contStream.showText(formalityTextLine2);
        contStream.newLine();
        contStream.newLine();

        // Event contribution information
        int totalHours = 0;
        for (Pair<Record, Event> pair: volunteerRecordEventPairs) {
            // Input information from event record
            Record thisRecord = pair.getKey();
            Event thisEvent = pair.getValue();

            Hour eventHours = thisRecord.getHour();
            String eventName = thisEvent.getName().fullName;
            EventId eventId = thisEvent.getEventId();
            Date endDate = thisEvent.getEndDate();
            Date startDate = thisEvent.getStartDate();

            // Trim event name to prevent exceeding page width
            if (eventName.length() > maxEventNameLength) {
                eventName = eventName.substring(0, maxEventNameLength - 1) + "...";
            }

            String eventEntryLine = eventName + " [" + eventId + "]" + " - " + eventHours + " hour(s) from "
                    + startDate + " to " + endDate;

            contStream.showText(bullet);
            contStream.showText(eventEntryLine);
            contStream.newLine();

            totalHours += Integer.parseInt(eventHours.toString());
        }
        contStream.newLine();

        // Input the total hours from across all records
        String totalHoursLine = "Total hours contributed: " + totalHours;
        contStream.showText(totalHoursLine);
        contStream.newLine();
        contStream.newLine();

        String appreciationLine = "We greatly appreciate " + volunteerName
                + "'s services rendered to our organisation.";
        contStream.showText(appreciationLine);
        contStream.newLine();
        contStream.newLine();

        String regardsLine = "Regards,";
        contStream.showText(regardsLine);
        contStream.newLine();
        contStream.newLine();
        contStream.newLine();

        // Line for user to manually sign off on the certificate
        contStream.showText("___________________");

        // Close the content stream
        contStream.endText();
        contStream.close();
    }

    /**
     * Exports the document to the save path, then closes it.
     * @throws IOException if there are issues saving the document to the filepath or closing the document
     */
    private void exportPdf() throws IOException {
        // Save document as <volunteerName>_<volunteerId>.pdf to the save path
        doc.save(savePath + volunteer.getName() + "_" + volunteer.getVolunteerId() + ".pdf");

        // Close the document
        doc.close();
    }
}
