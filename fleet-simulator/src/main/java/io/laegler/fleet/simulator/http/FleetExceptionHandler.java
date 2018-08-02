package io.laegler.fleet.simulator.http;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.util.CollectionUtils.isEmpty;
import java.util.List;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class FleetExceptionHandler {

  // @ResponseBody
  // @ExceptionHandler({Exception.class})
  public ResponseEntity<FleetErrorMessage> handleException(Throwable exception) {
    log.error("Exception in Fleet Simulator: ", exception);

    exception = getNestedException(exception);
    String exceptionMsg = exception.getMessage();

    // Extract nested exceptions (potentially more than one)
    if (exception instanceof MethodArgumentNotValidException) {
      List<ObjectError> allErrors =
          ((MethodArgumentNotValidException) exception).getBindingResult().getAllErrors();
      if (!allErrors.isEmpty()) {
        exceptionMsg = allErrors.get(allErrors.size() - 1).getDefaultMessage();
      }
    }

    // Extract error message for param parse
    if (JsonMappingException.class.isAssignableFrom(exception.getClass())
        && !isEmpty(((JsonMappingException) exception).getPath())) {
      List<Reference> errorPath = ((JsonMappingException) exception).getPath();
      String errorField = errorPath.get(errorPath.size() - 1).getFieldName();
      exceptionMsg = format("Invalid value type for Field: %s.", errorField);
      if (exception instanceof InvalidFormatException) {
        exceptionMsg = new StringBuilder(exceptionMsg).append("Expect type: ")
            .append(((InvalidFormatException) exception).getTargetType().getSimpleName())
            .toString();
      }
    }

    HttpStatus statusFromException = resolveExceptionHttpStatus(exception);
    if (statusFromException == null) {
      statusFromException = INTERNAL_SERVER_ERROR;
    }
    return status(statusFromException)
        .body(FleetErrorMessage.builder().message(exceptionMsg).build());
  }

  private HttpStatus resolveExceptionHttpStatus(final Throwable exception) {
    ResponseStatus annotation =
        AnnotatedElementUtils.findMergedAnnotation(exception.getClass(), ResponseStatus.class);
    if (annotation != null) {
      return annotation.value();
    }
    return INTERNAL_SERVER_ERROR;
  }

  private Throwable getNestedException(final Throwable exception) {
    if (exception.getCause() != null) {
      return getNestedException(exception.getCause());
    } else {
      return exception;
    }
  }

}
