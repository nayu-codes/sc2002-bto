package bto.model.enquiry;

public class Enquiry {
    private final int enquiryId;
    private final String applicantName;
    private final String projectName;
    private EnquiryMessage applicantMessage;
    private EnquiryMessage replyMessage;
    private boolean isSolved = false;

    /**
     * Constructor for Enquiry.
     * 
     * @param enquiryId The ID of the enquiry.
     * @param applicantName The name of the applicant.
     * @param projectName The name of the project.
     */
    public Enquiry(int enquiryId, String applicantName, String projectName) {
        this.enquiryId = enquiryId;
        this.applicantName = applicantName;
        this.projectName = projectName;
    }

    /**
     * Constructor for Enquiry with messages.
     * 
     * @param enquiryId The ID of the enquiry.
     * @param applicantName The name of the applicant.
     * @param projectName The name of the project.
     * @param applicantMessage The message from the applicant.
     * @param replyMessage The reply message from the HDB Officer / Manager.
     */
    public Enquiry(int enquiryId, String applicantName, String projectName, EnquiryMessage applicantMessage, EnquiryMessage replyMessage) {
        this.enquiryId = enquiryId;
        this.applicantName = applicantName;
        this.projectName = projectName;
        this.applicantMessage = applicantMessage;
        this.replyMessage = replyMessage;
    }

    /**
     * Gets the enquiry ID.
     * 
     * @return The ID of the enquiry.
     */
    public int getEnquiryId() {
        return enquiryId;
    }

    /**
     * Gets the applicant name.
     * 
     * @return The name of the applicant.
     */
    public String getApplicantName() {
        return applicantName;
    }

    /**
     * Gets the project name.
     * 
     * @return The name of the project.
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Gets the applicant message of the enquiry.
     * 
     * @return The applicant message in the enquiry.
     */
    public EnquiryMessage getApplicantMessage() {
        return applicantMessage;
    }

    /**
     * Sets the applicant message of the enquiry.
     * 
     * @return
     */
    public void setApplicantMessage(EnquiryMessage applicantMessage) {
        this.applicantMessage = applicantMessage;
    }

    /**
     * Gets the reply message of the enquiry.
     * 
     * @return The reply message in the enquiry.
     */
    public EnquiryMessage getReplyMessage() {
        return replyMessage;
    }

    /**
     * Gets the status of the enquiry.
     *
     * @return true if the enquiry is solved, false otherwise.
     */
    public boolean isSolved() {
        return isSolved;
    }
}
