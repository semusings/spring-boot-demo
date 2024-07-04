package dev.bhuwanupadhyay.demo.payment.application.security;

public class AuthorizationException extends RuntimeException {

  public AuthorizationException(Exception e) {
    super(e);
  }

  public AuthorizationException(String message) {
    super(message);
  }

  public AuthorizationException(String message, Exception e) {
    super(message, e);
  }
}