package org.uofm.ot.pad.exceptions;

/**
 * Created by grosscol on 5/23/17.
 */
public class ExecException extends RuntimeException {

    public ExecException() {
        super();

    }

    public ExecException(String message, Throwable cause, boolean enableSuppression,
                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

    }

    public ExecException(String message, Throwable cause) {
        super(message, cause);

    }

    public ExecException(String message) {
        super(message);

    }

    public ExecException(Throwable cause) {
        super(cause);

    }



}