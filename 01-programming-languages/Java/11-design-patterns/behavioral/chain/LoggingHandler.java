package academy.javaengineering.patterns.behavioral.chain;

/**
 * Concrete Handler - Logging Handler.
 * Handles logging-related requests.
 */
public class LoggingHandler extends BaseHandler {

    @Override
    protected boolean process(String request) {
        if (request.startsWith("log:")) {
            System.out.println("LoggingHandler: Logging - " + request);
            return true;
        }
        return false;
    }
}
