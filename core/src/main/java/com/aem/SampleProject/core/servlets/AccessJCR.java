package com.aem.SampleProject.core.servlets;

import org.apache.sling.api.servlets.SlingSafeMethodsServlet;


@SlingServlet(
resourceTypes = "/bin/sling/",
selectors = extensions = "*",
methods = HttpConstants.METHOD_GET
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
