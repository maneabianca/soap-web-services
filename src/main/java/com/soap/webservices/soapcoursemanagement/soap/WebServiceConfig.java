package com.soap.webservices.soapcoursemanagement.soap;

import java.io.IOException;
import java.util.List;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

// Enable Spring Web Services
@EnableWs
//Spring Configuration
@Configuration
public class WebServiceConfig  extends WsConfigurerAdapter{

	
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
	
	
	@Bean
	public Wss4jSecurityInterceptor securityInterceptor() {
		
		Wss4jSecurityInterceptor interceptor = new Wss4jSecurityInterceptor();		
		// Validate incoming UsernameToken
		interceptor.setValidationActions("UsernameToken");
		interceptor.setValidationCallbackHandler(callbackHandler());    
		return interceptor;
		
	}

	private CallbackHandler callbackHandler() {
		return new SimplePasswordCallbackHandler();
	}

	@Override
	public void addInterceptors(List<EndpointInterceptor> interceptors) {
		interceptors.add(securityInterceptor());
	}
	
	
    static class SimplePasswordCallbackHandler implements CallbackHandler {
        @Override
        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            for (Callback callback : callbacks) {
            	   if (callback instanceof WSPasswordCallback passwordCallback) {
                       String id = passwordCallback.getIdentifier();
                       if ("user".equals(id)) {
                           passwordCallback.setPassword("password"); // expected password
                       } else {
                           throw new IOException("Unknown user: " + id);
                       }
                   } else {
                       throw new UnsupportedCallbackException(callback);
                   }
            }
        }
    }
}
