package bto.database;

public class Database {

    public static void init() {
        // Initialize each database
        UserDB.init();
        BTOProjectDB.init();
        RegistrationDB.init();
        ApplicationDB.init();
        EnquiryDB.init();
    }
}
