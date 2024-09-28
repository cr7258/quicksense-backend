package pro.quicksense.exception;

public class CustomExceptions {

    public static class LoginException extends RuntimeException {
        public LoginException(String message) {
            super(message);
        }
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
