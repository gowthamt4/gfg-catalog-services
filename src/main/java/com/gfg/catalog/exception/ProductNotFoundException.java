package com.gfg.catalog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception to handle when no product found
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -970838785221465214L;
	public ProductNotFoundException(String exceptionMessage) {
		super(exceptionMessage);
	}
	

}
