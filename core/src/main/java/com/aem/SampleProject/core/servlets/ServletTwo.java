package com.aem.SampleProject.core.servlets;

import javax.jcr.Repository;
import javax.servlet.*;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
//import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * Servlet using the resource type for mapping
 * @author gkisife
 *
 */


//@SlingServlet(
//resourceTypes = "/rest/sling",selectors ="resource" ,extensions = "*", methods = "GET"
//)


@Component(
		service=Servlet.class,
		property= {
				"sling.servlet.resourceTypes="+"weretail/components/structure/page",
				"sling.servlet.selectors="+"sample"
		})
public class ServletTwo extends SlingSafeMethodsServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Reference
	Repository repo;
	
	
	public void doGet(final SlingHttpServletRequest req,final SlingHttpServletResponse resp) throws ServletException, IOException {
	
		resp.setHeader("Content-Type", "text/html");
		resp.getWriter().print("<h2> Servlet two called </h2>");
		resp.getWriter().close();
	}
	
	

}
