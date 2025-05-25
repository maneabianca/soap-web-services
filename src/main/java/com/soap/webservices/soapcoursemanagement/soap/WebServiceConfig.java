package com.soap.webservices.soapcoursemanagement.soap;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

// Enable Spring Web Services
@EnableWs
//Spring Configuration
@Configuration
public class WebServiceConfig {

	
	//MessageDispatcherServlet
		//ApplicationContext
	//url -> /ws/* 
	
	//helps us to map the servlet to a URL
	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext context) {
		
		//handle all the requests and mapped a simple URL to it
		MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
		messageDispatcherServlet.setApplicationContext(context);
		messageDispatcherServlet.setTransformWsdlLocations(true);
		
		return new ServletRegistrationBean(messageDispatcherServlet, "/ws/*");		
	}
	
	
	// /ws/courses.wsdl
	@Bean(name="courses")
	public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema coursesSchema) {
		
		DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
		
		//PortType -> CoursePort
		//Namespace -> http://in28minutes.com/courses
		//ws
		//schema
		definition.setPortTypeName("CoursePort");
		definition.setTargetNamespace("http://in28minutes.com/courses");
		definition.setLocationUri("/ws");
		definition.setSchema(coursesSchema);
		
		return definition;
	}
	
	
	// we need to use this schema in our WSDL definition
	@Bean
	public XsdSchema coursesSchema() {
		return new SimpleXsdSchema(new ClassPathResource("course-details.xsd"));
	}
	
}
