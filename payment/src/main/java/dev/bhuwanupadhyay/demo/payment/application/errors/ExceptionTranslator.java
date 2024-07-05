package dev.bhuwanupadhyay.demo.payment.application.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionTranslator extends ResponseEntityExceptionHandler {

  private final static Logger LOG = LoggerFactory.getLogger(ExceptionTranslator.class);

  @ExceptionHandler
  public ResponseEntity<Object> handleAnyException(Exception ex, NativeWebRequest request) {
    LOG.atError().log("", ex);
    HttpStatusCode status = HttpStatus.INTERNAL_SERVER_ERROR;
    String defaultDetail = ex.getMessage();
    String detailMessageCode = "error.http.internal";
    ProblemDetail problemDetail = this.createProblemDetail(ex, status, defaultDetail,
        detailMessageCode, null, request);
    return this.handleExceptionInternal(ex, problemDetail, new HttpHeaders(), status, request);
  }

}
