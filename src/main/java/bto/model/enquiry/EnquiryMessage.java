package bto.model.enquiry;

import java.util.Date;

public final class EnquiryMessage {
    private final String authorName;
    private final String message;
    private final Date dateTime;

    /**
     * Constructor for EnquiryMessage.
     * 
     * @param authorName The name of the author of the message.
     * @param message The content of the message.
     * @param dateTime The date and time when the message was sent.
     */
    public EnquiryMessage(String authorName, String message, Date dateTime) {
        this.authorName = authorName;
        this.message = message;
        this.dateTime = dateTime;
    }

    /**
     * Gets the author name.
     * 
     * @return The name of the author of the message.
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * Gets the message content.
     * 
     * @return The content of the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the date and time of the message.
     * 
     * @return The date and time when the message was sent.
     */
    public Date getDateTime() {
        return dateTime;
    }
}
