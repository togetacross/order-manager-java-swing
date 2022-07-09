package com.mycompany.CustomExceptions;

/**
 * Write and read file exceptions
 */
public class CustomIOException extends Exception {

    public CustomIOException() {
    }

    public CustomIOException(String string) {
        super(string);
    }

    public CustomIOException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public CustomIOException(Throwable thrwbl) {
        super(thrwbl);
    }

}
