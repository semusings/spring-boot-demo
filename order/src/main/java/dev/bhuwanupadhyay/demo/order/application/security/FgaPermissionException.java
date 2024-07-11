package dev.bhuwanupadhyay.demo.order.application.security;

public class FgaPermissionException extends RuntimeException {

    public FgaPermissionException(Exception e) {
        super(e);
    }

    public FgaPermissionException(String message) {
        super(message);
    }

    public FgaPermissionException(String message, Exception e) {
        super(message, e);
    }
}
