package com.soap.webservices.soapcoursemanagement.soap.exception;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode=FaultCode.CLIENT)
public class CourseNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -180604455581224502L;

	public CourseNotFoundException(String message) {
		super(message);
	}
	
}
