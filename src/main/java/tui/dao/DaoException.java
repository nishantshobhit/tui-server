package tui.dao;

public class DaoException extends RuntimeException {

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable t) {
        super(message, t);
    }
}
