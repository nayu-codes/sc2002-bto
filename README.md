# SC/CE/CZ2002: Object-Oriented Design & Programming Assignment
## Build-To-Order (BTO) Management System

### Overview

The BTO Management System is a centralized platform designed for applicants and HDB staff to view, apply for, and manage BTO projects.

### System Features

*   **Centralized Hub:** Acts as a single point of access for all users (applicants and HDB staff).
*   **Authentication:**
    *   Users log in using their account credentials.
    *   User ID: NRIC format (starts with S or T, followed by 7 digits, ends with a letter).
    *   Default Password: `password` (Users can change their password within the system).
*   **User Information:** Stores additional user details like Age and Marital Status.
*   **Initialization:** User data can be loaded from a file during system startup.

### Running the Project

1.  Ensure you have Java Runtime Environment (JRE) installed on your system.
2.  Navigate to the directory containing the `BTOApp.jar` file in your terminal or command prompt.
3.  Run the application using the following command:
    ```bash
    java -jar BTOApp.jar
    ```

### Implemented Features

**Databases:**
- [x] `UserDB`: Manages user data persistence.
- [x] `BTOProjectDB`: Manages BTO project data persistence.
- [x] `EnquiryDB`: Manages user Enquiries
- [x] `RegistrationDB`: Manages HDB Officer registration requests to handle BTO Projects
- [x] `ApplicationDB`: Manages Applicants' BTO Applications

**Models:**
- [x] `User` (and subclasses `Applicant`, `HDBOfficer`, `HDBManager`): Represents different user types.
- [x] `BTOProject`: Represents a BTO project with details like name, location, flat types, prices.
- [x] `UserFilter`: Static class to hold user filter preferences.
- [x] `BTOApplication`: Represents a BTO Application.
- [x] `Enquiry`: Represents an Enquiry.
- [x] `Registration`: Represents an Officer Registration.

**UI:**
- [x] `LoginScreen`: Handles user login.
- [x] `ProjectDashboard`: Main dashboard for project viewing/filtering.
- [x] `ProjectDetails`: Displays details of a selected project.
- [x] Application UI components
- [x] Enquiry UI components
- [x] Profile Management UI

**Controllers:**
- [x] `AuthenticationController`: Handles user authentication logic.
- [x] `ProjectController`: Manages interactions related to BTO projects.
- [x] `ProjectFilter`: Handles filtering logic for projects.
- [x] `ApplicationController`: Handle applications.
- [x] `EnquiryController`: Handle enquiries.
- [x] `RegistrationController`: Handles registration requests.
- [x] `ReportController`: Generate per-applicant receipts, and overall reports.

### User-specific Features

**Applicants:**
- [x] View and filter projects
- [x] View and add/edit/delete enquiries
- [x] Submit/withdraw BTO applications

**HDB Officer:**
- [ ] Handle applicant enquiries
- [ ] Generate Application receipt for flat booking

**HDB Manager:**
- [ ] Create/delete BTO Projects
- [ ] Approve/reject HDB Officer registrations
- [ ] Approve/reject BTO Applications for managed projects
- [ ] Generate application reports