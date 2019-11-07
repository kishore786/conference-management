package com.conference.management.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author kishore
 *
 */
public class ConferenceSourceException extends Throwable {
	private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	
	public ConferenceSourceException(Exception exception) {
		this(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");
    }
	
    public ConferenceSourceException(HttpStatus httpStatus, String message) {
        super(message);
    }
    public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
