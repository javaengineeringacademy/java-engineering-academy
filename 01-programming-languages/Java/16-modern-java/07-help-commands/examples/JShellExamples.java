package academy.javaengineering.modern.helpcommands;

/**
 * JShell scripting examples.
 */
public class JShellExamples {

    /*
     * JShell is an interactive REPL for Java.
     * Run: jshell
     * 
     * Basic Commands:
     * /help          - Show help
     * /vars          - Show variables
     * /methods       - Show methods
     * /types         - Show types
     * /list          - Show code history
     * /save          - Save session
     * /open          - Open file
     * /exit          - Exit jshell
     * 
     * Example Session:
     * jshell> var x = 10
     * x ==> 10
     * jshell> var y = 20
     * y ==> 20
     * jshell> x + y
     * $3 ==> 30
     * jshell> /vars
     * |  int x = 10
     * |  int y = 20
     * |  int $3 = 30
     * jshell> /exit
     * |  Goodbye
     */

    public static void main(String[] args) {
        System.out.println("=== JShell Examples ===");
        System.out.println("To use JShell, run: jshell");
        System.out.println("\nBasic JShell Commands:");
        System.out.println("  /help          - Show help");
        System.out.println("  /vars          - Show variables");
        System.out.println("  /methods       - Show methods");
        System.out.println("  /types         - Show types");
        System.out.println("  /list          - Show code history");
        System.out.println("  /save          - Save session");
        System.out.println("  /open          - Open file");
        System.out.println("  /exit          - Exit jshell");
        System.out.println("\nExample JShell Session:");
        System.out.println("  jshell> var x = 10");
        System.out.println("  x ==> 10");
        System.out.println("  jshell> var y = 20");
        System.out.println("  y ==> 20");
        System.out.println("  jshell> x + y");
        System.out.println("  $3 ==> 30");
    }
}
