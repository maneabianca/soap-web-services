package com.soap.webservices.soapcoursemanagement.soap.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.soap.webservices.soapcoursemanagement.soap.bean.Course;

@Component
public class CourseDetailsService {
	
	public enum Status{
		SUCCESS,
		FAILURE
	}

	//create them in memory
	private static List<Course> courses = new ArrayList<>();
	
	static {
		Course firstCourse = new Course(1, "Spring", "10 Steps");
		courses.add(firstCourse);
		Course secondCourse = new Course(2, "Spring MVC", "10 Examples");
		courses.add(secondCourse);
		Course thirdCourse = new Course(3, "Spring Boot", "6k Students");
		courses.add(thirdCourse);
		Course fourthCourse = new Course(4, "Maven", "OK");
		courses.add(fourthCourse);
		
	}
	
	// Get the details for one course
	public Course findById(int id) {
		for(Course course: courses) {
			if(course.getId() == id) {
				return course;
			}
		}
		return null;
	}
	
	// Get all courses
	public List<Course> findAll(){
		return courses;
	}
	
	// Delete a course
	public Status deleteById(int id) {	
		Iterator<Course>  iterator = courses.iterator();
		while(iterator.hasNext()) {
			Course course = iterator.next();
			if(course.getId() == id) {
				iterator.remove();
				return Status.SUCCESS;
			}
		}
		return Status.FAILURE;
	}
	// updating course and new course
	
}
