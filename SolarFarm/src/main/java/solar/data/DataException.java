package solar.data;

public class DataException extends Exception {

    // Constructor, Throwable arg is the root cause exception
    public DataException(String message) {
        super(message);
    }
    public DataException(String message, Throwable cause) {
        super(message, cause);
    }
}
