package com.mycompany.CustomExceptions;

/**
 * Database exceptions
 */
public class CustomDatabaseException extends Exception {

    public CustomDatabaseException() {
    }

    public CustomDatabaseException(String string) {
        super(string);
    }

    public CustomDatabaseException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public CustomDatabaseException(Throwable throwable) {
        super(throwable);
    }

}
