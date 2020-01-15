package com.aem.SampleProject.core.servlets;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
//import org.osgi.service.component.annotations.Reference;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;


@SlingServlet(
resourceTypes = "/bin/sling/", selectors = "rest", extensions = "*",methods = "GET"
)
public class AccessJCR extends SlingSafeMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) {
		
	}

}
