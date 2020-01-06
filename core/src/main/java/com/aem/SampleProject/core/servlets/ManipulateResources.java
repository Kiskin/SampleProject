package com.aem.SampleProject.core.servlets;

import java.io.IOException;
import java.util.Iterator;

import javax.jcr.LoginException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + " =Simple sling servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.resourceTypes=" + "trainingproject/components/structure/page",
		"sling.servlet.paths=" + "/bin/rest/resource" })

public class ManipulateResources extends SlingSafeMethodsServlet {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Reference
	private ResourceResolverFactory resFactory;

	public Resource myMethod() {
		Resource res = null;
		try {
			String resourcePath = "/content/dam/sampleproject/jcr:content";
			ResourceResolver resourceResolver = resFactory.getAdministrativeResourceResolver(null);
			res = resourceResolver.getResource(resourcePath);
			

			resourceResolver.close();
		} catch (org.apache.sling.api.resource.LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/json");
		
		Iterator<Resource> it;
		JSONObject jsonobject = new JSONObject();
		JSONObject json = new JSONObject();
		Resource res = myMethod();
		Resource testResource = myMethod();
		try {
			json.put("test", testResource);
		
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (res != null) {
			it = res.listChildren();
			while (it.hasNext()) {

				try {
					Resource aResource = it.next();
					
					jsonobject.append("test", aResource.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			it = null;
			try {
				jsonobject.append("R1","Resource was empty!");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		resp.getWriter().println(json.toString());

	}

}
