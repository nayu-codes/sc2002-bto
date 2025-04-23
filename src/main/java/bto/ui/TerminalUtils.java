package bto.ui;

public class TerminalUtils {
    private TerminalUtils(){} // Prevents instantiation
    /**
     * Clears the screen whenever there is a change of UI for better readibility
     */
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
            }
            System.out.flush();
        } catch (Exception e) {
            // Fallback: Print empty lines
            System.out.println("\n".repeat(50));
        }
    }
}