package com.shopalyst.coupondistribution.exception;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.shopalyst.coupondistribution.dto.ErrorDto;

@ControllerAdvice
public class CouponExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger LOG = LogManager.getLogger(CouponExceptionHandler.class);
	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(ApplicationException.class)
	protected ResponseEntity<Object> handleApplicationException(final ApplicationException ex,
			final WebRequest request) {
		final String errorMsg = messageSource.getMessage(ex.getCode(), null, "Something went wrong.", Locale.getDefault());
		return handleExceptionInternal(ex, new ErrorDto(ex.getCode(), errorMsg), new HttpHeaders(),
				HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
	final Function<ObjectError, ErrorDto> objErrToDto = error -> new ErrorDto("ERR_999", error.getDefaultMessage());
	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		final List<ErrorDto> errors = ex.getBindingResult().getAllErrors().stream().map(objErrToDto)
				.collect(Collectors.toList());
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleUnhandledException(final ApplicationException ex, final WebRequest request) {
		return handleExceptionInternal(ex, new ErrorDto("ERR_000", "Something went wrong."), new HttpHeaders(),
				HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	

}
