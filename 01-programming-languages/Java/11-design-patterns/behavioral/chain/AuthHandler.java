package academy.javaengineering.patterns.behavioral.chain;

/**
 * Concrete Handler - Authentication Handler.
 * Handles authentication-related requests.
 */
public class AuthHandler extends BaseHandler {

    @Override
    protected boolean process(String request) {
        if (request.startsWith("auth:")) {
            System.out.println("AuthHandler: Processing authentication - " + request);
            return true;
        }
        return false;
    }
}
