package com.soap.webservices.soapcoursemanagement.soap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.in28minutes.courses.CourseDetails;
import com.in28minutes.courses.DeleteCourseDetailsRequest;
import com.in28minutes.courses.DeleteCourseDetailsResponse;
import com.in28minutes.courses.GetAllCourseDetailsRequest;
import com.in28minutes.courses.GetAllCourseDetailsResponse;
import com.in28minutes.courses.GetCourseDetailsRequest;
import com.in28minutes.courses.GetCourseDetailsResponse;
import com.soap.webservices.soapcoursemanagement.soap.bean.Course;
import com.soap.webservices.soapcoursemanagement.soap.exception.CourseNotFoundException;
import com.soap.webservices.soapcoursemanagement.soap.service.CourseDetailsService;
import com.soap.webservices.soapcoursemanagement.soap.service.CourseDetailsService.Status;

@Endpoint
public class CourseDetailsEndpoint {
	
	@Autowired
	private CourseDetailsService service;

	//method
	//input - GetCourseDetailsRequest
	//output - GetCourseDetailsResponse
	
	//we need to tell Spring web services to process any request with this namespace and this name (course-details.xsd)
	@PayloadRoot(namespace = "http://in28minutes.com/courses", localPart = "GetCourseDetailsRequest")
	@ResponsePayload
	public GetCourseDetailsResponse processCourseDetailsRequest(@RequestPayload GetCourseDetailsRequest request) {	
		Course course = service.findById(request.getId());
		
		if(course == null) {
			throw new CourseNotFoundException("Invalid Course ID " + request.getId());
		}
		
		return mapCourseDetails(course);		
	}

	@PayloadRoot(namespace = "http://in28minutes.com/courses", localPart = "GetAllCourseDetailsRequest")
	@ResponsePayload
	public GetAllCourseDetailsResponse processAllCourseDetailsRequest(@RequestPayload GetAllCourseDetailsRequest request) {
		List<Course> courses = service.findAll();
		return mapAllCourseDetails(courses);
	}
	
	@PayloadRoot(namespace = "http://in28minutes.com/courses", localPart = "DeleteCourseDetailsRequest")
	@ResponsePayload
	public DeleteCourseDetailsResponse deleteCourseDetailsRequest(@RequestPayload DeleteCourseDetailsRequest request) {
		DeleteCourseDetailsResponse response = new DeleteCourseDetailsResponse();
		Status status = service.deleteById(request.getId());
		response.setStatus(mapStatus(status));
		return response;
	}
	
	
	private com.in28minutes.courses.Status mapStatus(Status status) {
		return status == Status.FAILURE ? com.in28minutes.courses.Status.FAILURE : com.in28minutes.courses.Status.SUCCESS;
	}

	private GetCourseDetailsResponse mapCourseDetails(Course course) {
		GetCourseDetailsResponse response = new GetCourseDetailsResponse();
		response.setCourseDetails(mapCourse(course));
		return response;
	}

	private GetAllCourseDetailsResponse mapAllCourseDetails(List<Course> courses) {		
		GetAllCourseDetailsResponse response = new GetAllCourseDetailsResponse();		
		for(Course course: courses) {
			CourseDetails mapCourse = mapCourse(course);
			response.getCourseDetails().add(mapCourse);			
		}	
		return response;
	}

	private CourseDetails mapCourse(Course course) {
		CourseDetails courseDetails = new CourseDetails();
		courseDetails.setId(course.getId());
		courseDetails.setName(course.getName());
		courseDetails.setDescription(course.getDescription());
		return courseDetails;
	}
	
}
