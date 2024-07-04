package dev.bhuwanupadhyay.demo.order.application.errors;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionTranslator extends ResponseEntityExceptionHandler {

}
