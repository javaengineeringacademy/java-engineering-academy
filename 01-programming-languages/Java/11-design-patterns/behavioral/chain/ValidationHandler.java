package academy.javaengineering.patterns.behavioral.chain;

/**
 * Concrete Handler - Validation Handler.
 * Handles validation-related requests.
 */
public class ValidationHandler extends BaseHandler {

    @Override
    protected boolean process(String request) {
        if (request.startsWith("validate:")) {
            System.out.println("ValidationHandler: Validating - " + request);
            return true;
        }
        return false;
    }
}
