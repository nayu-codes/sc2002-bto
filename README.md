# SC/CE/CZ2002: Object-Oriented Design & Programming Assignment: Building an OO Application
## Build-To-Order (BTO) Management System 
BTO  Management  System  is  a  system  for  applicants  and  HDB  staffs  to  view, apply and manage for BTO projects.

The following are information about the system:
__Overview of the System:__
- The system will act as a centralized hub for all applicants and HDB staffs 
- All users will need to login to this hub using their Singpass account. 
    - User ID will be their NRIC, that starts with S or T, followed by 7-
digit number and ends with another letter 
    - Assume all users use the default password, which is password. 
    - A user can change password in the system. 
    - Additional Information about the user: Age and Marital Status 
- A  user  list  can  be  initiated  through  a  file  uploaded  into  the  system  at initialization.

## Implemented Features
Databases:
- [x] UserDB
- [x] BTOProjectsDB
- [ ] EnquiryDB
- [ ] RegistrationDB
- [ ] ApplicationDB

UI
- [x] Login Screen

Controller
- [ ] AuthenticationController