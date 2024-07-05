package dev.bhuwanupadhyay.demo.order.application.errors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionTranslator extends ResponseEntityExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<Object> handleAnyException(Exception ex, NativeWebRequest request) {
    HttpStatusCode status = HttpStatusCode.valueOf(500);
    ProblemDetail body = this.createProblemDetail(ex, status, ex.getMessage(),
        "error.http.internal", null, request);
    return this.handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
  }

}
