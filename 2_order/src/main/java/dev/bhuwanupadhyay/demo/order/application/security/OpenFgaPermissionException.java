package dev.bhuwanupadhyay.demo.order.application.security;

public class OpenFgaPermissionException extends RuntimeException {

    public OpenFgaPermissionException(Exception e) {
        super(e);
    }

    public OpenFgaPermissionException(String message) {
        super(message);
    }

    public OpenFgaPermissionException(String message, Exception e) {
        super(message, e);
    }
}
