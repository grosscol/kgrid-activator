package org.uofm.ot.pad.exceptions;

/**
 * Created by grosscol on 5/23/17.
 */
public class PayloadExecException extends RuntimeException {

    public PayloadExecException() {
        super();

    }

    public PayloadExecException(String message, Throwable cause, boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

    }

    public PayloadExecException(String message, Throwable cause) {
        super(message, cause);

    }

    public PayloadExecException(String message) {
        super(message);

    }

    public PayloadExecException(Throwable cause) {
        super(cause);

    }



}