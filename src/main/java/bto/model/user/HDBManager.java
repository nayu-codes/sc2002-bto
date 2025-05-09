package bto.model.user;

import bto.model.project.BTOProject;
import bto.model.project.FlatType;
import bto.database.ApplicationDB;
import bto.database.BTOProjectDB;
import bto.database.EnquiryDB;
import bto.database.RegistrationDB;
import bto.model.application.ApplicationStatus;
import bto.model.application.BTOApplication;
import bto.model.enquiry.Enquiry;
import bto.model.registration.OfficerRegistration;
import bto.model.registration.RegistrationStatus;

import java.util.ArrayList;
import java.util.List;

public class HDBManager extends User{
    /**
     * Constructor to initialise the HDBManager object with common attributes.
     * 
     * @param name The name of the HDBManager.
     * @param userId The unique identifier for the HDBManager.
     * @param password The password for the HDBManager.
     * @param age The age of the HDBManager.
     * @param maritalStatus The marital status of the HDBManager (e.g., "Single", "Married").
     * @param userType The type of user (e.g., {@link UserType#HDB_MANAGER}).
     * @see UserType
     */
    public HDBManager(String name, String userId, String password, int age, MaritalStatus maritalStatus, UserType userType) {
        super(name, userId, password, age, maritalStatus, userType);
    }

    /**
     * Create a BTO project listing. Project information is provided from ProjectController.createProject()
     * 
     * @param project The BTOProject object containing the project information.
     * 
     * @return The ID of the created project.
     * 
     * @throws IllegalStateException if the project cannot be created due to an error (e.g. Overlapping application periods).
     */
    public int createProject(BTOProject project) throws IllegalStateException {
        // Check if the project opening and closing dates overlap with currently managed projects
        for (BTOProject existingProject : BTOProjectDB.getBTOProjectList()) {
            // Check if the project is managed by this manager
            if (existingProject.getProjectManager().getUserId().equals(this.getUserId())) {
                // Check if the project opening and closing dates overlap with existing projects, inclusive
                if (existingProject.getApplicationOpeningDate().before(project.getApplicationClosingDate()) ||
                    existingProject.getApplicationOpeningDate() == project.getApplicationClosingDate() ||
                    existingProject.getApplicationClosingDate().after(project.getApplicationOpeningDate()) ||
                    existingProject.getApplicationClosingDate() == project.getApplicationOpeningDate()) {
                    throw new IllegalStateException("Cannot create project as it overlaps with another project you are handling.");
                }
            }
        }

        // Add the project to the database
        return BTOProjectDB.addBTOProject(project);
    }

    /**
     * Edits an existing BTO project listing. Project information is provided from ProjectController.editProject()
     * 
     * @param project The BTOProject object containing the updated project information.
     * 
     * @return The ID of the updated project.
     * 
     * @throws IllegalAccessException if the project cannot be edited due to an error (e.g. Project not found, not managed by this manager).
     * @throws IllegalStateException if the project cannot be edited due to an error (e.g. Overlapping application periods).
     */
    public int editProject(BTOProject project) throws IllegalAccessException, IllegalStateException {
        // Check if new project opening and closing dates overlap with currently managed projects
        for (BTOProject existingProject : BTOProjectDB.getBTOProjectsByManager(this)) {
            if (existingProject.getName() != project.getName()) {
                // Check if the project opening and closing dates overlap with existing projects, inclusive
                if (existingProject.getApplicationOpeningDate().before(project.getApplicationClosingDate()) ||
                    existingProject.getApplicationOpeningDate() == project.getApplicationClosingDate() ||
                    existingProject.getApplicationClosingDate().after(project.getApplicationOpeningDate()) ||
                    existingProject.getApplicationClosingDate() == project.getApplicationOpeningDate()) {
                    throw new IllegalStateException("Cannot edit project as it will overlaps with another project you are handling.");
                }
            }
        }

        // Check if the project is managed by this manager
        for (BTOProject existingProject : BTOProjectDB.getBTOProjectsByManager(this)) {
            if (existingProject.getName().equals(project.getName())) {
                // Update the project in the database
                BTOProjectDB.updateBTOProject(project.getName(), project);
                return project.getProjectId();
            }
        }
        throw new IllegalStateException("Cannot edit project as it is not managed by this officer.");
    }

    /**
     * Delete a BTO project listing. Project information is provided from ProjectController.deleteProject()
     * @param project The BTOProject object to be deleted.
     * 
     * @return true if the project was successfully deleted, false otherwise.
     * 
     * @throws IllegalAccessException if the project cannot be deleted due to an error (e.g. Project not found, not managed by this manager).
     */
    public boolean deleteProject(BTOProject project) throws IllegalAccessException {
        // Check if the project is managed by this manager
        for (BTOProject existingProject : BTOProjectDB.getBTOProjectsByManager(this)) {
            if (existingProject.getName().equals(project.getName())) {
                // Delete the project from the database
                return BTOProjectDB.deleteBTOProject(project);
            }
        }
        throw new IllegalAccessException("Cannot delete project as it is not managed by this officer.");
    }

    /**
     * Toggle project visibility between visible and hidden by toggling the {@link BTOProject#getVisibility()}.
     * 
     * @param project The BTOProject object to be toggled.
     * 
     * @return The updated visibility status of the project.
     * 
     * @throws IllegalAccessException if the project cannot be toggled due to an error.
     */
    public boolean toggleProjectVisibility(BTOProject project) throws IllegalAccessException {
        // Toggle the visibility status of the project
        project.setVisibility(!project.getVisibility());
        // Update the project in the database
        if(BTOProjectDB.updateBTOProject(project.getName(), project)) {
            return project.getVisibility();
        } else {
            throw new IllegalAccessException("Unknown error occured. Please try again.");
        }
    }

    /**
     * Get all projects managed by all managers, regardless of visibility status.
     * 
     * @return A list of BTOProject objects managed by all managers.
     */
    public List<BTOProject> getAllProjects() {
        return BTOProjectDB.getBTOProjectList();
    }

    /**
     * Get all projects managed by the HDBManager, regardless of visibility status.
     * 
     * @return A list of BTOProject objects managed by the HDBManager.
     */
    public ArrayList<BTOProject> getManagedProjects() {
        ArrayList<BTOProject> managedProjects = new ArrayList<>();
        for (BTOProject project : BTOProjectDB.getBTOProjectList()) {
            if (project.getProjectManager().getUserId().equals(this.getUserId())) {
                managedProjects.add(project);
            }
        }
        return managedProjects;
    }

    /**
     * Get all pending and successful registrations for all projects, even if the
     * projects are not managed by this manager.
     * 
     * @return A list of OfficerRegistration objects with pending or successful
     *         status.
     */
    public ArrayList<OfficerRegistration> getPendingAndSuccessfulRegistrations() {
        ArrayList<OfficerRegistration> pendingAndApprovedRegistrations = new ArrayList<>();
        for (OfficerRegistration registration : RegistrationDB.getAllRegistrations()) {
            if (registration.getRegistrationStatus() == RegistrationStatus.PENDING || registration.getRegistrationStatus() == RegistrationStatus.SUCCESSFUL) {
                pendingAndApprovedRegistrations.add(registration);
            }
        }
        return pendingAndApprovedRegistrations;
    }

    /**
     * Approve a HDB Officer's registration for a project
     * 
     * @param registration The OfficerRegistration object to be approved.
     * 
     * @throws IllegalStateException if the registration is not pending, or if the
     *                               project does not have available slots for officers.
     * 
     * @throws IllegalAccessException if the registration cannot be approved due to not
     *                                being able to find the project in the database.
     */
    public void approveRegistration(OfficerRegistration registration) throws IllegalStateException, IllegalAccessException {
        // Check if project still has available slots for officers
        if (registration.getProject().getAvailableOfficerSlots() - registration.getProject().getAssignedOfficers().size() <= 0) {
            throw new IllegalStateException("Cannot approve registration as there are no available slots.");
        }
        
        // Check if the registration is pending
        if (registration.getRegistrationStatus() == RegistrationStatus.PENDING) {
            // Update the registration status to successful
            registration.setRegistrationStatus(RegistrationStatus.SUCCESSFUL);
            // Update the registration in the database
            RegistrationDB.updateRegistration(registration);
            // Add the registration to the project
            BTOProject project = registration.getProject();
            project.addAssignedOfficer(registration.getOfficer().getName());
            // Update the project in the database
            BTOProjectDB.updateBTOProject(project.getName(), project);
        } else {
            throw new IllegalStateException("Cannot approve registration as it is not pending.");
        }
    }

    /**
     * Reject a HDB Officer's registration for a project
     * 
     * @param registration The OfficerRegistration object to be rejected.
     * 
     * @throws IllegalStateException if the registration is not pending.
     */
    public void rejectRegistration(OfficerRegistration registration) {
        // Check if the registration is pending
        if (registration.getRegistrationStatus() == RegistrationStatus.PENDING) {
            // Update the registration status to unsuccessful
            registration.setRegistrationStatus(RegistrationStatus.UNSUCCESSFUL);
            // Update the registration in the database
            RegistrationDB.updateRegistration(registration);
        } else {
            throw new IllegalStateException("Cannot reject registration as it is not pending.");
        }
    }

    /**
     * Approve a BTOApplication for a project managed by this manager.
     * 
     * @param application The BTOApplication object to be approved.
     * 
     * @throws IllegalStateException if the application is not pending or if the
     *                               project is not managed by this manager.
     */
    public void approveApplication(BTOApplication application) throws IllegalStateException {
        // Check if the application is pending
        if (application.getStatus() == ApplicationStatus.PENDING) {
            // Check if the project is managed by this manager
            boolean isManaged = false;
            for (BTOProject project : getManagedProjects()) {
                if (project.getProjectId() == application.getProject().getProjectId()) {
                    isManaged = true;
                    break;
                }
            }
            if (isManaged) {
                // Update the application status to successful
                application.setStatus(ApplicationStatus.SUCCESSFUL);
                // Update the application in the database
                ApplicationDB.updateApplication(application);
            } else {
                throw new IllegalStateException("Cannot approve application as it is not managed by this manager.");
            }
        } else {
            throw new IllegalStateException("Cannot approve application as it is not pending.");
        }
    }

    /**
     * Reject a BTOApplication for a project managed by this manager.
     * 
     * @param application The BTOApplication object to be rejected.
     * 
     * @throws IllegalStateException if the application is not pending or if the
     *                               project is not managed by this manager.
     */
    public void rejectApplication(BTOApplication application) throws IllegalStateException {
        // Check if the application is pending
        if (application.getStatus() == ApplicationStatus.PENDING) {
            // Check if the project is managed by this manager
            boolean isManaged = false;
            for (BTOProject project : getManagedProjects()) {
                if (project.getProjectId() == application.getProject().getProjectId()) {
                    isManaged = true;
                    break;
                }
            }
            if (isManaged) {
                // Update the application status to unsuccessful
                application.setStatus(ApplicationStatus.UNSUCCESSFUL);
                // Update the application in the database
                ApplicationDB.updateApplication(application);
            } else {
                throw new IllegalStateException("Cannot reject application as it is not managed by this manager.");
            }
        } else {
            throw new IllegalStateException("Cannot reject application as it is not pending.");
        }
    }

    /**
     * Generate a report of the list of applicants with their respective flat booking - flat type, project name, age, marital status.
     */
    public void generateReport() {
        // Get the list of all applicants
        List<BTOApplication> applications = ApplicationDB.getAllApplications();

        // Filter the applications to include only those with booked status
        applications.removeIf(application -> application.getStatus() != ApplicationStatus.BOOKED);

        // Sort applications by project name alphabetically, then by applicant name
        applications.sort((a, b) -> {
            int projectComparison = a.getProject().getName().compareTo(b.getProject().getName());
            if (projectComparison != 0) {
                return projectComparison;
            } else {
                return a.getApplicant().getName().compareTo(b.getApplicant().getName());
            }
        });

        // Print the header
        System.out.println(" " + "-".repeat(65));
        System.out.println("  Flat Booking Report: ");
        System.out.printf(" %15s | %15s | %3s | %7s | %6s \n", "Project Name", "Applicant Name", "Age", "Marital Status", "Flat Type");
        System.out.println(" " + "-".repeat(65));

        // Print the details of all applications
        for (BTOApplication application : applications) {
            System.out.printf(" %15s | %15s | %3d | %7s | %6s \n",
                    application.getProject().getName(),
                    application.getApplicant().getName(),
                    application.getApplicant().getAge(),
                    application.getApplicant().getMaritalStatus(),
                    application.getFlatType().getDisplayName()
            );
            System.out.println(" " + "-".repeat(65));
        }
    }

    /**
     * Generate a report of the list of applicants with their respective flat booking - flat type, project name, age, marital status.
     * Supports filtering based on various categories such as min_age, max_age, marital status, and project name.
     * 
     * If any of the parameters are null, assume that the user wants to include all applicants in that category.
     * 
     * @param minAge The minimum age of the applicants to be included in the report.
     * @param maxAge The maximum age of the applicants to be included in the report.
     * @param maritalStatus The marital status of the applicants to be included in the report.
     * @param projectName The name of the project to be included in the report.
     * @param flatType The type of flat to be included in the report.
     */
    public void generateReport(Integer minAge, Integer maxAge, MaritalStatus maritalStatus, String projectName, FlatType flatType) {
        // Get the list of all applicants
        List<BTOApplication> applications = ApplicationDB.getAllApplications();

        // Filter the applications to include only those with booked status
        applications.removeIf(application -> application.getStatus() != ApplicationStatus.BOOKED);

        // Sort applications by project name alphabetically, then by applicant name
        applications.sort((a, b) -> {
            int projectComparison = a.getProject().getName().compareTo(b.getProject().getName());
            if (projectComparison != 0) {
                return projectComparison;
            } else {
                return a.getApplicant().getName().compareTo(b.getApplicant().getName());
            }
        });

        // Print the header
        System.out.println(" " + "-".repeat(65));
        System.out.println("  Flat Booking Report: ");
        System.out.printf(" %15s | %15s | %3s | %7s | %6s \n", "Project Name", "Applicant Name", "Age",
                "Marital Status", "Flat Type");
        System.out.println(" " + "-".repeat(65));

        // Filter the applications based on the provided criteria
        for (BTOApplication application : applications) {
            if ((minAge == null || application.getApplicant().getAge() >= minAge) &&
                (maxAge == null || application.getApplicant().getAge() <= maxAge) &&
                (maritalStatus == null || application.getApplicant().getMaritalStatus() == maritalStatus) &&
                (projectName == null || application.getProject().getName().toLowerCase().contains(projectName.toLowerCase())) &&
                (flatType == null || application.getFlatType() == flatType)) {
                // Print the details of the filtered applications
                System.out.printf(" %15s | %15s | %3d | %7s | %6s \n",
                        application.getProject().getName(),
                        application.getApplicant().getName(),
                        application.getApplicant().getAge(),
                        application.getApplicant().getMaritalStatus(),
                        application.getFlatType().getDisplayName());
                System.out.println(" " + "-".repeat(65));
            }
        }
    }

    /**
     * View enquiries of all projects, even if the projects are not managed by this manager.
     * 
     * @return A list of Enquiry objects for all projects.
     */
    public List<Enquiry> viewEnquiries() {
        return EnquiryDB.getEnquiryList();
    }

    /**
     * Reply to an enquiry from an applicant for a project managed by this manager.
     * 
     * @param enquiry The Enquiry object to reply to.
     * @param reply The reply message.
     * 
     * @throws IllegalStateException if the enquiry is not managed by this manager.
     * @see Enquiry#addReplyMessage(User, String)
     */
    public void replyToEnquiry(Enquiry enquiry, String reply) throws IllegalStateException {
        // Check if the project is managed by this manager
        boolean isManaged = false;
        for (BTOProject project : getManagedProjects()) {
            if (project.getName().equals(enquiry.getProjectName())) {
                isManaged = true;
                break;
            }
        }
        if (isManaged) {
            // Reply to the enquiry
            enquiry.addReplyMessage(this, reply);
            // Update the enquiry in the database
            EnquiryDB.updateEnquiry(enquiry);
        } else {
            throw new IllegalStateException("Cannot reply to this enquiry as it is not managed by this officer.");
        }
    }
}
