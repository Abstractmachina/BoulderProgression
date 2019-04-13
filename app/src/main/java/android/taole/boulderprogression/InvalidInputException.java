package android.taole.boulderprogression;

public class InvalidInputException extends Exception {

        // Parameterless Constructor
        public InvalidInputException() {}

        // Constructor that accepts a message
        public InvalidInputException(String message)
        {
            super(message);
        }

}
