package bto.model.user;

import bto.database.ApplicationDB;
import bto.database.BTOProjectDB;
import bto.database.EnquiryDB;
import bto.database.RegistrationDB;
import bto.model.application.ApplicationStatus;
import bto.model.application.BTOApplication;
import bto.model.enquiry.Enquiry;
import bto.model.project.BTOProject;
import bto.model.registration.OfficerRegistration;
import bto.model.registration.RegistrationStatus;

import java.util.ArrayList;

public class HDBOfficer extends Applicant {
    /**
     * Constructor to initialise the HDBOfficer object with common attributes.
     * 
     * @param name The name of the HDBOfficer.
     * @param userId The unique identifier for the HDBOfficer.
     * @param password The password for the HDBOfficer.
     * @param age The age of the HDBOfficer.
     * @param maritalStatus The marital status of the HDBOfficer (e.g., "Single", "Married").
     * @param userType The type of user (e.g., {@link UserType#HDB_OFFICER}).
     * @see UserType
     */
    public HDBOfficer(String name, String userId, String password, int age, MaritalStatus maritalStatus, UserType userType) {
        super(name, userId, password, age, maritalStatus, userType);
    }

    public void registerForProject(int projectId) throws IllegalStateException {
        // Check that the project is not within another project's timeframe
        BTOProject project = BTOProjectDB.getBTOProjectById(projectId);
        for (OfficerRegistration registration : RegistrationDB.getRegistrationsByOfficer(this)) {
            if (registration.getRegistrationStatus() == RegistrationStatus.SUCCESSFUL) { // If Officer is assigned to this project
                BTOProject registeredProject = registration.getProject();
                if (registeredProject.getApplicationOpeningDate().before(project.getApplicationClosingDate()) ||
                    registeredProject.getApplicationOpeningDate() == project.getApplicationClosingDate() ||
                    registeredProject.getApplicationClosingDate().after(project.getApplicationOpeningDate()) ||
                    registeredProject.getApplicationClosingDate() == project.getApplicationOpeningDate()) {
                    throw new IllegalStateException("Cannot register for this project as it overlaps with another assigned project.");
                }
            }
        }

        // Check that officer has no pending BTOApplications for the project
        for (BTOApplication application : ApplicationDB.getApplicationsByApplicant(getUserId())){
            if (application.getProject().getProjectId() == projectId) {
                if (application.getStatus() != ApplicationStatus.UNSUCCESSFUL) {
                    throw new IllegalStateException("Cannot register for this project as there is a pending/completed application for the same project.");
                }
            }
        }

        // Create a new OfficerRegistration object
        OfficerRegistration registration = new OfficerRegistration(RegistrationDB.getRegistrationCount()+1, this, RegistrationStatus.PENDING, project);
        // Add the registration to the registration list
        RegistrationDB.addRegistration(registration);
    }

    /**
     * Retrieves the list of projects registered by this officer. TODO: is there a point? Compared to getting from RegistrationDB directly?
     * 
     * @return An ArrayList of OfficerRegistration objects associated with this officer.
     */
    public ArrayList<OfficerRegistration> getRegisteredProjects() {
        return RegistrationDB.getRegistrationsByOfficer(this);
    }

    /**
     * Reply to an enquiry from an applicant.
     * 
     * @param enquiry The {@link Enquiry} to reply to.
     * @param reply The reply message.
     * 
     * @throws IllegalStateException if the enquiry is not assigned to this officer.
     */
    public void replyToEnquiry(Enquiry enquiry, String reply) throws IllegalStateException {
        // Check if the enquiry is a project that this officer is assigned to
        boolean isAssigned = false;
        for (OfficerRegistration registration : RegistrationDB.getRegistrationsByOfficer(this)) {
            if (registration.getRegistrationStatus() == RegistrationStatus.SUCCESSFUL && registration.getProject().getName() == enquiry.getProjectName()) {
                isAssigned = true;
                break;
            }
        }
        if (!isAssigned) {
            throw new IllegalStateException("Cannot reply to this enquiry as it is not assigned to this officer.");
        }
        
        enquiry.addReplyMessage(this, reply);
    }

    /**
     * Gets the list of enquiries assigned to this officer.
     * 
     * @return An ArrayList of Enquiry objects assigned to this officer.
     */
    public ArrayList<Enquiry> getAssignedEnquiries() {
        ArrayList<Enquiry> assignedEnquiries = new ArrayList<>();
        for (OfficerRegistration registration : RegistrationDB.getRegistrationsByOfficer(this)) {
            if (registration.getRegistrationStatus() == RegistrationStatus.SUCCESSFUL) {
                assignedEnquiries.addAll(EnquiryDB.getEnquiriesByProject(registration.getProject()));
            }
        }
        return assignedEnquiries;
    }

    /**
     * Generate receipt of applicants with their respective flat booking details
     * (Applicant’s Name, NRIC, age, marital status, flat type
     * booked and its project details).
     * 
     * @param applicant The {@link Applicant} for whom the receipt is generated.
     */
    public void generateReceiptForApplicant(Applicant applicant) {
        ArrayList<BTOApplication> applications = ApplicationDB.getApplicationsByApplicant(applicant.getUserId());
        for (BTOApplication application : applications) {
            if (application.getStatus() == ApplicationStatus.BOOKED) {
                System.out.println("Receipt for " + applicant.getName() + ":");
                System.out.println("NRIC: " + applicant.getUserId());
                System.out.println("Age: " + applicant.getAge());
                System.out.println("Marital Status: " + applicant.getMaritalStatus().getStatus());
                System.out.println("Flat Type: " + application.getFlatType().getDisplayName());
                System.out.println("Project Details:");
                System.out.println("  Project Name: " + application.getProject().getName());
                System.out.println("  Project Neighbourhood: " + application.getProject().getNeighbourhood());
            }
        }
    }
}